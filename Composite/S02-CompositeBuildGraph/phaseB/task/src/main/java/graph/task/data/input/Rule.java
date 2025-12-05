package graph.task.data.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

// Detail: Rule provided by the User
@Getter
@Setter
public class Rule {
    private final String ruleID;
    private String parent;
    private List<String> dependents;
    private Map<String, NodeInput> requirementMap;

    public Rule(String ID) {
        this.ruleID = ID;
        this.parent = null;
        this.dependents = new ArrayList<>();
        this.requirementMap = new HashMap<>();
    }

    @Getter
    @Setter
    public class NodeInput {
        private List<String> resources;
        // techniques like cache, retry, provider, etc.
        private Map<String, String> features;       // key stores the property and value the enum name

        public NodeInput() {
            this.resources = new ArrayList<>();
            this.features = new HashMap<>();
        }

        public int size() {return resources.size();}
    }
}
