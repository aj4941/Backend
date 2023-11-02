package swm_nm.morandi.domain.testRecord.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.testInfo.repository.TestHistoryRepositoryImpl;
import swm_nm.morandi.domain.testRecord.dto.*;
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

    private final TestHistoryRepositoryImpl testStatusRepository;

    @Transactional(readOnly = true)
    public TestRecordDto getTestRecordDtoByTestId(Long testId) {
        //N+1문제 발생하여 fetch join으로 해결
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findTestDetailsByTest_TestId(testId);
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

    @Transactional(readOnly = true)
    public AllTestHistoryResponse findAllTestStatusByCondition(TestHistoryCondition testHistoryCondition) {
        Page<Tests> testHistory = testStatusRepository.findAllTestHistoryByCondition(testHistoryCondition);

        List<TestHistoryDto> testHistoryDtos = testHistory.stream().map(tests -> TestHistoryDto.builder().
                testId(tests.getTestId())
                .testDate(tests.getTestDate())
                .memberId(tests.getMember().getMemberId())
                .bojId(tests.getMember().getBojId())
                .testTypename(tests.getTestTypename())
                .problemCount(tests.getAttemptProblems().size())
                .solvedCount((int) tests.getAttemptProblems().stream().filter(AttemptProblem::getIsSolved).count())
                .attemptProblems(AttemptProblemDto.getAttemptProblemDtoList(tests.getAttemptProblems()))
                .build()).toList();

        return AllTestHistoryResponse.builder()
                .testHistorys(testHistoryDtos)
                .totalElements(testHistory.getTotalElements())
                .currentSize(testHistory.getSize())
                .build();

    }
}
