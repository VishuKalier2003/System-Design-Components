package ladder.pager_duty.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ladder.pager_duty.store.OperationLog;

public interface OperationLogRepo extends JpaRepository<OperationLog, String> {

}
