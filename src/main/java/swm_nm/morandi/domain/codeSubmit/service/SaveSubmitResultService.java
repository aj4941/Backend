package swm_nm.morandi.domain.codeSubmit.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.codeSubmit.dto.AttemptProblemResultDto;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.AttemptProblemErrorCode;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.time.Duration;
import java.time.LocalDateTime;

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

        Tests test = attemptProblem.getTest();

        //테스트 시작시간에서 현재 시간 차이를 구해서 저장
        Duration duration = Duration.between(test.getTestDate(), LocalDateTime.now());

        Long minutes = duration.toMinutes();
        if (minutes <= test.getTestTime()) {
            attemptProblem.setIsSolved(true);
            attemptProblem.setExecutionTime(minutes);
        }

        attemptProblemRepository.save(attemptProblem);

        return "success";

    }

}
