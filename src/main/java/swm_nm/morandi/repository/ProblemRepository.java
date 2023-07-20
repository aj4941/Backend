package swm_nm.morandi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.Problem;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

}
