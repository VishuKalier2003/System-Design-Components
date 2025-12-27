package ladder.pager_duty.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ladder.pager_duty.store.Incident;

public interface IncidentRepo extends JpaRepository<Incident, String> {

}
