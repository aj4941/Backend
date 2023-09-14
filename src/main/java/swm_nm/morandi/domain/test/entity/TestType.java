package swm_nm.morandi.domain.test.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.domain.common.BaseEntity;
import swm_nm.morandi.domain.problem.dto.DifficultyLevel;
import swm_nm.morandi.domain.problem.dto.DifficultyRange;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testTypeId;
    private String testTypename;
    private Long testTime;
    private Integer problemCount;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel startDifficulty;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel endDifficulty;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<DifficultyRange> difficultyRanges;

    private Long averageCorrectAnswerRate;

    private Integer numberOfTestTrial;


    public void updateAverageCorrectAnswerRate(Long newTrialCorrectAnswerRate){
        this.averageCorrectAnswerRate =
                (((this.averageCorrectAnswerRate * this.numberOfTestTrial) + newTrialCorrectAnswerRate)
                        /(++this.numberOfTestTrial));

    }
}
