package swm_nm.morandi.domain.problem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.domain.common.BaseEntity;
import swm_nm.morandi.domain.testInfo.entity.TestType;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeProblemList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeProblemListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEST_TYPE_ID")
    private TestType testType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROBLEM_ID")
    private Problem problem;
}
