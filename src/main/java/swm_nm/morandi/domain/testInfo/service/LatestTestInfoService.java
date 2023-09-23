package swm_nm.morandi.domain.testInfo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testExit.dto.AttemptProblemDto;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testRecord.dto.TestRecordDto;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testRecord.mapper.TestRecordMapper;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LatestTestInfoService {

    private final TestRepository testRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    public List<TestRecordDto> getTestRecordDtosLatest() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Pageable pageable = PageRequest.of(0, 4);
        List<Tests> recentTests = testRepository.findDataRecent4(memberId, pageable);
        List<TestRecordDto> testRecordDtos = new ArrayList<>();
        recentTests.forEach(recentTest -> {
            List<AttemptProblemDto> attemptProblemDtos = getAttemptProblemDtos(recentTest);
            TestRecordDto testRecordDto = TestRecordMapper.convertToDto(recentTest, attemptProblemDtos);
            testRecordDtos.add(testRecordDto);
        });
        getTestRecordDtos(testRecordDtos);
        return testRecordDtos;
    }
    private List<AttemptProblemDto> getAttemptProblemDtos(Tests test) {
        List<AttemptProblemDto> attemptProblemDtos = new ArrayList<>();
        Long testId = test.getTestId();
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(testId);
        long index = 1;
        for (AttemptProblem attemptProblem : attemptProblems) {
            AttemptProblemDto attemptProblemDto = AttemptProblemDto.getAttemptProblemDto(attemptProblem);
            attemptProblemDto.setTestProblemId(index++);
            attemptProblemDtos.add(attemptProblemDto);
        }
        return attemptProblemDtos;
    }

    private static void getTestRecordDtos(List<TestRecordDto> testRecordDtos) {
        while (testRecordDtos.size() < 4) {
            testRecordDtos.add(TestRecordMapper.dummyDto());
        }
    }
}
