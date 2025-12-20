package iam.aws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iam.aws.data.dto.Token;

@Service
public class TokenManager {

    @Autowired private TokenPatcher tp;
    @Autowired private TokenStore ts;

    public String createToken(String scope) {
        Token tkn = tp.createToken(scope, 10, 60);
        ts.insertToken(tkn);
        return tkn.getTokenID();
    }

    public boolean validateToken(String tknID) {
        if(!ts.exist(tknID))
            return false;
        Token tkn = ts.getToken(tknID);
        if(tp.isExpired(tkn))
            return true;
        Token newTkn = tp.degrade(tkn);
        ts.degradeToken(tknID, newTkn);
        return true;
    }
}
