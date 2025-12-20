package iam.aws.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import iam.aws.data.dto.Token;

@Service
public class TokenStore {
    private final Map<String, Token> tknStore = new HashMap<>();

    public boolean insertToken(Token tkn) {
        if(!exist(tkn.getTokenID())) {
            return false;
        }
        tknStore.put(tkn.getTokenID(), tkn);
        return true;
    }

    public boolean exist(String id) {return tknStore.containsKey(id);}

    public Token getToken(String id) {return tknStore.get(id);}

    // using token Patcher get token and update it here
    public void degradeToken(String id, Token tkn) {
        if(!exist(id))
            return;
        tknStore.put(id, tkn);
    }

    public void deleteToken(String id) {
        tknStore.remove(id);
    }
}
