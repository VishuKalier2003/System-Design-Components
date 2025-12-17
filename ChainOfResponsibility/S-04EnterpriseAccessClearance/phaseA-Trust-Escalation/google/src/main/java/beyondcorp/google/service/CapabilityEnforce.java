package beyondcorp.google.service;

import org.springframework.stereotype.Service;

import beyondcorp.google.store.Token;

@Service
public class CapabilityEnforce {

    public boolean validate(Token token) {
        return !token.expired();
    }
}
