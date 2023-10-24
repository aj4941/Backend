package swm_nm.morandi.domain.codeSubmit.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.codeSubmit.dto.AttemptProblemResultDto;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.AttemptProblemErrorCode;
import swm_nm.morandi.global.utils.SecurityUtils;

@Service
@RequiredArgsConstructor
public class SaveSubmitResultService {
    private final AttemptProblemRepository attemptProblemRepository;

    @Transactional
    public String saveSubmitResult(AttemptProblemResultDto attemptProblemResultDto) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        AttemptProblem attemptProblem =
                attemptProblemRepository.findByMember_MemberIdAndTest_testIdAndTest_TestStatusAndProblem_BojProblemId(memberId, attemptProblemResultDto.getTestId(), TestStatus.IN_PROGRESS,attemptProblemResultDto.getBojProblemId())
                .orElseThrow(() -> new MorandiException(AttemptProblemErrorCode.ATTEMPT_PROBLEM_NOT_FOUND_DURING_TEST));

        if(attemptProblem.getIsSolved())
            throw new MorandiException(AttemptProblemErrorCode.ATTEMPT_PROBLEM_ALREADY_SOLVED);
        attemptProblem.setIsSolved(attemptProblemResultDto.getIsSolved());
        attemptProblem.setExecutionTime(attemptProblemResultDto.getExecutionTime());

        attemptProblemRepository.save(attemptProblem);

        return "success";

    }

}
