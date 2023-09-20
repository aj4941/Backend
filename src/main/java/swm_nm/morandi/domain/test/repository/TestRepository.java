package swm_nm.morandi.domain.test.repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.test.entity.Tests;

import java.util.List;
public interface TestRepository extends JpaRepository<Tests, Long> {
    List<Tests> findAllByMember_MemberId(Long memberId);
    List<Tests> findAllByMember_MemberIdOrderByTestDateDesc(Long memberId, Pageable pageable);
}
