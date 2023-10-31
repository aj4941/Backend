package swm_nm.morandi.domain.practice.dto;

import lombok.*;
import swm_nm.morandi.domain.practice.entity.PracticeProblem;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
public class PracticeResultDto {

    private LocalDateTime practiceDate;

    private Boolean isSolved;

    private String solvedTime;

    public static PracticeResultDto getPracticeResultDto(PracticeProblem practiceProblem) {
        Long practiceProblemSolvedTime = practiceProblem.getSolvedTime();
        String solvedTime = (practiceProblemSolvedTime == null) ?
                null : String.format("%d:%02d", practiceProblemSolvedTime / 60, practiceProblemSolvedTime % 60);
        PracticeResultDto practiceResultDto = PracticeResultDto.builder()
                .practiceDate(practiceProblem.getPracticeDate())
                .isSolved(practiceProblem.getPracticeStatus() == PracticeStatus.SUCCESS)
                .solvedTime(solvedTime)
                .build();

        return practiceResultDto;
    }
}
