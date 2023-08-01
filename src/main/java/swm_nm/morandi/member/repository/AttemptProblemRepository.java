package swm_nm.morandi.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.testResult.entity.AttemptProblem;

import java.util.List;
import java.util.Optional;

public interface AttemptProblemRepository extends JpaRepository<AttemptProblem, Long> {
    List<AttemptProblem> findAllByMember_MemberId(Long memberId);
    List<AttemptProblem> findAllByTest_TestId(Long testId);
    List<AttemptProblem> findAttemptProblemsByTest_TestId(Long testId);
}


