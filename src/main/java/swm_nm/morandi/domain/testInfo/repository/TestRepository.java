package swm_nm.morandi.domain.testInfo.repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm_nm.morandi.domain.testInfo.entity.Tests;

import java.time.LocalDateTime;
import java.util.List;
public interface TestRepository extends JpaRepository<Tests, Long> {

    @Query("SELECT t FROM Tests t " +
            "WHERE t.member.memberId = :memberId " +
            "AND t.testStatus = 'COMPLETED'" +
            " ORDER BY t.testDate DESC")
    List<Tests> findDataRecent4(Long memberId, Pageable pageable);

    //1년동안의 테스트 기록을 가져온다.
    //이 때 테스트 날짜와 테스트 점수만 최근 날짜부터 가져온다.
    @Query("SELECT t.testDate, t.testRating, t.testTypename " +
            "FROM Tests t " +
            "WHERE t.member.memberId = :memberId " +
            "AND (t.testDate BETWEEN :oneYearAgo AND CURRENT_TIMESTAMP )" +
            "AND t.testStatus = 'COMPLETED' " +
            "ORDER BY t.testDate DESC")
    List<Object[]> getRatingGraphSinceOneYear(@Param("memberId") Long memberId, @Param("oneYearAgo") LocalDateTime oneYearAgo);
}
