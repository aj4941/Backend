package swm_nm.morandi.domain.testRecord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class AllTestHistoryResponse {
    public long totalElements;
    public int currentSize;
    public List<TestHistoryDto> testHistorys;
    
}
