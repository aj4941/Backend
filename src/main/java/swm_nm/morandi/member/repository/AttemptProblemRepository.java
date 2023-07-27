package swm_nm.morandi.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.testResult.entity.AttemptProblem;

import java.util.List;
import java.util.Optional;

public interface AttemptProblemRepository extends JpaRepository<AttemptProblem, Long> {
    Optional<List<AttemptProblem>> findAllByMember_MemberId(Long memberId);
    Optional<List<AttemptProblem>> findAllByTest_TestId(Long testId);
    Optional<List<AttemptProblem>> findAttemptProblemsByTest_TestId(Long testId);
}


