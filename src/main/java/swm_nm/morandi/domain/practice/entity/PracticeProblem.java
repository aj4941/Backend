package swm_nm.morandi.domain.practice.entity;

import lombok.*;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.practice.dto.PracticeStatus;
import swm_nm.morandi.domain.problem.entity.Problem;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PracticeProblem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long practiceProblemId;

    private LocalDateTime practiceDate;

    @Enumerated(EnumType.STRING)
    private PracticeStatus practiceStatus;

    private Long solvedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROBLEM_ID")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
