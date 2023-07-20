package swm_nm.morandi.problem.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmProblemList {

    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long algorithmProblemListId;

    @ManyToOne
    @JoinColumn(name = "PROBLEM_ID")
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "ALGORITHM_ID")
    private Algorithm algorithm;
}
