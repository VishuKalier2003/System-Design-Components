package transactions.compensating.config;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import transactions.compensating.database.Database;
import transactions.compensating.enums.Handlers;
import transactions.compensating.enums.ResourceRequest;
import transactions.compensating.model.Resource;
import transactions.compensating.service.LockCache;
import transactions.compensating.service.TransactionQuotas;

@Configuration
public class ResourceMapper {

    @Autowired private Database db;
    @Autowired private LockCache lc;
    @Autowired private TransactionQuotas q;

    @Bean(name="pool")
    public Map<ResourceRequest, Resource> mapper() {
        Map<ResourceRequest, Resource> mapper = new EnumMap<>(ResourceRequest.class);
        mapper.put(ResourceRequest.DATABASE, db);
        mapper.put(ResourceRequest.LOCKER, lc);
        mapper.put(ResourceRequest.QUOTAS, q);
        return mapper;
    }

    @Bean(name="threads")
    public Map<Handlers, ExecutorService> map() {
        Map<Handlers, ExecutorService> map = new EnumMap<>(Handlers.class);
        map.put(Handlers.CHECK, Executors.newFixedThreadPool(3));
        map.put(Handlers.SENDER_LOCK, Executors.newFixedThreadPool(4));
        map.put(Handlers.RECEIVER_LOCK, Executors.newFixedThreadPool(4));
        map.put(Handlers.SENDER_MONEY, Executors.newFixedThreadPool(3));
        map.put(Handlers.RECEIVER_MONEY, Executors.newFixedThreadPool(2));
        map.put(Handlers.QUOTA, Executors.newFixedThreadPool(2));
        map.put(Handlers.DONE, Executors.newFixedThreadPool(2));
        return map;
    }
}
