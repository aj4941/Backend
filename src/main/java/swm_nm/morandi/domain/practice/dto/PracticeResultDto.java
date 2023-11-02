package swm_nm.morandi.domain.practice.dto;

import lombok.*;
import swm_nm.morandi.domain.practice.entity.PracticeProblem;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@Builder
public class PracticeResultDto {

    private LocalDate practiceDate;

    private Boolean isSolved;

    private String solvedTime;

    public static PracticeResultDto getPracticeResultDto(PracticeProblem practiceProblem) {
        PracticeResultDto practiceResultDto = PracticeResultDto.builder()
                .practiceDate(practiceProblem.getPracticeDate())
                .isSolved(practiceProblem.getIsSolved())
                .build();

        return practiceResultDto;
    }
}
