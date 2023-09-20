package swm_nm.morandi.domain.testInfo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.testExit.entity.TestType;

public interface TestTypeRepository extends JpaRepository<TestType, Long> {

}
