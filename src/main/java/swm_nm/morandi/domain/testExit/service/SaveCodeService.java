package swm_nm.morandi.domain.testExit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testExit.dto.AttemptCodeDto;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.AttemptProblemErrorCode;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaveCodeService {

    private final AttemptProblemRepository attemptProblemRepository;

    public void saveEachCodeinAttemptProblems(AttemptCodeDto attemptCodeDto) {
        List<AttemptProblem> attemptProblems =
                attemptProblemRepository.findAttemptProblemsByTest_TestId(attemptCodeDto.getTestId());
        if(attemptProblems.isEmpty()) {
            log.error("attemptCodeDto TestId = {}, ", attemptCodeDto.getTestId());
            throw new MorandiException(AttemptProblemErrorCode.ATTEMPT_PROBLEM_NOT_FOUND);
        }
        attemptProblems.forEach(attemptProblem -> {
            Long attemptProblemId = attemptProblem.getAttemptProblemId();
            attemptProblem.setSubmitCode(
                    Optional.of(attemptCodeDto.getSubmitCode().get(attemptProblemId))
                            .orElseThrow(() -> {
                                log.error("attemptProblem = {}, attemptCodeDto = {}", attemptProblem, attemptCodeDto);
                                return new MorandiException(AttemptProblemErrorCode.ATTEMPT_PROBLEM_NOT_FOUND);
                            }));


        });
        attemptProblemRepository.saveAll(attemptProblems);
        log.info("테스트 Id에 해당하는 푼 문제들의 코드를 모두 저장했습니다: {}", attemptCodeDto.getTestId());

    }
}
