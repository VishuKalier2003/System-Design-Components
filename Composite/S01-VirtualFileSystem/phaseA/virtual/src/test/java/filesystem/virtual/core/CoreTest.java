package filesystem.virtual.core;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import filesystem.virtual.Enum.OperationStatus;
import filesystem.virtual.model.Composite;
import filesystem.virtual.service.admin.VfsManager;

@SpringBootTest
class CoreTest {

    @Test
    void testingStarts() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("                               CORE TESTING                               ");
        System.out.println("--------------------------------------------------------------------------");
        assertNull(null, "");
        System.out.println("TEST 0 check passed... BEGIN TESTING");
    }

    @Autowired private VfsManager vfsManager;

    @Test
    void test1() {
        vfsManager.attachToRoot("root");
        assertEquals("root", vfsManager.getRoot().getNodeName(), "TEST 1 ERROR : 'root' node name not same when creating since 'root' is : "+vfsManager.getRoot().getNodeName());
        System.out.println("TEST 1 PASSED : 'root' is created successfully...");
    }

    @Test
    void test2() {
        vfsManager.addNode("/root", "tempA");
        assertEquals("tempA", vfsManager.get("/root/tempA").getNodeName(), "TEST 2 ERROR : the non-root nodes are not attaching properly with the provided path '/root/tempA', got node name as "+vfsManager.getRoot().getNodeName());
        vfsManager.addNode("/root", "tempB");
        assertEquals("tempB", vfsManager.get("/root/tempB").getNodeName(), "TEST 2 ERROR : the non-root nodes are not attaching properly with the provided path '/root/tempB', got node name as "+vfsManager.getRoot().getNodeName());
        vfsManager.addLeaf("/root/tempA", "leafA");
        assertEquals("leafA", vfsManager.get("/root/tempA/leafA").getNodeName(), "TEST 2 ERROR : the non-root nodes are not attaching properly with the provided path '/root/tempA/leafA', got node name as "+vfsManager.getRoot().getNodeName());
        vfsManager.addNode("/root/tempB", "tempC");
        assertEquals("tempC", vfsManager.get("/root/tempB/tempC").getNodeName(), "TEST 2 ERROR : the non-root nodes are not attaching properly with the provided path '/root/tempB', got node name as "+vfsManager.getRoot().getNodeName());
        System.out.println("TEST 2 PASSED : non-root nodes created successfully...");
    }

    @Test
    void test3() {
        assertEquals(OperationStatus.PATH_ERROR, vfsManager.addLeaf("/root/xyz/", "tempX"), "TEST 3 ERROR : not firing OperationStatus.PATH_ERROR correctly");
        System.out.println("TEST 3 PASSED : Correctly Firing OperationStatus.PATH_ERROR...");
    }

    @Test
    void test4() {
        assertEquals(OperationStatus.FAIL, vfsManager.addNode("/root", "tempA"), "TEST 4 ERROR : failure of Idempotency, multiple same nodes of parent created");
        System.out.println("TEST 4 PASSED : Idempotency cases passed...");
    }

    @Test
    void test5() {
        vfsManager.addLeaf("/root", "leafX");
        List<String> nodeNames = vfsManager.getChildren("/root").stream().map(Composite::getNodeName).toList();
        assertEquals(nodeNames, Stream.builder().add("tempA").add("tempB").add("leafX").build().toList(), "TEST 5 ERROR : children not correctly traversed found children of 'root' as "+nodeNames);
        System.out.println("TEST 5 PASSED : children of node follow succession and idempotency...");
    }

    @Test
    void test6() {
        List<String> nodes = vfsManager.getSubtreeDfs("/root").stream().map(Composite::getNodeName).toList();
        assertEquals(nodes, Stream.builder().add("tempA").add("leafA").add("tempB").add("tempC").add("leafX").build().toList(), "TEST 6 ERROR : Incorrect traversal logic");
        System.out.println("TEST 6 PASSED : traversal follows correct order...");
    }
}
