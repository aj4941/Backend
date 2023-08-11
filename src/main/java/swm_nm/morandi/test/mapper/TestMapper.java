package swm_nm.morandi.test.mapper;

import swm_nm.morandi.test.entity.Test;
import swm_nm.morandi.test.dto.TestDto;

public class TestMapper {
    public static TestDto convertToDto(Test test) {
        TestDto testDto = TestDto.builder()
                .testId(test.getTestId())
                .testDate(test.getTestDate())
                .testTime(test.getTestTime())
                .problemCount(test.getProblemCount())
                .startDifficulty(test.getStartDifficulty())
                .endDifficulty(test.getEndDifficulty())
                .testTypename(test.getTestTypename())
                .testRating(test.getTestRating())
                .build();

        return testDto;
    }
}
