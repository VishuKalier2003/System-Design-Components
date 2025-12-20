package iam.aws.store;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor      // fixed: a No-args constructor is definitely required
@Table(name="employee")
public class Employee {
    @Id @Column(name="empID")
    private String empID;

    // fixed: use nullable to ensure SQL syntax does not mark it as null
    @Column(nullable=false)
    @NonNull private String name;
    @Column(nullable=false)
    @NonNull private String address;
    private int age;
    // fixed: instead of floats or doubles use Bigdecimal
    private BigDecimal bills, paid, pending;    // fixed: ensure that the name of any property is not SQL keyword
    @Column(columnDefinition="TEXT")
    private String items;

    public Employee(String Id, String name, String address, int age, double bills) {
        this.empID = Id;
        this.name = name;
        this.address = address;
        this.age = age;
        this.bills = this.pending = BigDecimal.valueOf(bills);
    }

    public void setItems(List<String> items) {
        this.items = String.join(",", items);
    }
}
