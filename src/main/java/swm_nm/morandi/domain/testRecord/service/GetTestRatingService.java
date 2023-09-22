package swm_nm.morandi.domain.testRecord.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testRecord.dto.TestRatingDto;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetTestRatingService {

    private final TestRepository testRepository;

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
