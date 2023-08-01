package swm_nm.morandi.problem.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.problem.domain.Problem;
import swm_nm.morandi.test.domain.TestType;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeProblemList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeProblemListId;

    @ManyToOne
    @JoinColumn(name = "TEST_TYPE_ID")
    private TestType testType;

    @ManyToOne
    @JoinColumn(name = "PROBLEM_ID")
    private Problem problem;
}
