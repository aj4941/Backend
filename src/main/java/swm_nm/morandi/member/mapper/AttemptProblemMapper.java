package swm_nm.morandi.member.mapper;

import swm_nm.morandi.member.domain.AttemptProblem;
import swm_nm.morandi.member.dto.AttemptProblemDto;

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
