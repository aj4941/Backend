package swm_nm.morandi.testResult.entity;

import lombok.*;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.problem.domain.Problem;
import swm_nm.morandi.test.domain.Test;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptProblem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptProblemId;

    private Boolean isSolved;

    private LocalDate testDate;

    private Long executionTime;

    private String submitCode;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "TEST_ID")
    private Test test;

    @ManyToOne
    @JoinColumn(name = "PROBLEM_ID")
    private Problem problem;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AttemptProblem{");
        sb.append("attemptProblemId=").append(attemptProblemId);
        sb.append(", isSolved=").append(isSolved);
        sb.append(", testDate=").append(testDate);
        sb.append(", executionTime=").append(executionTime);
        sb.append(", submitCode='").append(submitCode).append('\'');
        sb.append(", member=").append(member);
        sb.append(", test=").append(test);
        sb.append(", problem=").append(problem);
        sb.append('}');
        return sb.toString();
    }
}
