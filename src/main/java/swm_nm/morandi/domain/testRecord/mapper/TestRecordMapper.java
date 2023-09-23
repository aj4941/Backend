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

        TestRecordDto testRecordDto = TestRecordDto.builder()
                .testId(test.getTestId())
                .testDate(test.getTestDate())
                .testTime(test.getTestTime())
                .problemCount(test.getProblemCount())
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
                .testId(null)
                .testDate(null)
                .testTime(null)
                .problemCount(null)
                .startDifficulty(null)
                .endDifficulty(null)
                .testTypename(null)
                .testRating(null)
                .originRating(null)
                .attemptProblemDto(new ArrayList<>())
                .build();

        return testRecordDto;
    }
}
