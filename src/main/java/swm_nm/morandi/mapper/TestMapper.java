package swm_nm.morandi.mapper;

import swm_nm.morandi.domain.Test;
import swm_nm.morandi.dto.TestDto;

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
