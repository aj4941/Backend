package swm_nm.morandi.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.Test;

import java.util.List;
public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findAllByMember_MemberId(Long memberId);
}
