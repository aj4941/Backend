package swm_nm.morandi.domain.testRecord.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.testRecord.dto.TestRecordDto;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testExit.dto.AttemptProblemDto;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.domain.testRecord.mapper.TestRecordMapper;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.AttemptProblemErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestDetailsService {

    private final AttemptProblemRepository attemptProblemRepository;

    @Transactional
    public TestRecordDto getTestRecordDtoByTestId(Long testId) {
        //N+1문제 발생하여 fetch join으로 해결
        List<AttemptProblem> attemptProblems= attemptProblemRepository.findTestDetailsByTest_TestId(testId);
        if (attemptProblems.isEmpty()) {
            throw new MorandiException(AttemptProblemErrorCode.ATTEMPT_PROBLEM_NOT_FOUND);
        }
        Tests test = attemptProblems.get(0).getTest();
        long[] index = {1};
        List<AttemptProblemDto> attemptProblemDtos =
                attemptProblems
                        .stream().map(attemptProblem -> {
                    AttemptProblemDto attemptProblemDto = AttemptProblemDto.getAttemptProblemDto(attemptProblem);
                    attemptProblemDto.setTestProblemId(index[0]++);
                    return attemptProblemDto;

                }).collect(Collectors.toList());

        TestRecordDto testRecordDto = TestRecordMapper.convertToDto(test, attemptProblemDtos);
        return testRecordDto;
    }
}
