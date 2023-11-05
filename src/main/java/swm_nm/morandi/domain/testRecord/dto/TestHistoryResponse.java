package swm_nm.morandi.domain.testRecord.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import swm_nm.morandi.domain.testExit.dto.AttemptProblemDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TestHistoryResponse {

    public Long testId;

    public Long memberId;

    public String bojId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime testDate;

    public String testTypename;

    public Integer problemCount;

    public Integer solvedCount;

    public List<AttemptProblemDto> attemptProblems;

}
