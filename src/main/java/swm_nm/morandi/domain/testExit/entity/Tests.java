package swm_nm.morandi.domain.testExit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.domain.common.BaseEntity;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.problem.dto.DifficultyLevel;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tests extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;
    private LocalDateTime testDate; // 테스트 시작 시간
    private Long testTime; // minutes
    private Integer problemCount;
    private Long remainingTime; // seconds

    @Enumerated(EnumType.STRING)
    private DifficultyLevel startDifficulty;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel endDifficulty;

    private String testTypename;
    private Long testRating; // 사용자 레이팅에 반영된 결과 레이팅
    private Long originRating; // 순수 테스트 레이팅

    @Enumerated(EnumType.STRING)
    private TestStatus testStatus;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setOriginRating(Long originRating) {
        this.originRating = originRating;
    }
    public void setTestRating(Long testRating) {
        this.testRating = testRating;
    }
    public void setTestStatus(TestStatus testStatus) {
        this.testStatus = testStatus;
    }
    public void setRemainingTime(Long remainingTime) {
        this.remainingTime = remainingTime;
    }
}
