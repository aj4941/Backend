package swm_nm.morandi.domain.testRecord.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TestRecordPageRequest {
    private Integer page = 1;
    private Integer size = 4;
}
