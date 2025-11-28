package filesystem.virtual.core;

import static org.junit.jupiter.api.Assertions.assertNull;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import filesystem.virtual.Enum.OperationStatus;
import filesystem.virtual.middleware.MountRegistry;
import filesystem.virtual.model.Composite;
import filesystem.virtual.service.MountExtractor;
import filesystem.virtual.service.admin.VfsManager;
import filesystem.virtual.utils.FileData;

@SpringBootTest
class DatabaseTest {

    @Test
    void testingStarts() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("                             DATABASE TESTING                             ");
        System.out.println("--------------------------------------------------------------------------");
        assertNull(null, "");
        System.out.println("TEST 0 check passed... BEGIN TESTING");
    }

    @Autowired private DatabaseAdapter adapterTest;
    @Autowired private DataStoreAdapter adapterStore;

    @Test void test1() {
        assertEquals("D:/System-Design-Components/Composite/S01-VirtualFileSystem/phaseA/virtual/src/main/java/filesystem/virtual/database/dbTest", adapterTest.getHOME(), "TEST 1 ERROR : database path not correct");
        assertEquals("D:/System-Design-Components/Composite/S01-VirtualFileSystem/phaseA/virtual/src/main/java/filesystem/virtual/database/dbStore", adapterStore.getHOME(), "TEST 1 ERROR : database path not correct");
        System.out.println("TEST 1 PASSED : external database paths configured correctly...");
    }

    @Autowired private MountRegistry mountRegistry;
    @Autowired private VfsManager vfsManager;
    @Autowired private DatabaseAdapter dbAdapter;
    @Autowired private DataStoreAdapter dsAdapter;

    @Test void test2() {
        System.out.println(vfsManager.getSubtreeDfs("/root").stream().map(Composite::getNodeName).toList());
        assertEquals(OperationStatus.DONE, mountRegistry.createMount("tempA", dbAdapter), "TEST 2 ERROR : Error in mounting");
        assertEquals(OperationStatus.DONE, mountRegistry.createMount("tempC", dsAdapter), "TEST 2 ERROR : Error in mounting");
        System.out.println("TEST 2 PASSED : Test Mount successful, "+mountRegistry.getMountMap().entrySet().stream().map(x -> {
            return x.getKey() + " -> " + x.getValue().getTYPE();
        }).toList());
    }

    @Autowired private MountExtractor mountExtractor;

    @Test void test3() {
        // Never use a leading slash for relative paths
        final String path = "/root/tempA/folderTest/f2";
        // Use extractor response to extract information from a path
        FileData exRes = mountExtractor.extractPathDetails(path);
        // mount to be taken through the root name
        assertEquals(OperationStatus.DONE, mountRegistry.getMount(exRes.getRootName()).createFolder(exRes.getRelativeFilePath()), "TEST 3 ERROR : Error in creating folder");
        System.out.println("TEST 3 PASSED : Folder creation successful using a mounting directory string '/root/tempA/folderTest/f2'");
    }

    @Test void test4() {
        // Never use a leading slash for relative paths
        final String path = "/root/tempB/tempC/folderDataTest/f1";
        // Use extractor response to extract information from a path
        FileData exRes = mountExtractor.extractPathDetails(path);
        // mount to be taken through the root name
        System.out.println("ROOT NAME : "+exRes.getRootName()+" LINK : "+exRes.getRelativeFilePath()+" FILE NAME : "+exRes.getFileName());
        assertEquals(OperationStatus.DONE, mountRegistry.getMount(exRes.getRootName()).createFolder(exRes.getRelativeFilePath()), "TEST 4 ERROR : Error in creating folder");
        System.out.println("TEST 4 PASSED : Folder creation successful using a mounting directory string '/root/tempB/tempC/folderDataTest/f1'");
    }

    @Test void test5() {
        // Never use a leading slash for relative paths
        final String path = "/root/tempA/folderTest/f2/file.txt";
        // Use extractor response to extract information from a path
        FileData exRes = mountExtractor.extractPathDetails(path);
        assertEquals(OperationStatus.DONE, mountRegistry.getMount(exRes.getRootName()).createFile(exRes), "TEST 5 ERROR : Error in creating file");
        System.out.println("TEST 5 PASSED : File creation test successful using mounting directory string '/root/tempA/folderTest/f1'");
    }

    @Test void test6() {
        final String path = "/root/tempA/folderTest/f2/file.txt";
        FileData exRes = mountExtractor.extractPathDetails(path);
        assertEquals(OperationStatus.DONE, mountRegistry.getMount(exRes.getRootName()).remove(exRes.getRelativeFilePath()), "TEST 6 ERROR : Error in removing file");
        System.out.println("TEST 6 PASSED : File removal test successful using mounting directory '/root/tempA/folderTest/f2/file.txt'");
    }

    @Test void test7() {
        final String p1 = "/root/tempA/folderTest/file_New.txt";
        // Use extractor response to extract information from a path
        FileData exRes = mountExtractor.extractPathDetails(p1);
        assertEquals(OperationStatus.DONE, mountRegistry.getMount(exRes.getRootName()).createFile(exRes), "TEST 5 ERROR : Error in creating file");
        exRes.setText("The text is recently appended\n");
        mountRegistry.getMount(exRes.getRootName()).write(exRes);
        System.out.println("TEST 7 PASSED : File data append test successful using mounting directory string '/root/tempA/folderTest/file_New.txt'");
    }
}
