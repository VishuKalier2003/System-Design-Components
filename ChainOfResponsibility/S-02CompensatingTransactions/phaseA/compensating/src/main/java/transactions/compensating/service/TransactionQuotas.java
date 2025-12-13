package transactions.compensating.service;

import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import transactions.compensating.enums.ResourceRequest;
import transactions.compensating.model.Resource;

@Service
public class TransactionQuotas implements Resource {
    private final TreeSet<String> tokens = new TreeSet<>();
    private static final String KEYS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @PostConstruct
    public void init() {
        tokens.add(generateToken());
    }

    private synchronized String generateToken() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 10; i++)
            sb.append(KEYS.charAt(ThreadLocalRandom.current().nextInt(0, KEYS.length())));
        return sb.toString();
    }

    public synchronized String catchToken() {
        String first = tokens.first();
        tokens.remove(first);
        return first;
    }

    public synchronized boolean tokensExist() {return !tokens.isEmpty();}

    public synchronized void createToken() {tokens.add(generateToken());}

    @Override
    public ResourceRequest getResourceType() {
        return ResourceRequest.QUOTAS;
    }
}
