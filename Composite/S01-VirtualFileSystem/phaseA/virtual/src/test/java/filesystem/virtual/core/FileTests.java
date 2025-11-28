package filesystem.virtual.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import filesystem.virtual.Enum.OperationStatus;
import filesystem.virtual.service.admin.FileManager;

@SpringBootTest
class FileTests {

    @Test
    void testingStarts() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("                              FILES TESTING                               ");
        System.out.println("--------------------------------------------------------------------------");
        assertNull(null, "");
        System.out.println("TEST 0 check passed... BEGIN TESTING");
    }

    @Autowired private FileManager fileManager;

    // When accessing mount registry always use "/"
    @Test
    void test1() {
        final String p1 = "/root/tempA/folderTest/file_New.txt";
        fileManager.readFile(p1);
        assertEquals("The text is recently appended\n", fileManager.readMountedData("database"), "TEST 1 ERROR : The file manager is having issues in reading data");
        // Needs to clear cache after every read call
        fileManager.clearMountedData("database");
        System.out.println("TEST 1 PASSED : File Manager reads file data successfully");
    }

    @Test void test2() {
        final String p2 = "/root/tempA/folderTest/file_Another.txt";
        assertEquals(OperationStatus.DONE, fileManager.createFile(p2), "TEST 2 ERROR : Error in creating file at location '/root/tempA/folderTest/file_Another.txt'");
        String text2 = "This is another file";
        assertEquals(OperationStatus.DONE, fileManager.writeFile(p2, text2), "TEST 2 ERROR : Error in writing file at location '/root/tempA/folderTest/file_Another.txt'");
        assertEquals(OperationStatus.DONE, fileManager.readDirectory("/root/tempA/folderTest"), "TEST 2 ERROR : read error in reading directories");
        assertEquals("This is another file\n\nThe text is recently appended\n\n\n", fileManager.readMountedData("database"), "TEST 2 ERROR : Error in file");
    }
}
