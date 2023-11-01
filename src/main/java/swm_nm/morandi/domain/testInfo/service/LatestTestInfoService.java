package swm_nm.morandi.domain.testInfo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.aop.annotation.Logging;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testExit.dto.AttemptProblemDto;
import swm_nm.morandi.domain.testInfo.dto.TestPageDto;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testRecord.dto.TestRecordDto;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testRecord.dto.TestRecordRequestDto;
import swm_nm.morandi.domain.testRecord.mapper.TestRecordMapper;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.*;

@Service
@RequiredArgsConstructor
public class LatestTestInfoService {

    private final TestRepository testRepository;

    @Transactional(readOnly = true)
    public TestPageDto getTestRecordDtosLatest(TestRecordRequestDto testRecordRequestDto) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Integer page = testRecordRequestDto.getPage();
        Integer size = testRecordRequestDto.getSize();
        Long totalCount = testRepository.countByMember_MemberIdAndTestStatus(memberId, TestStatus.COMPLETED);
        Long totalPage = (totalCount + size - 1) / size;

        //페이징하여 최근 4개의 테스트 기록을 가져옴
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(DESC, "testDate"));
        List<Tests> recentTests = testRepository.findAllTestsByMember_MemberIdAndTestStatus(memberId, TestStatus.COMPLETED, pageable);

        //테스트 기록을 받아와서 dto로 변환하면서 getAttemptProblemDtos를 통해 테스트 문제들을 dto로 변환
        List<TestRecordDto> testRecordDtos =
                recentTests.stream().map(recentTest -> {
                    List<AttemptProblemDto> attemptProblemDtos = getAttemptProblemDtos(recentTest);
                    TestRecordDto testRecordDto = TestRecordMapper.convertToDto(recentTest, attemptProblemDtos);
                    return testRecordDto;})
                        .collect(Collectors.toList());

        //테스트 기록이 4개 미만일 경우 더미 데이터를 넣어줌
        getTestRecordDtos(testRecordDtos);

        TestPageDto testPageDto = TestPageDto.getTestPageDto(totalPage, testRecordDtos);

        return testPageDto;
    }

    //테스트 기록을 받아와서 dto로 변환하면서 getAttemptProblemDtos를 통해 테스트 문제들을 dto로 변환
    //Test의 AttemptProblems를 LAZY 로딩하여두고, default_batch_fetch_size 를 통해 한번에 가져옴
    @Transactional(readOnly = true)
    public List<AttemptProblemDto> getAttemptProblemDtos(Tests test) {
        final long[] index = { 1 };
        // after
        return test.getAttemptProblems().stream().map(attemptProblem -> {
                AttemptProblemDto attemptProblemDto = AttemptProblemDto.getAttemptProblemDto(attemptProblem);
                attemptProblemDto.setTestProblemId(index[0]++);
                return attemptProblemDto;
        }).collect(Collectors.toList());
    }

    private static void getTestRecordDtos(List<TestRecordDto> testRecordDtos) {
        while (testRecordDtos.size() < 4) {
            testRecordDtos.add(TestRecordMapper.dummyDto());
        }
    }
}
