package swm_nm.morandi.domain.testRecord.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import swm_nm.morandi.domain.problem.dto.DifficultyLevel;
import swm_nm.morandi.domain.testExit.dto.AttemptProblemDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TestRecordDto {
    private Long testId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime testDate;

    private Long testTime;
    private Integer problemCount;
    private Integer acceptCount;
    private DifficultyLevel startDifficulty;
    private DifficultyLevel endDifficulty;
    private String testTypename;
    private Long testRating;
    private Long originRating;
    private List<AttemptProblemDto> attemptProblemDto;
}
