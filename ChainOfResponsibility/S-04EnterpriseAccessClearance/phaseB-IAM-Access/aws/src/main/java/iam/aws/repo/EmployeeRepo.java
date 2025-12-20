package iam.aws.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import iam.aws.store.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, String> {

}
