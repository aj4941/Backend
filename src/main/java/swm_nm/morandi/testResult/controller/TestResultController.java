package swm_nm.morandi.testResult.controller;

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
public class TestResultController {

    private final TestResultService testResultService;

    // 진행한 테스트 저장
    // 테스트 해당하는 문제별로 저장
    @PostMapping("/exit")
    public ResponseEntity<List<AttemptProblemDto>> saveAttemptedProblemResult(@RequestBody TestCheckDto testCheckDto) {
        return new ResponseEntity<>(testResultService.testExit(testCheckDto), HttpStatus.OK);
    }

    // 문제별 정답여부 확인하는 API
    @PostMapping("/is-solved")
    public ResponseEntity<List<AttemptProblemDto>> checkAttemptedProblemResult
                                    (@RequestBody TestCheckDto testCheckDto){
        return new ResponseEntity<>(testResultService.isSolvedCheck(testCheckDto), HttpStatus.OK);
    }
}
