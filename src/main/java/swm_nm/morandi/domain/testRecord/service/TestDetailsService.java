package swm_nm.morandi.domain.testRecord.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testRecord.dto.TestRecordDto;
import swm_nm.morandi.domain.testExit.entity.AttemptProblem;
import swm_nm.morandi.domain.testExit.entity.Tests;
import swm_nm.morandi.domain.testExit.dto.AttemptProblemDto;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.domain.testRecord.mapper.TestRecordMapper;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestDetailsService {

    private final TestRepository testRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    public TestRecordDto getTestRecordDtoByTestId(Long testId) {
        Tests test = testRepository.findById(testId).orElseThrow(()-> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
        TestRecordDto testRecordDto = TestRecordMapper.convertToDto(test);
        List<AttemptProblem> attemptProblems
                = attemptProblemRepository.findAllByTest_TestId(testId);
        if (!attemptProblems.isEmpty()) {
            long index = 1;
            for (AttemptProblem attemptProblem : attemptProblems) {
                AttemptProblemDto attemptProblemDto =
                        AttemptProblemDto.builder()
                                .testProblemId(index++)
                                .bojProblemId(attemptProblem.getProblem().getBojProblemId())
                                .isSolved(attemptProblem.getIsSolved())
                                .executionTime(attemptProblem.getExecutionTime())
                                .build();
                testRecordDto.getAttemptProblemDto().add(attemptProblemDto);
            }
        }
        return testRecordDto;
    }
}
