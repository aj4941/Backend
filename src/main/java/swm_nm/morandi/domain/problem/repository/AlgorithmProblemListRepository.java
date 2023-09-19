package swm_nm.morandi.domain.problem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.problem.entity.AlgorithmProblemList;

import java.util.List;

public interface AlgorithmProblemListRepository extends JpaRepository<AlgorithmProblemList, Long> {
    List<AlgorithmProblemList> findByProblem_ProblemId(Long problemId);
}
