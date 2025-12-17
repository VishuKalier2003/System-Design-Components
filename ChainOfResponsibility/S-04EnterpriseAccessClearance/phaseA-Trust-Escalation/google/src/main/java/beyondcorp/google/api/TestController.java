package beyondcorp.google.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import beyondcorp.google.service.Dispatcher;
import beyondcorp.google.store.Token;
import beyondcorp.google.store.User;
import beyondcorp.google.store.enums.DatabaseActions;
import beyondcorp.google.utils.Tokenizer;

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired private Dispatcher dispatcher;
    @Autowired private Tokenizer tokenizer;

    @PostMapping("/push")
    public ResponseEntity<Object> create() {
        try {
            User user = User.builder().uuid("v1").name("Vishu").amount(2000).build();
            Token tkn = tokenizer.createToken(DatabaseActions.CREATE, 50);
            return ResponseEntity.accepted().body(dispatcher.dispatch(tkn, user));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> get() {
        try {
            Token tkn = tokenizer.createToken(DatabaseActions.GET, 50);
            return ResponseEntity.accepted().body(dispatcher.dispatch(tkn, "v1"));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }
}
