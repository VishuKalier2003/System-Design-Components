package filesystem.virtual.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import filesystem.virtual.Enum.OperationStatus;
import filesystem.virtual.api.input.RequestPath;
import filesystem.virtual.errors.FileFolderException;
import filesystem.virtual.service.admin.FileManager;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired private FileManager fileManager;

    @PostMapping("/create/file")
    public ResponseEntity<String> createFile(@RequestBody RequestPath rp) {
        try {
            if(fileManager.createFile(rp.getPath()) != OperationStatus.DONE)
                throw new FileFolderException(rp.getPath());
            return ResponseEntity.accepted().body("File created at path "+rp.getPath());
        } catch(FileFolderException e) {
            return ResponseEntity.internalServerError().body("Error at file creating for path "+rp.getPath()+" as error "+e.getLocalizedMessage());
        }
    }

    @PostMapping("/create/folder")
    public ResponseEntity<String> createFolder(@RequestBody RequestPath rp) {
        try {
            if(fileManager.createFolder(rp.getPath()) != OperationStatus.DONE)
                throw new FileFolderException(rp.getPath());
            return ResponseEntity.accepted().body("Folder created at path "+rp.getPath());
        } catch(FileFolderException e) {
            return ResponseEntity.internalServerError().body("Error at folder creating for path "+rp.getPath()+" as error "+e.getLocalizedMessage());
        }
    }

    @GetMapping("/read/file/{adapter}")
    public ResponseEntity<String> readFile(@PathVariable String adapter, @RequestBody RequestPath rp) {
        try {
            if(fileManager.readFile(rp.getPath()) != OperationStatus.DONE)
                throw new FileFolderException(rp.getPath());
            return ResponseEntity.accepted().body(fileManager.readMountedData(adapter.toUpperCase()));
        } catch(FileFolderException e) {
            return ResponseEntity.internalServerError().body("Error at reading for path "+rp.getPath()+" as error "+e.getLocalizedMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> del(@RequestBody RequestPath rp) {
        try {
            if(fileManager.removePathOrDirectory(rp.getPath()) != OperationStatus.DONE)
                throw new FileFolderException(rp.getPath());
            return ResponseEntity.accepted().body("Successful Deletion");
        } catch(FileFolderException e) {
            return ResponseEntity.internalServerError().body("Error at deleting for path "+rp.getPath()+" as error "+e.getLocalizedMessage());
        }
    }
}
