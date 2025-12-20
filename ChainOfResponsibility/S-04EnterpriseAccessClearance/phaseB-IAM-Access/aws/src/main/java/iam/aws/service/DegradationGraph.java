package iam.aws.service;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import iam.aws.enums.ScopeName;
import jakarta.annotation.PostConstruct;

@Service
public class DegradationGraph {
    private final Map<ScopeName, ScopeName> scopes = new EnumMap<>(ScopeName.class);

    @PostConstruct
    public void init() {
        scopes.put(ScopeName.EMP_READ, null);
        scopes.put(ScopeName.NAME_READ, ScopeName.EMP_READ);
        scopes.put(ScopeName.BILLS_READ, null);
        scopes.put(ScopeName.PERSONAL_READ, ScopeName.NAME_READ);
        scopes.put(ScopeName.NAME_EDIT, ScopeName.NAME_READ);
        scopes.put(ScopeName.PERSONAL_EDIT, ScopeName.PERSONAL_READ);
        scopes.put(ScopeName.INVEST_PAID_EDIT, ScopeName.BILLS_READ);
        scopes.put(ScopeName.INVEST_PENDING_EDIT, ScopeName.BILLS_READ);
        scopes.put(ScopeName.INVEST_PURCHASE_EDIT, ScopeName.BILLS_READ);
    }

    public boolean isLeaf(ScopeName name) {
        return scopes.get(name) == null;
    }

    // detail: done when degradation is possible
    public ScopeName degrade(ScopeName name) {
        return scopes.get(name);
    }
}
