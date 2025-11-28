package filesystem.virtual.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import filesystem.virtual.Enum.AdapterTypes;
import filesystem.virtual.core.DataStoreAdapter;
import filesystem.virtual.core.DatabaseAdapter;

@Configuration
public class AdapterConfig {

    @Autowired private DatabaseAdapter dbAdapter;
    @Autowired private DataStoreAdapter dsAdapter;

    @Bean("adapter_config")
    public Map<AdapterTypes, StringBuilder> createMap() {
        Map<AdapterTypes, StringBuilder> mp = new LinkedHashMap<>();
        mp.put(dbAdapter.getTYPE(), new StringBuilder());
        mp.put(dsAdapter.getTYPE(), new StringBuilder());
        return mp;
    }
}
