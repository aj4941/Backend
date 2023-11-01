package swm_nm.morandi.domain.testRecord.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Builder
@Setter
@Getter
@AllArgsConstructor
public class GrassHeatMapResponse {

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate testDate;

    public Long solvedCount;

    public static GrassHeatMapResponse getGrassHeatMapResponse(LocalDate date, Long count) {
        GrassHeatMapResponse grassHeatMapResponse = GrassHeatMapResponse.builder()
                .testDate(date)
                .solvedCount(count)
                .build();
        return grassHeatMapResponse;
    }
}
