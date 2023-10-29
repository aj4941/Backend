package swm_nm.morandi.domain.testRecord.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttemptProblemRepository extends JpaRepository<AttemptProblem, Long> {
    List<AttemptProblem> findAllByMember_MemberId(Long memberId);
    List<AttemptProblem> findAllByTest_TestId(Long testId);
    List<AttemptProblem> findAllByTestOrderByAttemptProblemIdAsc(Tests test);


    @EntityGraph(attributePaths = {"test", "problem"})
    Optional<AttemptProblem> findByTest_TestIdAndProblem_BojProblemId(Long testId, Long bojProblemId);
    @EntityGraph(attributePaths = {"test", "problem"})
    Optional<AttemptProblem> findByMember_MemberIdAndTest_testIdAndTest_TestStatusAndProblem_BojProblemId(Long memberId, Long testId, TestStatus testStatus, Long bojProblemId);
    List<AttemptProblem> findAttemptProblemsByTest_TestId(Long testId);

    //1년동안의 테스트 기록 히트맵 데이터를 가져온다.
    // select test_date, count(*)
    // from attempt_problem where test_id = (select test_id from tests t left join t.member m where t.member_id = m.member_id and between 1 year ago and now() and t.test_status = 'COMPLETED' order by t.test_date desc )
    // is_solved = true
    // group by test_date;
//    @Query("SELECT a.testDate, count(a.testDate) " +
//            "FROM AttemptProblem a " +
//            "WHERE a.test.testId IN " +
//                "(SELECT DISTINCT t.testId " +
//                    "FROM Tests t " +
//                    "WHERE t.member.memberId = :memberId " +
//                    "AND (t.testDate BETWEEN :oneYearAgo AND CURRENT_TIMESTAMP) " +
//                    "AND t.testStatus = 'COMPLETED') " +
//            "AND a.isSolved = true " +
//            "GROUP BY a.testDate " +
//            "ORDER BY a.testDate DESC")

    List<AttemptProblem> findAllAttemptProblemsByMember_MemberIdAndAndTestDateAfterAndIsSolved(Long memberId, LocalDate oneYearAgo, boolean isSolved);

    @Query("SELECT a.algorithmName, " +
                "COUNT(ap.problem) as totalAttempts, " +
                "SUM(CASE WHEN ap.isSolved = true THEN 1 ELSE 0 END) as solvedAttempts " +
            "FROM AlgorithmProblemList apl " +
            "JOIN apl.algorithm a " +
            "JOIN AttemptProblem AS ap ON apl.problem.problemId = ap.problem.problemId " +
            "WHERE ap.member.memberId = :memberId " +
            "GROUP BY a.algorithmName")
    List<Object[]> getAttemptStatisticsCollectByAlgorithm(@Param("memberId") Long memberId);

    //N+1문제 발생하여 entity graph로
    @EntityGraph(attributePaths = {"test", "problem", "member"})
    List<AttemptProblem> findTestDetailsByTest_TestId(Long testId);
}



