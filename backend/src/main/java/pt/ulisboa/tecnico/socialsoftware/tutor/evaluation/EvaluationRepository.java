package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
}
