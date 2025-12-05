package graph.task.config;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import graph.task.enums.ResourceRequest;
import graph.task.resources.FlagResource;
import graph.task.resources.QuotaResource;
import graph.task.resources.TokenResource;

// detail: Configuration class loaded at compile time
@Configuration
public class ResourceProvider {

    @Autowired private QuotaResource qr;
    @Autowired private TokenResource tr;
    @Autowired private FlagResource fr;

    @Bean("resource_provider")
    public Map<ResourceRequest, Supplier<Object>> map() {
        Map<ResourceRequest, Supplier<Object>> mp = new EnumMap<>(ResourceRequest.class);
        // technique: Supplier Injection - produces a supplier, hence pluggable
        mp.put(ResourceRequest.QUOTA, () -> qr.provide());
        mp.put(ResourceRequest.TOKEN, () -> tr.provide());
        mp.put(ResourceRequest.FLAG, () -> fr.provide());
        return mp;
    }
}
