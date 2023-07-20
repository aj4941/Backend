package swm_nm.morandi.mapper;

import swm_nm.morandi.domain.Test;
import swm_nm.morandi.dto.TestRecordDto;

import java.util.HashMap;

public class TestRecordMapper {
    public static TestRecordDto convertToDto(Test test) {
        TestRecordDto testRecordDto = TestRecordDto.builder()
                .testDate(test.getTestDate())
                .testTime(test.getTestTime())
                .problemCount(test.getProblemCount())
                .startDifficulty(test.getStartDifficulty())
                .endDifficulty(test.getEndDifficulty())
                .testTypename(test.getTestTypename())
                .testRating(test.getTestRating())
                .solvedInfo(new HashMap<>())
                .build();

        return testRecordDto;
    }
}
