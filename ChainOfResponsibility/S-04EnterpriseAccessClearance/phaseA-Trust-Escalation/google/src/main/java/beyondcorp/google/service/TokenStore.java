package beyondcorp.google.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import beyondcorp.google.error.EntryNotExistException;
import beyondcorp.google.store.Token;

@Service
public class TokenStore {
    private final Map<String, Token> store = new HashMap<>();

    public void pushIntoStore(Token tkn) {
        store.put(tkn.getTokenID(), tkn);
    }

    public Token get(String tokenID) {
        if(store.containsKey(tokenID))
            return store.get(tokenID);
        throw new EntryNotExistException(tokenID);
    }

    public Map<String, Token> getAll() {return this.store;}
}
