package iam.aws.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import iam.aws.enums.OperationType;
import iam.aws.enums.ScopeName;
import iam.aws.repo.ScopeRepo;
import iam.aws.store.Scope;

@Component
public class ScopeLoader implements CommandLineRunner {
    @Autowired private ScopeRepo scopeRepo;

    @Override
    public void run(String... args) {
        if(scopeRepo.count() > 0)
            return;
        Scope s1 = new Scope("read", ScopeName.EMP_READ, OperationType.READ);
        s1.setColumns(List.of("empID", "name", "address", "age", "bills", "paid", "pending", "items"));
        Scope s2 = new Scope("name-read", ScopeName.NAME_READ, OperationType.READ);
        s2.setColumns(List.of("name"));
        Scope s3 = new Scope("name-edit", ScopeName.NAME_EDIT, OperationType.EDIT);
        s3.setColumns(List.of("name"));
        Scope s4 = new Scope("personal-read", ScopeName.PERSONAL_READ, OperationType.READ);
        s4.setColumns(List.of("name", "age", "address"));
        Scope s5 = new Scope("personal-edit", ScopeName.PERSONAL_EDIT, OperationType.EDIT);
        s5.setColumns(List.of("name", "age", "address"));
        Scope s6 = new Scope("paid-edit", ScopeName.INVEST_PAID_EDIT, OperationType.EDIT);
        s6.setColumns(List.of("bills", "paid"));
        Scope s7 = new Scope("pending-edit", ScopeName.INVEST_PENDING_EDIT, OperationType.EDIT);
        s7.setColumns(List.of("bills", "pending"));
        Scope s8 = new Scope("purchase-edit", ScopeName.INVEST_PURCHASE_EDIT, OperationType.EDIT);
        s8.setColumns(List.of("purchase", "bills"));
        Scope s9 = new Scope("bills-read", ScopeName.BILLS_READ, OperationType.READ);
        s9.setColumns(List.of("bills", "paid", "pending", "purchase"));
        scopeRepo.saveAll(List.of(s1, s2, s3, s4, s5, s6, s7, s8, s9));
    }
}
