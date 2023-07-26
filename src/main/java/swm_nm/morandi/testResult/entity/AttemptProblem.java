package swm_nm.morandi.testResult.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.problem.domain.Problem;
import swm_nm.morandi.test.domain.Test;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptProblem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptProblemId;

    private Boolean isSolved;

    private LocalDate testDate;

    private Integer solvedTime;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "TEST_ID")
    private Test test;

    @ManyToOne
    @JoinColumn(name = "PROBLEM_ID")
    private Problem problem;
}
