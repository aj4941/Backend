package swm_nm.morandi.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.testResult.entity.AttemptProblem;

import java.util.List;

public interface AttemptProblemRepository extends JpaRepository<AttemptProblem, Long> {
    List<AttemptProblem> findAllByMember_MemberId(Long memberId);
    List<AttemptProblem> findAllByTest_TestId(Long testId);
}
