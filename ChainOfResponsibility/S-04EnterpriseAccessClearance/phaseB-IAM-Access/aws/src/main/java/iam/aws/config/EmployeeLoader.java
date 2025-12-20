package iam.aws.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import iam.aws.repo.EmployeeRepo;
import iam.aws.store.Employee;

@Component
public class EmployeeLoader implements CommandLineRunner {
    private final EmployeeRepo repo;

    public EmployeeLoader(EmployeeRepo ep) {
        this.repo = ep;
    }

    @Override public void run(String... args) {
        if(repo.count() > 0)
            return;
        Employee e1 = new Employee("emp1", "Vishu", "Meerut, U.P.", 22, 27800);
        e1.setItems(List.of("Sugar", "Coffee", "Milk"));
        Employee e2 = new Employee("emp2", "Aaryan", "Bangalore, Karnataka", 23, 25931);
        e2.setItems(List.of("Rice", "Flour"));
        Employee e3 = new Employee("emp3", "Deepa", "Hyderabad, Telangana", 17, 2500);
        e3.setItems(List.of("Fish", "Steak", "Meat"));
        repo.saveAll(List.of(e1, e2, e3));
    }
}
