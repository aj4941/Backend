package swm_nm.morandi.test.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Test {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;
    private LocalDateTime testDate;
    private Long testTime;
    private Integer problemCount;
    private Integer startDifficulty;
    private Integer endDifficulty;
    private String testTypename;
    private Long testRating;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
