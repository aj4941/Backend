package swm_nm.morandi.domain.testRetry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.domain.testInfo.dto.TestDto;
import swm_nm.morandi.domain.testRetry.request.RetryTestRequest;
import swm_nm.morandi.domain.testRetry.response.TestRetryResponse;
import swm_nm.morandi.domain.testRetry.service.TestRetryService;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Tag(name = "TestRetryController", description = "테스트 다시 풀기와 관련된 컨트롤러")
public class TestRetryController {

    private final TestRetryService testRetryService;

    @PostMapping("/retry")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "테스트 다시 풀기", description = "테스트 다시 풀기")
    public TestRetryResponse retryTest(@RequestBody RetryTestRequest retryTestRequest) {
        return testRetryService.retryTest(retryTestRequest);
    }


}
