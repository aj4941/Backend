package swm_nm.morandi.domain.practice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.practice.dto.PracticeIdDto;
import swm_nm.morandi.domain.practice.dto.PracticeResultDto;
import swm_nm.morandi.domain.practice.entity.PracticeProblem;
import swm_nm.morandi.domain.practice.repository.PracticeProblemRepository;
import swm_nm.morandi.domain.problem.entity.Problem;
import swm_nm.morandi.domain.problem.repository.ProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.MemberErrorCode;
import swm_nm.morandi.global.exception.errorcode.PracticeProblemErrorCode;
import swm_nm.morandi.global.exception.errorcode.ProblemErrorCode;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PracticeProblemService {

    private final ProblemRepository problemRepository;

    private final MemberRepository memberRepository;

    private final PracticeProblemRepository practiceProblemRepository;

    public PracticeIdDto savePraticeProblems(Long bojProblemId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));

        Optional<PracticeProblem> optionalPracticeProblem = practiceProblemRepository.
                findByMember_MemberIdAndProblem_BojProblemId(memberId, bojProblemId);

        if (optionalPracticeProblem.isPresent()) {
            PracticeProblem practiceProblem = optionalPracticeProblem.get();
            return PracticeIdDto.getPracticeIdDto(practiceProblem.getPracticeProblemId());
        }

        Problem problem = problemRepository.findProblemByBojProblemId(bojProblemId).orElseThrow(
                () -> new MorandiException(ProblemErrorCode.PROBLEM_NOT_FOUND));

        PracticeProblem addPracticeProblem = PracticeProblem.builder()
                .isSolved(false)
                .practiceDate(LocalDate.now())
                .problem(problem)
                .member(member)
                .build();

        PracticeProblem savedPracticeProblem = practiceProblemRepository.save(addPracticeProblem);
        return PracticeIdDto.getPracticeIdDto(savedPracticeProblem.getPracticeProblemId());
    }

    public PracticeResultDto practiceResult(Long practiceProblemId) {
        PracticeProblem practiceProblem = practiceProblemRepository.findById(practiceProblemId).orElseThrow(
                () -> new MorandiException(PracticeProblemErrorCode.PRACTICE_PROBLEM_NOT_FOUND));
        PracticeResultDto practiceResultDto = PracticeResultDto.getPracticeResultDto(practiceProblem);
        return practiceResultDto;
    }
}
