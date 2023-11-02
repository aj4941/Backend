package swm_nm.morandi.domain.practice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.practice.dto.PracticeProblemInfo;
import swm_nm.morandi.domain.practice.dto.PracticeStartResponseDto;
import swm_nm.morandi.domain.practice.dto.PracticeResultDto;
import swm_nm.morandi.domain.practice.entity.PracticeProblem;
import swm_nm.morandi.domain.practice.repository.PracticeProblemRepository;
import swm_nm.morandi.domain.problem.entity.Problem;
import swm_nm.morandi.domain.problem.repository.ProblemRepository;
import swm_nm.morandi.domain.testDuring.dto.PracticeProblemCodeDto;
import swm_nm.morandi.domain.testDuring.service.TempCodeService;
import swm_nm.morandi.domain.testStart.service.TempCodeInitializer;
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

    private final PracticeCheckService practiceCheckService;

    private final TempCodeInitializer tempCodeInitializer;

    private final TempCodeService tempCodeService;

    private final ProblemRepository problemRepository;

    private final MemberRepository memberRepository;

    private final PracticeProblemRepository practiceProblemRepository;
    public PracticeStartResponseDto startPractices(Long bojProblemId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));

        PracticeProblemInfo practiceProblemInfo = practiceCheckService.getPracticeProblemInfo(bojProblemId);

        if (practiceProblemInfo != null) {
            return getPracticeStartResponseDto(practiceProblemInfo.getPracticeProblemId());
        }

        Problem problem = problemRepository.findProblemByBojProblemId(bojProblemId).orElseThrow(
                () -> new MorandiException(ProblemErrorCode.PROBLEM_NOT_FOUND));

        PracticeProblem practiceProblem = PracticeProblem.builder()
                .isSolved(false)
                .practiceDate(LocalDate.now())
                .problem(problem)
                .member(member)
                .build();

        PracticeProblem savedPracticeProblem = practiceProblemRepository.save(practiceProblem);

        tempCodeInitializer.initializePracticeTempCodeCache(savedPracticeProblem);

        return getPracticeStartResponseDto(savedPracticeProblem.getPracticeProblemId());
    }

    private PracticeStartResponseDto getPracticeStartResponseDto(Long practiceProblemId) {
        PracticeProblemCodeDto practiceProblemCodeDto = getPracticeProblemCodeDto(practiceProblemId);
        PracticeStartResponseDto practiceStartResponseDto = PracticeStartResponseDto.getPracticeStartResponseDto(practiceProblemId,
                practiceProblemCodeDto);
        return practiceStartResponseDto;
    }
    private PracticeProblemCodeDto getPracticeProblemCodeDto(Long practiceProblemId) {
        return tempCodeService.getPracticeProblemTempCode(practiceProblemId);
    }

    public PracticeResultDto practiceResult(Long practiceProblemId) {
        PracticeProblem practiceProblem = practiceProblemRepository.findById(practiceProblemId).orElseThrow(
                () -> new MorandiException(PracticeProblemErrorCode.PRACTICE_PROBLEM_NOT_FOUND));
        PracticeResultDto practiceResultDto = PracticeResultDto.getPracticeResultDto(practiceProblem);
        return practiceResultDto;
    }
}
