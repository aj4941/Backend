package swm_nm.morandi.domain.test.preTest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.test.dto.TestRatingDto;
import swm_nm.morandi.domain.test.dto.TestRecordDto;
import swm_nm.morandi.domain.test.dto.TestStatus;
import swm_nm.morandi.domain.test.dto.TestTypeDto;
import swm_nm.morandi.domain.test.preTest.mapper.TestTypeMapper;
import swm_nm.morandi.domain.test.repository.TestRepository;
import swm_nm.morandi.global.utils.SecurityUtils;
import swm_nm.morandi.domain.member.dto.CurrentRatingDto;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.test.repository.AttemptProblemRepository;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.test.entity.Tests;
import swm_nm.morandi.domain.test.entity.TestType;
import swm_nm.morandi.domain.test.preTest.mapper.TestRecordMapper;
import swm_nm.morandi.domain.test.repository.TestTypeRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.MemberErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestTypeErrorCode;

import swm_nm.morandi.domain.test.entity.AttemptProblem;
import swm_nm.morandi.domain.test.postTest.request.AttemptProblemDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;


@Service
@RequiredArgsConstructor
public class PreTestService {

    private final MemberRepository memberRepository;

    private final TestRepository testRepository;

    private final TestTypeRepository testTypeRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    public List<TestRecordDto> getTestRecordDtosLatest() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Pageable pageable = PageRequest.of(0, 5);
        List<Tests> recentTests = testRepository.findAllByMember_MemberIdOrderByTestDateDesc(memberId, pageable);
        List<TestRecordDto> testRecordDtos = recentTests.stream().map(TestRecordMapper::convertToDto).collect(Collectors.toList());
        return testRecordDtos;
    }

    public CurrentRatingDto getCurrentRatingDto() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));
        CurrentRatingDto currentRatingDto = CurrentRatingDto.builder()
                .rating(member.getRating())
                .build();
        return currentRatingDto;
    }
    public List<TestTypeDto> getPracticeTestTypeDtos() {
        List<TestTypeDto> testTypeDtos = LongStream.rangeClosed(1, 6).mapToObj(i -> testTypeRepository.findById(i)
                        .orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND)))
                .map(TestTypeMapper::convertToDto).collect(Collectors.toList());
        return testTypeDtos;
    }

    public List<TestTypeDto> getCompanyTestTypeDtos() {
        List<TestTypeDto> testTypeDtos = LongStream.rangeClosed(7, 12).mapToObj(i -> testTypeRepository.findById(i)
                        .orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND)))
                .map(TestTypeMapper::convertToDto).collect(Collectors.toList());
        return testTypeDtos;
    }

    public TestTypeDto getTestTypeDto(Long testTypeId) {
        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        TestTypeDto testTypeDto = TestTypeMapper.convertToDto(testType);
        return testTypeDto;
    }

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

    public List<TestRatingDto> getTestRatingDtos() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        List<Tests> tests = testRepository.findAllByMember_MemberId(memberId);
        List<TestRatingDto> testRatingDtos = new ArrayList<>();
        tests.stream()
                .filter(test -> test.getTestStatus() != TestStatus.IN_PROGRESS)
                .forEach(test -> {
                    LocalDateTime testDate = test.getTestDate();
                    String testTypename = test.getTestTypename();
                    Long testRating = test.getTestRating();
                    Long testId = test.getTestId();
                    TestRatingDto testRatingDto = TestRatingDto.builder()
                            .testId(testId)
                            .testTypeName(testTypename)
                            .testDate(testDate)
                            .testRating(testRating)
                            .build();
                    testRatingDtos.add(testRatingDto);
                });

        if (testRatingDtos.size() == 0) {
            TestRatingDto testRatingDto = TestRatingDto.builder()
                    .testId(null)
                    .testTypeName(null)
                    .testDate(null)
                    .testRating(null)
                    .build();
            testRatingDtos.add(testRatingDto);
        }

        return testRatingDtos;
    }
}
