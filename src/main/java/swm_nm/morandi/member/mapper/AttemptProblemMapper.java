package swm_nm.morandi.member.mapper;

import swm_nm.morandi.testResult.entity.AttemptProblem;
import swm_nm.morandi.testResult.request.AttemptProblemDto;

public class AttemptProblemMapper {
    public static AttemptProblemDto convertToDto(AttemptProblem attemptProblem) {
        AttemptProblemDto attemptProblemDto =
                AttemptProblemDto.builder()
                        .isSolved(attemptProblem.getIsSolved())
                        .testDate(attemptProblem.getTestDate())
                        .build();
        return attemptProblemDto;
    }
}
