package swm_nm.morandi.domain.testRecord.mapper;

import swm_nm.morandi.domain.problem.dto.DifficultyLevel;
import swm_nm.morandi.domain.testExit.dto.AttemptProblemDto;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testRecord.dto.TestRecordDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestRecordMapper {
    public static TestRecordDto convertToDto(Tests test, List<AttemptProblemDto> attemptProblemDtos) {
        Integer acceptCount = 0;
        for (AttemptProblemDto attemptProblemDto : attemptProblemDtos) {
            if (attemptProblemDto.getIsSolved())
                acceptCount++;
        }
        TestRecordDto testRecordDto = TestRecordDto.builder()
                .testId(test.getTestId())
                .testDate(test.getTestDate())
                .testTime(test.getTestTime())
                .problemCount(test.getProblemCount())
                .acceptCount(acceptCount)
                .startDifficulty(test.getStartDifficulty())
                .endDifficulty(test.getEndDifficulty())
                .testTypename(test.getTestTypename())
                .testRating(test.getTestRating())
                .originRating(test.getOriginRating())
                .attemptProblemDto(attemptProblemDtos)
                .build();

        return testRecordDto;
    }

    public static TestRecordDto dummyDto() {
        TestRecordDto testRecordDto = TestRecordDto.builder()
                .testId(0L)
                .testDate(LocalDateTime.now())
                .testTime(0L)
                .problemCount(0)
                .startDifficulty(DifficultyLevel.B5)
                .endDifficulty(DifficultyLevel.B5)
                .testTypename("테스트가 없습니다.")
                .testRating(0L)
                .originRating(0L)
                .attemptProblemDto(new ArrayList<>())
                .build();

        return testRecordDto;
    }
}
