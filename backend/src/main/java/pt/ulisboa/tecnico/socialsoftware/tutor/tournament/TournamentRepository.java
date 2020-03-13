package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.domain.Tournament;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {

	@Query(value = "SELECT * FROM tournaments t, course_executions ce WHERE ce.id = t.course_execution_id AND ce.id = :executionId", nativeQuery = true)
	List<Tournament> findTournaments(int executionId);

	@Query(value = "SELECT MAX(key) FROM tournaments", nativeQuery = true)
	Integer getMaxTournamentKey();

	@Query(value = "SELECT * FROM tournaments t WHERE t.key = :key", nativeQuery = true)
	Optional<Tournament> findTournaments(Integer key);
}
