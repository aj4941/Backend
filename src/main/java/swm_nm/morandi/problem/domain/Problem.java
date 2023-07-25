package swm_nm.morandi.problem.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.problem.dto.DifficultyLevel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Problem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long problemId;
    private Long bojProblemId;
    private DifficultyLevel problemDifficulty;
}
