package filesystem.virtual.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import filesystem.virtual.Enum.OperationStatus;
import filesystem.virtual.api.input.Request;
import filesystem.virtual.errors.PathException;
import filesystem.virtual.service.admin.VfsManager;

import org.springframework.web.bind.annotation.GetMapping;

import filesystem.virtual.api.input.RequestPath;


@RestController
@RequestMapping("/vfs")
public class CompositeController {
    @Autowired private VfsManager vfsManager;

    @PostMapping("/createRoot/{rootName}")
    public ResponseEntity<String> createRoot(@PathVariable String rootName) {
        try {
            if(!vfsManager.attachToRoot(rootName))
                throw new IllegalStateException();
            return ResponseEntity.accepted().body("Root defined for the Virtual File System");
        } catch(IllegalStateException e) {
            return ResponseEntity.unprocessableContent().body("Root already defined, cannot define new root");
        }
    }

    @PostMapping("/addNode")
    public ResponseEntity<String> createNode(@RequestBody Request input) {
        try {
            if(vfsManager.addNode(input.getPath(), input.getNodeName()) != OperationStatus.DONE)
                throw new PathException(input.getPath());
            return ResponseEntity.accepted().body("Node attached to Virtual File System");
        } catch(PathException e) {
            return ResponseEntity.unprocessableContent().body("Node cannot be attached due to "+e.getLocalizedMessage());
        }
    }

    @PostMapping("/addLeaf")
    public ResponseEntity<String> createLeaf(@RequestBody Request input) {
        try {
            if(vfsManager.addLeaf(input.getPath(), input.getNodeName()) != OperationStatus.DONE)
                throw new PathException(input.getPath());
            return ResponseEntity.accepted().body("Leaf attached to Virtual File System");
        } catch(PathException e) {
            return ResponseEntity.unprocessableContent().body("Leaf cannot be attached due to "+e.getLocalizedMessage());
        }
    }

    @GetMapping("/root")
    public ResponseEntity<String> getMethodName() {
        return ResponseEntity.accepted().body(vfsManager.getRoot().getNodeName());
    }

    @GetMapping("/getChildren")
    public ResponseEntity<Object> getChildren(@RequestBody RequestPath rp) {
        try {
            return ResponseEntity.accepted().body(vfsManager.getChildren(rp.getPath()));
        } catch(Exception e) {
            return ResponseEntity.unprocessableContent().body("Error in finding children of path "+rp.getPath());
        }
    }

    @GetMapping("/getSubtree")
    public ResponseEntity<Object> getSubtree(@RequestBody RequestPath rp) {
        try {
            return ResponseEntity.accepted().body(vfsManager.getSubtreeDfs(rp.getPath()));
        } catch(Exception e) {
            return ResponseEntity.unprocessableContent().body("Error in finding children of path "+rp.getPath());
        }
    }
}
