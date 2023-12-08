package swm_nm.morandi.domain.problem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.problem.entity.Problem;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Optional<Problem> findProblemByBojProblemId(Long bojProblemId);
    List<Problem> findAllByBojProblemIdIn(List<Long> bojProblemIdList);

}
