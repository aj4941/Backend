package swm_nm.morandi.problem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.problem.entity.Problem;
import swm_nm.morandi.test.entity.Test;
import swm_nm.morandi.test.entity.TestType;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Optional<Problem> findProblemByBojProblemId(Long bojProblemId);
}
