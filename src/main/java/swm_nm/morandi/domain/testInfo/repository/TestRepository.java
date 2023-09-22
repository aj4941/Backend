package swm_nm.morandi.domain.testInfo.repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.testInfo.entity.Tests;

import java.util.List;
public interface TestRepository extends JpaRepository<Tests, Long> {
    List<Tests> findAllByMember_MemberId(Long memberId);
    List<Tests> findAllByMember_MemberIdOrderByTestDateDesc(Long memberId, Pageable pageable);
}
