package swm_nm.morandi.mapper;

import swm_nm.morandi.domain.AttemptProblem;
import swm_nm.morandi.dto.AttemptProblemDto;

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
