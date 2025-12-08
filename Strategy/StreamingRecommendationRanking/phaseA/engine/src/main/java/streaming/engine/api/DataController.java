package streaming.engine.api;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import streaming.engine.data.User;
import streaming.engine.service.Store;

@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired private Store store;

    @PostMapping("/user/{name}")
    public ResponseEntity<String> createUser(@PathVariable String name) {
        try {
            User user = User.builder().userName(name).watchedIndex(new ArrayList<>()).activeDays(new ArrayList<>()).build();
            store.insert(name, user);
            return ResponseEntity.accepted().body("User created with user Id : "+user);
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Exception caused as : "+e.getLocalizedMessage());
        }
    }

    @GetMapping("/user/exists/{name}")
    public ResponseEntity<String> checkUser(@PathVariable String name) {
        try {
            return ResponseEntity.accepted().body("User state : "+store.exist(name));
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Exception caused as : "+e.getLocalizedMessage());
        }
    }

    @PostMapping("/user/day/{name}/{day}")
    public ResponseEntity<String> insertDay(@PathVariable String name, @PathVariable String day) {
        try {
            store.insertDay(name, day);
            return ResponseEntity.accepted().body("User watch day updated");
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Exception caused as : "+e.getLocalizedMessage());
        }
    }

    @PostMapping("/user/day/{name}/{index}")
    public ResponseEntity<String> insertAnimeIndex(@PathVariable String name, @PathVariable String index) {
        try {
            store.insertGenre(name, index);
            return ResponseEntity.accepted().body("User anime Index updated");
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Exception caused as : "+e.getLocalizedMessage());
        }
    }
}
