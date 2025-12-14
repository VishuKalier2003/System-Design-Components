package transactions.compensating.database;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import transactions.compensating.data.User;
import transactions.compensating.enums.Bank;
import transactions.compensating.enums.ResourceRequest;
import transactions.compensating.model.Resource;

@Service
public class Database implements Resource {
    // Stores bank name and set of values
    private final Map<Bank, Set<String>> db = new EnumMap<>(Bank.class);
    private final Map<String, Integer> balance = new HashMap<>();

    @PostConstruct
    public void init() {
        db.put(Bank.SBI, new HashSet<>());
        db.put(Bank.CANARA, new HashSet<>());
        db.put(Bank.PNB, new HashSet<>());
    }

    public synchronized void register(User user) {
        db.get(user.getBank()).add(user.getUsername());
        String hash = user.getUsername() + "-" + user.getBank();
        balance.put(hash, user.getAmount());
    }

    public synchronized int getCurrentAmount(String key) {return balance.get(key);}

    public synchronized int getCurrentAmount(User user) {
        String hash = user.getUsername() + "-" + user.getBank();
        return getCurrentAmount(hash);
    }

    // assuming, the banker detail exist, else NPE
    public synchronized void setAmount(String key, int add) {balance.put(key, balance.get(key) + add);}

    public synchronized boolean contains(User user) {
        return db.get(user.getBank()).contains(user.getUsername());
    }

    public void show(User user) {
        String key = user.getUsername() + "-" + user.getBank();
        System.out.println("Account : "+key+" amount : "+getCurrentAmount(key));
    }

    @Override
    public ResourceRequest getResourceType() {
        return ResourceRequest.DATABASE;
    }
}
