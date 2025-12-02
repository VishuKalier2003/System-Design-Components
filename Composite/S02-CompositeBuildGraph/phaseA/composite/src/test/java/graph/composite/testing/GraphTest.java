package graph.composite.testing;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import graph.composite.dto.ExecutionOutput;
import graph.composite.enums.ConfigType;
import graph.composite.generator.CompositeFactory;
import graph.composite.model.Composite;
import graph.composite.service.ExecutionManager;
import graph.composite.service.NodeManager;
import graph.composite.utils.Configuration;

@SpringBootTest
class GraphTest {

    @Test
    void test1() {
        System.out.println("---------------------------------------------------------");
        System.out.println("                    Graph Test loaded....");
        System.out.println("---------------------------------------------------------");
        assertNull(null, "");
    }

    @Autowired private CompositeFactory factory;

    @Test
    void test2() {
        System.out.println("-----> TEST 1: Leaf and Container Creation");
        Composite c1 = factory.createLeafNode(true), c2 = factory.createLeafNode(false);
        assertEquals(true, c1.isLeaf(), "TEST 1: Error in Leaf Creation");
        assertEquals(false, c2.isLeaf(), "TEST 1: Error in Container Creation");
        c1.show();
        c2.show();
        System.out.println("TEST 1: PASSED - Leaf and Container Creation");
    }

    @Autowired private NodeManager nm;

    @Test
    void test3() {
        System.out.println("-----> TEST 2: Graph Creation and Bfs Traversal");
        Composite root = factory.createLeafNode(false);
        Composite c1 = factory.createLeafNode(true);
        Composite c2 = factory.createLeafNode(false);
        Composite c3 = factory.createLeafNode(true);
        Composite c4 = factory.createLeafNode(true);
        System.out.println("New added nodes : "+root.getName()+" "+c1.getName()+" "+c2.getName()+" "+c3.getName()+" "+c4.getName());
        assertEquals(true, nm.attachRoot(root), "TEST 2: Error in Root Attachment");
        nm.attachContainerOrLeaf(root.getName(), c1);
        nm.attachContainerOrLeaf(root.getName(), c2);
        nm.attachContainerOrLeaf(c2.getName(), c3);
        nm.attachContainerOrLeaf(c2.getName(), c4);
        assertEquals(nm.bfs().stream().map(Composite::getName).toList(), Stream.builder().add("Node-3").add("Node-4").add("Node-5").add("Node-6").add("Node-7").build().toList(), "TEST 2 : Error as mismatched Bfs traversal");
        System.out.println("TEST 2: PASSED - Graph Creation and Bfs Traversal");
    }

    @Autowired private ExecutionManager em;

    @Test
    void test4() {
        // Creating configuration for resources
        Configuration cf1 = new Configuration();
        cf1.addConfiguration(ConfigType.FUEL, 15);
        cf1.addConfiguration(ConfigType.TOKEN, 1);
        Map<String, Configuration> mConfig = new HashMap<>();
        mConfig.put("Node-3", cf1);
        System.out.println(em.isGraphValid("Node-3").getStatus());
        System.out.println(em.executeGraph("Node-3", mConfig).stream().map(ExecutionOutput::print).toList());
    }
}
