package swm_nm.morandi.domain.problem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.domain.common.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlgorithmProblemList extends BaseEntity {

    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long algorithmProblemListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROBLEM_ID")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALGORITHM_ID")
    private Algorithm algorithm;
}
