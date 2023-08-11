package swm_nm.morandi.problem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.problem.entity.Algorithm;

public interface AlgorithmRepository extends JpaRepository<Algorithm, Long> {
}
