package swm_nm.morandi.problem.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.problem.dto.DifficultyLevel;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long problemId;
    private Long bojProblemId;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel problemDifficulty;
}
