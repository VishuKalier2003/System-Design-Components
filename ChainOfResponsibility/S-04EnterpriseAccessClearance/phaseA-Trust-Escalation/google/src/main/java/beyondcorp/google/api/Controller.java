package beyondcorp.google.api;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import beyondcorp.google.admin.ChainEngine;
import beyondcorp.google.service.Dispatcher;
import beyondcorp.google.service.TokenStore;
import beyondcorp.google.store.Output;
import beyondcorp.google.store.Token;
import beyondcorp.google.utils.OutputHelper;
import beyondcorp.google.utils.Tokenizer;
import beyondcorp.google.utils.input.Request;
import beyondcorp.google.utils.input.RequestInput;

@RestController
@RequestMapping("/google")
public class Controller {
    @Autowired private ChainEngine engine;
    @Autowired private Dispatcher dispatcher;
    @Autowired private OutputHelper helper;
    @Autowired private Tokenizer tokenizer;
    @Autowired private TokenStore tknStore;

    // detail: for testing purpose case, pass following-
    /**
     * geo - "INDIA"
     * uuid - "VishuKalier"
     * name - "Vishu"
     * channel - "DBZ0231"
     */
    @PostMapping("/request")
    public ResponseEntity<Object> request(@RequestBody Request input) {
        try {
            String tkn = tokenizer.createToken();
            Output inp = Output.builder().build();
            Output.ChainData chainData = inp.new ChainData(tkn);
            Output.TrustState state = inp.new TrustState(input.getUuid(), null, input.getMap());
            inp = inp.toBuilder().tokens(new HashSet<>()).transactionID(tkn).internalData(chainData).trustState(state).build();
            Output output = engine.executeRequest(inp);
            for(Token token : output.getTokens())
                tknStore.pushIntoStore(token);
            return ResponseEntity.accepted().body(output);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/allTokens")
    public ResponseEntity<Object> output(@RequestBody Output input) {
        try {
            return ResponseEntity.accepted().body(helper.getAllTokens(input));
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/active")
    public ResponseEntity<Object> active(@RequestBody Output input) {
        try {
            return ResponseEntity.accepted().body(helper.activeTokens(input));
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/dispatch")
    public ResponseEntity<Object> request(@RequestBody RequestInput input) {
        try {
            Token token = tknStore.get(input.getTkn());
            return ResponseEntity.accepted().body(dispatcher.dispatch(token, input.getInput()));
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> store() {
        try {
            return ResponseEntity.accepted().body(tknStore.getAll());
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }
}
