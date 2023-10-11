package swm_nm.morandi.domain.codeSubmit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.domain.codeSubmit.dto.BaekjoonCookieDto;
import swm_nm.morandi.domain.codeSubmit.dto.SubmitCodeDto;
import swm_nm.morandi.domain.codeSubmit.service.BaekjoonSubmitService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/submit")
@Tag(name = "CodeSubmitController", description = "코드 제출과 관련된 컨트롤러")
public class CodeSubmitController {

    private final BaekjoonSubmitService submitService;

    @PostMapping("/baekjoon")
    @Operation(summary = "문제 번호, 언어이름, 소스코드를 백준에 제출하는 컨트롤러 ", description = "사용자가 테스트 중 코드를 제출하는 경우 백준에 제출하는 컨트롤러입니다.")
    public ResponseEntity<String> submit(@RequestBody SubmitCodeDto submitCodeDto) {
        return submitService.submit(submitCodeDto);
    }

    @PostMapping("/cookie")
    public String loginForSubmit(@RequestBody BaekjoonCookieDto cookieDto) {
        return submitService.saveBaekjoonCookie(cookieDto);
    }
}
