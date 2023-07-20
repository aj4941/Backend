package swm_nm.morandi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.Algorithm;

import java.util.List;

public interface AlgorithmRepository extends JpaRepository<Algorithm, Long> {
}
