package graph.task.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import graph.task.core.decorators.CacheDecorator;
import graph.task.core.decorators.RetryDecorator;
import graph.task.core.decorators.RpDecorator;
import graph.task.core.engines.ExecutionEngine;
import graph.task.core.engines.RectificationEngine;
import graph.task.data.context.ExecutionContext;
import graph.task.data.input.ActionInput;
import graph.task.data.input.Rule;
import graph.task.data.input.Rule.NodeInput;
import graph.task.data.output.ActionOutput;
import graph.task.database.Database;
import graph.task.enums.ActionStatus;
import graph.task.model.Composite;
import graph.task.model.Marker;
import graph.task.model.modules.Executable;
import graph.task.model.modules.Preprocessing;
import graph.task.utils.Factory;
import lombok.Setter;

@Setter
@Service
public class ExecutionManager {

    @Autowired
    private ExecutionEngine eEngine;
    @Autowired
    private RectificationEngine rEngine;
    @Autowired
    private Database db;

    @Autowired
    @Qualifier("functions")
    private Map<String, Marker> map;

    private final ReentrantLock lock = new ReentrantLock();
    private final Logger log = LoggerFactory.getLogger(ExecutionManager.class);

    @SuppressWarnings("CallToPrintStackTrace")
    public ActionOutput executeRule(Rule rule) {
        // fixed: If using locks no need to use synchronized keyword
        try {
            lock.lock();
            Composite root = db.get(rule.getParent());
            // detail: builder used to create object without any data, references point to null in this case
            ActionOutput output = ActionOutput.builder().build();
            dfs(root, rule, output, rule.getRequirementMap());
            return output;
        } catch (Exception e) {
            log.info("EXCEPTION FOUND DURING EXECUTION");
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    // traversal logic, private to keep hidden
    private void dfs(Composite root, Rule rule, ActionOutput output, Map<String, Rule.NodeInput> map) {
        if (root == null)
            return;
        for (Composite neighbor : root.getDependencies()) {
            dfs(neighbor, rule, output, map);
        }
        NodeInput nInput = map.get(root.nodeName());
        // fixed: safe check if node input is not given, else error
        if(nInput == null)
            nInput = rule.new NodeInput();
        // fixed: The output node is passed to ensure the output is fetched correctly, else will return null
        execute(root, rule, nInput, null, output);
    }

    @Autowired
    private RpDecorator rpDecorator;
    @Autowired
    private CacheDecorator cacheDecorator;
    @Autowired
    private RetryDecorator retryDecorator;

    @Autowired
    private Factory factory;

    private ActionOutput execute(Composite root, Rule rule, NodeInput nodeInput, ActionInput in, ActionOutput out) {
        // First we access the provider
        Marker marker = map.get(nodeInput.getFeatures().get("provider"));
        // if inputs are null, the objects are built dynamically
        ActionOutput output = out == null ? ActionOutput.builder().logs(new ArrayList<>()).code(0).build() : out;
        ExecutionContext ctx = ExecutionContext.builder().build();
        ActionInput inp = in == null ? factory.create(rule, nodeInput) : in;
        // when no provider exist
        if(marker == null)
            return output;
        // technique: instanceof down-casting (type-safe when used with interfaces)
        if (marker instanceof Executable executable) {
            if (!root.isActiveExecutable())
                root.setExecution(executable);
            eEngine.setEngineFunction(executable);
            // decorator used to enrich the ExecutionContext object
            buildOnObject(output, eEngine.execute(inp, rpDecorator.enrich(ctx)));
        }
        marker = map.get(nodeInput.getFeatures().get("cache"));     // extract cache
        if(marker != null && marker instanceof Executable executable) {
                if (!root.isActiveExecutable())
                    root.setExecution(executable);
                eEngine.setEngineFunction(executable);
                // decorator used to enrich the ExecutionContext object
                buildOnObject(output, eEngine.execute(inp, cacheDecorator.enrich(ctx)));
            }
        // If failure occurs, pass a retry
        if (output.getActionStatus() != ActionStatus.SUCCESS && output.getActionStatus() != ActionStatus.CACHE_HIT) {
            marker = map.get(nodeInput.getFeatures().get("retry"));
            if(marker == null)
                return output;
            if (marker instanceof Preprocessing preprocess) {
                if (!root.isActivePreprocess())
                    root.setPreprocessing(preprocess);
                rEngine.setPreprocessFunction(preprocess);
                inp = rEngine.evaluate(inp, retryDecorator.enrich(ctx));
                if(inp.getRetryCount() > 3)     // fixed: fixed retry (recursion depth)
                    return output;
                ActionOutput rOut = execute(root, rule, nodeInput, inp, null);
                buildOnObject(output, rOut);
            }
        }
        return output;
    }

    // function to attach logs and data from second object into first object lazily
    private void buildOnObject(ActionOutput output, ActionOutput build) {
        List<String> logs = new ArrayList<>();
        if (output.getLogs() != null) {
            logs.addAll(output.getLogs());
        }
        if (build.getLogs() != null) {
            logs.addAll(build.getLogs());
        }
        output.setActionStatus(build.getActionStatus());
        output.setLogs(logs);
        output.setCode(build.getCode());
    }
}
