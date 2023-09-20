package swm_nm.morandi.domain.testRecord.mapper;

import swm_nm.morandi.domain.testExit.entity.Tests;
import swm_nm.morandi.domain.testRecord.dto.TestRecordDto;

import java.util.ArrayList;

public class TestRecordMapper {
    public static TestRecordDto convertToDto(Tests test) {
        TestRecordDto testRecordDto = TestRecordDto.builder()
                .testDate(test.getTestDate())
                .testTime(test.getTestTime())
                .problemCount(test.getProblemCount())
                .startDifficulty(test.getStartDifficulty())
                .endDifficulty(test.getEndDifficulty())
                .testTypename(test.getTestTypename())
                .testRating(test.getTestRating())
                .attemptProblemDto(new ArrayList<>())
                .build();

        return testRecordDto;
    }
}
