package swm_nm.morandi.test.mapper;

import swm_nm.morandi.test.domain.TestType;
import swm_nm.morandi.test.dto.TestTypeDto;

public class TestTypeMapper {
    public static TestTypeDto convertToDto(TestType testType) {
        TestTypeDto testTypeDto = TestTypeDto.builder()
                .testTypeId(testType.getTestTypeId())
                .testTypename(testType.getTestTypename())
                .testTime(testType.getTestTime())
                .problemCount(testType.getProblemCount())
                .startDifficulty(testType.getStartDifficulty())
                .endDifficulty(testType.getEndDifficulty())
                .numberOfTestTrial(testType.getNumberOfTestTrial())
                .averageCorrectAnswerRate(testType.getAverageCorrectAnswerRate())
                .build();

        return testTypeDto;
    }
}
