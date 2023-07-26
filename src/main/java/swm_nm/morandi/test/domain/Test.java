package swm_nm.morandi.test.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.problem.dto.DifficultyLevel;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;
    private LocalDateTime testDate;
    private Integer testTime;
    private Integer problemCount;
    private DifficultyLevel startDifficulty;
    private DifficultyLevel endDifficulty;
    private String testTypename;
    private Long testRating;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
