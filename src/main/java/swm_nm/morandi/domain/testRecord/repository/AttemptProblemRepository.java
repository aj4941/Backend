package swm_nm.morandi.domain.testRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;

import java.time.LocalDateTime;
import java.util.List;

public interface AttemptProblemRepository extends JpaRepository<AttemptProblem, Long> {
    List<AttemptProblem> findAllByMember_MemberId(Long memberId);
    List<AttemptProblem> findAllByTest_TestId(Long testId);
    List<AttemptProblem> findAllByTestOrderByAttemptProblemIdAsc(Tests test);
    List<AttemptProblem> findAttemptProblemsByTest_TestId(Long testId);

    //1년동안의 테스트 기록 히트맵 데이터를 가져온다.
    // select test_date, count(*)
    // from attempt_problem where test_id = (select test_id from tests t left join t.member m where t.member_id = m.member_id and between 1 year ago and now() and t.test_status = 'COMPLETED' order by t.test_date desc )
    // is_solved = true
    // group by test_date;
    @Query("SELECT a.testDate, count(a.testDate) " +
            "FROM AttemptProblem a " +
            "LEFT JOIN a.test t " +
            "WHERE t.testId IN " +
                "(SELECT DISTINCT t.testId " +
                    "FROM Tests t LEFT JOIN t.member m " +
                    "WHERE m.memberId = :memberId " +
                    "AND (t.testDate BETWEEN :oneYearAgo AND CURRENT_TIMESTAMP) " +
                    "AND t.testStatus = 'COMPLETED') " +
            "AND a.isSolved = true " +
            "GROUP BY a.testDate " +
            "ORDER BY a.testDate DESC")
    List<Object[]> getHeatMapDataSinceOneYear(@Param("memberId") Long memberId, @Param("oneYearAgo") LocalDateTime oneYearAgo);


    //List<AttemptProblem> findAttemptProblemsByTest_TestIdOrderByAttemptProblemIdAsc(Long testId);
}


