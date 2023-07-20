package swm_nm.morandi.domain;

import com.fasterxml.jackson.core.base.GeneratorBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
