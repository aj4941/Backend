package swm_nm.morandi.domain.practice.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PracticeIdDto {
    private Long practiceProblemId;
    public static PracticeIdDto getPracticeIdDto(Long practiceProblemId) {
        return PracticeIdDto.builder()
                .practiceProblemId(practiceProblemId)
                .build();
    }
}
