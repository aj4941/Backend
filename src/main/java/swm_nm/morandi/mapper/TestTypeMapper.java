package swm_nm.morandi.mapper;

import swm_nm.morandi.domain.TestType;
import swm_nm.morandi.dto.TestTypeDto;

public class TestTypeMapper {
    public static TestTypeDto convertToDto(TestType testType) {
        TestTypeDto testTypeDto = TestTypeDto.builder()
                .testTypename(testType.getTestTypename())
                .testTime(testType.getTestTime())
                .problemCount(testType.getProblemCount())
                .startDifficulty(testType.getStartDifficulty())
                .endDifficulty(testType.getEndDifficulty())
                .build();

        return testTypeDto;
    }
}
