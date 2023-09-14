package swm_nm.morandi.domain.test.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.test.entity.TestType;

public interface TestTypeRepository extends JpaRepository<TestType, Long> {

}
