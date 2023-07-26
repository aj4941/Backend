package swm_nm.morandi.problem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.problem.domain.TypeProblemList;

import java.util.List;

public interface TypeProblemListRepository extends JpaRepository<TypeProblemList, Long> {
    List<TypeProblemList> findByTestType_TestTypeId(Long testTypeId);
}
