package swm_nm.morandi.domain.testDuring.scheduler;

import org.springframework.stereotype.Component;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TestMapManager {
    private final ConcurrentHashMap<Long, TestCheckDto> testMap = new ConcurrentHashMap<>();
    public ConcurrentHashMap<Long, TestCheckDto> getTestMap() {
        return testMap;
    }
    public void addTest(TestCheckDto testCheckDto) {
        testMap.put(testCheckDto.getTestId(), testCheckDto);
    }
}
