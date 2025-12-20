package iam.aws.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import iam.aws.store.Scope;

public interface ScopeRepo extends JpaRepository<Scope, String> {
}
