package swm_nm.morandi.domain.customTest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import swm_nm.morandi.domain.customTest.request.CustomTestRequest;
import swm_nm.morandi.domain.customTest.response.CustomTestResponses;
import swm_nm.morandi.domain.customTest.service.CustomTestService;
import swm_nm.morandi.global.annotations.CurrentMember;

@RestController
@RequiredArgsConstructor
public class CustomTestController {

    private final CustomTestService customTestService;
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/custom")
    public CustomTestResponses customTestGenerate(@CurrentMember Long memberId,
                                                  @RequestBody CustomTestRequest customTestRequest) {
        return customTestService.generateCustomTest(memberId,customTestRequest);
    }
}
