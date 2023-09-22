package swm_nm.morandi.domain.testInfo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.testInfo.entity.TestType;

import java.util.Optional;

public interface TestTypeRepository extends JpaRepository<TestType, Long> {
    Optional<TestType> findTestTypeByTestTypename(String testTypename);
}
