package swm_nm.morandi.domain.test.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.domain.common.BaseEntity;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.test.dto.TestStatus;
import swm_nm.morandi.domain.problem.dto.DifficultyLevel;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Test extends BaseEntity {
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
    private Long testRating;

    @Enumerated(EnumType.STRING)
    private TestStatus testStatus;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

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
