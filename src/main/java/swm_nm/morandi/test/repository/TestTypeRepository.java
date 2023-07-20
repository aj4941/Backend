package swm_nm.morandi.test.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.test.domain.TestType;

public interface TestTypeRepository extends JpaRepository<TestType, Long> {

}
