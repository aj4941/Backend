package swm_nm.morandi.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm_nm.morandi.testResult.entity.AttemptProblem;
import swm_nm.morandi.test.domain.Test;
import swm_nm.morandi.test.dto.TestRatingDto;
import swm_nm.morandi.test.dto.TestRecordDto;
import swm_nm.morandi.test.mapper.TestRecordMapper;
import swm_nm.morandi.member.repository.AttemptProblemRepository;
import swm_nm.morandi.test.repository.TestRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    private final AttemptProblemRepository attemptProblemRepository;
    public List<TestRecordDto> getTestRecordDtosByMemberId(Long memberId) {
        List<Test> tests = testRepository.findAllByMember_MemberId(memberId);
        List<TestRecordDto> testRecordDtos = new ArrayList<>();
        for (Test test : tests) {
            Long testId = test.getTestId();
            TestRecordDto testRecordDto = TestRecordMapper.convertToDto(test);
            List<AttemptProblem> attemptProblems
                    = attemptProblemRepository.findAllByTest_TestId(testId);
            int index = 1;
            for (AttemptProblem attemptProblem : attemptProblems) {
                if (attemptProblem.getIsSolved())
                    testRecordDto.getSolvedInfo().put(index, true);
                else
                    testRecordDto.getSolvedInfo().put(index, false);

                index = index + 1;
            }
            testRecordDtos.add(testRecordDto);
        }

        return testRecordDtos;
    }

    public List<TestRatingDto> getTestRatingDtosByMemberId(Long memberId) {
        List<Test> tests = testRepository.findAllByMember_MemberId(memberId);
        List<TestRatingDto> testRatingDtos = new ArrayList<>();
        for (Test test : tests) {
            LocalDate testDate = LocalDate.from(test.getTestDate());
            String testTypename = test.getTestTypename();
            Long testRating = test.getTestRating();
            TestRatingDto testRatingDto = TestRatingDto.builder()
                    .testTypeName(testTypename)
                    .testDate(testDate)
                    .testRating(testRating)
                    .build();
            testRatingDtos.add(testRatingDto);
        }

        return testRatingDtos;
    }

}
