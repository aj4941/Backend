package swm_nm.morandi.problem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.problem.domain.Problem;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

}
