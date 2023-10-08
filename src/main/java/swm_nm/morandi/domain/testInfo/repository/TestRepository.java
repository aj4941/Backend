package swm_nm.morandi.domain.testInfo.repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testInfo.entity.Tests;

import java.time.LocalDateTime;
import java.util.List;
public interface TestRepository extends JpaRepository<Tests, Long> {
    //Paging하여 테스트 기록을 가져옴
    List<Tests> findAllTestsByMember_MemberIdAndTestStatus(Long memberId, TestStatus testStatus,Pageable pageable);
    //1년동안의 테스트 기록을 가져와서 레이팅 반환에 사용함
    List<Tests> findAllTestsByMember_MemberIdAndTestStatusAndTestDateAfterOrderByTestDateDesc(Long memberId, TestStatus testStatus, LocalDateTime oneYearAgo);
}
