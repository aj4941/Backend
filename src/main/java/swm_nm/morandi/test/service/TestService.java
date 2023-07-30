package swm_nm.morandi.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.member.repository.MemberRepository;
import swm_nm.morandi.test.domain.TestType;
import swm_nm.morandi.test.dto.TestDto;
import swm_nm.morandi.test.dto.TestStatus;
import swm_nm.morandi.test.repository.TestTypeRepository;
import swm_nm.morandi.testResult.entity.AttemptProblem;
import swm_nm.morandi.test.domain.Test;
import swm_nm.morandi.test.dto.TestRatingDto;
import swm_nm.morandi.test.dto.TestRecordDto;
import swm_nm.morandi.test.mapper.TestRecordMapper;
import swm_nm.morandi.member.repository.AttemptProblemRepository;
import swm_nm.morandi.test.repository.TestRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class TestService {

    private final MemberRepository memberRepository;

    private final TestRepository testRepository;

    private final TestTypeRepository testTypeRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    @Transactional
    public Long getTestByTestTypeId(Long testTypeId, Long memberId) {
        Optional<TestType> resultTestType = testTypeRepository.findById(testTypeId);
        TestType testType = resultTestType.get();
        Optional<Member> resultMember = memberRepository.findById(memberId);
        Member member = resultMember.get();
        Test test = Test.builder()
                .testDate(LocalDateTime.now())
                .testTime(testType.getTestTime())
                .problemCount(testType.getProblemCount())
                .startDifficulty(testType.getStartDifficulty())
                .endDifficulty(testType.getEndDifficulty())
                .testTypename(testType.getTestTypename())
                .testRating(null)
                .testStatus(TestStatus.IN_PROGRESS)
                .member(member)
                .build();
        testRepository.save(test);
        return test.getTestId();
    }
    public TestRecordDto getTestRecordDtoByTestId(Long testId) {
        Optional<Test> testResult = testRepository.findById(testId);
        Test test = testResult.get();
        TestRecordDto testRecordDto = TestRecordMapper.convertToDto(test);
        Optional<List<AttemptProblem>> attemptProblemResult
                = attemptProblemRepository.findAllByTest_TestId(testId);
        if (attemptProblemResult.isPresent()) {
            List<AttemptProblem> attemptProblems = attemptProblemResult.get();
            int index = 1;
            for (AttemptProblem attemptProblem : attemptProblems) {
                if (attemptProblem.getIsSolved())
                    testRecordDto.getSolvedInfo().put(index, true);
                else
                    testRecordDto.getSolvedInfo().put(index, false);

                index++;
            }
        }
        return testRecordDto;
    }

    public List<TestRatingDto> getTestRatingDtosByMemberId(Long memberId) {
        List<Test> tests = testRepository.findAllByMember_MemberId(memberId);
        List<TestRatingDto> testRatingDtos = new ArrayList<>();
        for (Test test : tests) {
            LocalDate testDate = LocalDate.from(test.getTestDate());
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
        }

        return testRatingDtos;
    }

}
