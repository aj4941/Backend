package swm_nm.morandi.domain.testInfo.dto;

import lombok.*;
import swm_nm.morandi.domain.testRecord.dto.TestRecordDto;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestPageDto {
    private Long totalPage;
    private List<TestRecordDto> testRecordDtos = new ArrayList<>();
    public static TestPageDto getTestPageDto(Long totalPage, List<TestRecordDto> testRecordDtos) {
        return TestPageDto.builder()
                    .totalPage(totalPage)
                    .testRecordDtos(testRecordDtos)
                    .build();
    }
}
