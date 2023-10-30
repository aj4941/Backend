package swm_nm.morandi.domain.testExit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import swm_nm.morandi.domain.testDuring.dto.TestCheckDto;
import swm_nm.morandi.domain.testExit.dto.AttemptCodeDto;
import swm_nm.morandi.domain.testExit.dto.TestResultDto;
import swm_nm.morandi.domain.testExit.service.SaveCodeService;
import swm_nm.morandi.domain.testExit.service.TestExitService;

import javax.servlet.http.HttpSession;
import java.net.URI;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Tag(name = "TestExitController", description = "테스트가 종료될 때 호출되는 컨트롤러")
public class TestExitController {

    private final TestExitService testExitService;

    private final SaveCodeService saveCodeService;
    @PostMapping("/exit")
    @Operation(summary = "테스트 종료하기", description = "테스트를 종료할 경우 문제별 정답 여부와 이를 기반으로 레이팅을 계산합니다.")
    public ResponseEntity saveAttemptedProblemResultByPost(@RequestBody TestCheckDto testCheckDto,
                                                                    HttpSession session) {
        session.setAttribute("testResultDto", testExitService.testExit(testCheckDto));
        return new ResponseEntity<>(HttpStatus.SEE_OTHER);
    }

    @GetMapping("/result")
    @Operation(summary = "테스트 결과 데이터 제공", description = "테스트를 종료할 때 POST 요청을 받은 것을 PRG 패턴을 통해 GET 요청으로 받습니다.")
    public ResponseEntity<TestResultDto> saveAttemptedProblemResultByGet(HttpSession session) {
        TestResultDto testResultDto = (TestResultDto) session.getAttribute("testResultDto");
        return new ResponseEntity<>(testResultDto, HttpStatus.OK);
    }

    @PostMapping("/submit")
    @Operation(summary = "테스트 종료 시점 코드 저장", description = "테스트 종료 시점에서 종료하기 직전 API를 호출하여 사용자가 풀어둔 코드를 저장해야 합니다.")
    public ResponseEntity<?> saveCode(@RequestBody AttemptCodeDto attemptCodeDto) {
        saveCodeService.saveEachCodeinAttemptProblems(attemptCodeDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
