package swm_nm.morandi.problem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.common.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlgorithmProblemList extends BaseEntity {

    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long algorithmProblemListId;

    @ManyToOne
    @JoinColumn(name = "PROBLEM_ID")
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "ALGORITHM_ID")
    private Algorithm algorithm;
}
