package swm_nm.morandi.domain.testRecord.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testRecord.dto.TestRecordDto;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testExit.dto.AttemptProblemDto;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.domain.testRecord.mapper.TestRecordMapper;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestDetailsService {

    private final TestRepository testRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    public TestRecordDto getTestRecordDtoByTestId(Long testId) {
        //TODO
        //여기를 DB두번접근하지말고
        // attemptProblemList에서 testId가 뭐 X인
        // AttemptProblemList를 가져오도록 쿼리 짜면 DB 한 번으로 개선될 듯?

        Tests test = testRepository.findById(testId).orElseThrow(()-> new MorandiException(TestErrorCode.TEST_NOT_FOUND));
        List<AttemptProblem> attemptProblems
                = attemptProblemRepository.findAllByTest_TestId(testId);
        List<AttemptProblemDto> attemptProblemDtos = new ArrayList<>();
        if (!attemptProblems.isEmpty()) {
            long index = 1;
            for (AttemptProblem attemptProblem : attemptProblems) {
                AttemptProblemDto attemptProblemDto = AttemptProblemDto.getAttemptProblemDto(attemptProblem);
                attemptProblemDto.setTestProblemId(index++);
                attemptProblemDtos.add(attemptProblemDto);
            }
        }
        TestRecordDto testRecordDto = TestRecordMapper.convertToDto(test, attemptProblemDtos);
        return testRecordDto;
    }
}
