package swm_nm.morandi.testResult.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.auth.security.SecurityUtils;
import swm_nm.morandi.test.dto.TestCheckDto;
import swm_nm.morandi.testResult.request.AttemptProblemDto;
import swm_nm.morandi.testResult.request.TestResultDto;
import swm_nm.morandi.testResult.service.AttemptProblemService;
import swm_nm.morandi.testResult.service.TestResultService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Tag(name = "TestResultController", description = "테스트 결과와 관련된 컨트롤러")
public class TestResultController {

    private final TestResultService testResultService;

    // 진행한 테스트 저장
    // 테스트 해당하는 문제별로 저장
    @PostMapping("/exit")
    @Operation(summary = "테스트 종료하기", description = "테스트를 종료할 경우 문제별 정답 여부와 소요 시간을 제공합니다.")
    public ResponseEntity<List<AttemptProblemDto>> saveAttemptedProblemResult(@RequestBody TestCheckDto testCheckDto) {
        return new ResponseEntity<>(testResultService.testExit(testCheckDto), HttpStatus.OK);
    }

    // 문제별 정답여부 확인하는 API
    @PostMapping("/is-solved")
    @Operation(summary = "테스트 문제 정답 여부 확인", description = "테스트 문제의 정답 여부를 확인 및 반환합니다.")
    public ResponseEntity<List<AttemptProblemDto>> checkAttemptedProblemResult
                                    (@RequestBody TestCheckDto testCheckDto){
        return new ResponseEntity<>(testResultService.isSolvedCheck(testCheckDto), HttpStatus.OK);
    }
}
