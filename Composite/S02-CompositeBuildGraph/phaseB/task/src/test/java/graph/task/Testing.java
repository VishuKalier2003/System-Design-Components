package graph.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import graph.task.admin.ExecutionManager;
import graph.task.admin.NodeManager;
import graph.task.data.input.Rule;
import graph.task.database.Database;
import graph.task.enums.ActionStatus;
import graph.task.model.Composite;

// Testing done here
@SpringBootTest
class Testing {
    @Autowired
    private ApplicationContext app;

    @Test
    void t1_createParentAndChildren() {
        NodeManager nm = app.getBean(NodeManager.class);
        Database db = app.getBean(Database.class);
        Rule r = new Rule("R1");
        r.setParent("A");
        r.getDependents().add("B");
        r.getDependents().add("C");
        nm.prepareRule(r);
        assertTrue(db.contains("A"));
        assertTrue(db.contains("B"));
        assertTrue(db.contains("C"));
        assertEquals(2, db.get("A").getDependencies().size());
    }

    @Test
    void t2_basicExecutionPureFlow() {
        ExecutionManager em = app.getBean(ExecutionManager.class);
        NodeManager nm = app.getBean(NodeManager.class);

        Rule rule = new Rule("R100");
        rule.setParent("P");
        rule.getDependents().add("X");

        // Node Input config
        Rule.NodeInput nX = rule.new NodeInput();
        nX.getResources().add("quota");
        nX.getFeatures().put("provider", "pure"); // Base METAL PURE function
        rule.getRequirementMap().put("X", nX);

        nm.prepareRule(rule);

        var out = em.executeRule(rule);
        assertEquals(ActionStatus.SUCCESS, out.getActionStatus());
        assertTrue(out.getLogs().stream().anyMatch(s -> s.contains("QUOTA")));
    }

    @Test
    void t3_cacheHitScenario() {
        ExecutionManager em = app.getBean(ExecutionManager.class);
        NodeManager nm = app.getBean(NodeManager.class);

        Rule rule = new Rule("C1");
        rule.setParent("Z");
        Rule.NodeInput node = rule.new NodeInput();
        node.getResources().add("quota");
        node.getFeatures().put("provider", "pure");
        node.getFeatures().put("cache", "lru");
        rule.getRequirementMap().put("Z", node);

        nm.prepareRule(rule);

        var first = em.executeRule(rule);
        var second = em.executeRule(rule);

        assertEquals(ActionStatus.SUCCESS, first.getActionStatus());
        assertEquals(ActionStatus.CACHE_HIT, second.getActionStatus());
    }

    @Test
    void t4_retryExecutionFlow() {
        ExecutionManager em = app.getBean(ExecutionManager.class);
        NodeManager nm = app.getBean(NodeManager.class);

        Rule rule = new Rule("R_FAIL");
        rule.setParent("Y");
        Rule.NodeInput node = rule.new NodeInput();

        node.getResources().add("flag"); // random → might fail
        node.getFeatures().put("provider", "impure");
        node.getFeatures().put("retry", "half");

        rule.getRequirementMap().put("Y", node);

        nm.prepareRule(rule);
        var result = em.executeRule(rule);
        assertTrue(!result.getLogs().isEmpty());
    }

    @Test
    void t5_dfsTraversalIntegrity() {
        NodeManager nm = app.getBean(NodeManager.class);
        Rule r = new Rule("R_TREE");
        r.setParent("ROOT");
        r.getDependents().addAll(List.of("A", "B", "C"));
        nm.prepareRule(r);
        List<Composite> nodes = nm.bfs("ROOT");
        assertEquals(4, nodes.size());
    }

}
