package swm_nm.morandi.domain.testDuring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.domain.testDuring.dto.*;
import swm_nm.morandi.domain.testExit.dto.AttemptProblemDto;

import swm_nm.morandi.domain.testDuring.service.RunCodeService;
import swm_nm.morandi.domain.testDuring.service.TempCodeService;
import swm_nm.morandi.domain.testDuring.service.SolvedCheckService;

import java.util.List;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Tag(name = "TestDuringController", description = "테스트 시행 중에 호출되는 컨트롤러")
public class TestDuringController {

    private final RunCodeService runCodeService;

    private final SolvedCheckService solvedCheckService;

    @PostMapping("/output")
    @Operation(summary = "코드 실행 결과값 반환", description = "사용자가 특정 코드를 실행할 경우 결과값을 제공합니다.")
    public ResponseEntity<OutputDto> getOutputResult
            (@RequestBody InputData inputData) throws Exception {
        return new ResponseEntity<>(runCodeService.runCode(inputData), HttpStatus.OK);
    }
    @PostMapping("/tc-output")
    @Operation(summary = "테스트케이스 실행 결과값 반환", description = "사용자가 특정 코드를 실행할 경우 결과값을 제공합니다.")
    public ResponseEntity<List<OutputDto>> getTestCaseOutputResult
            (@RequestBody TestInputData testInputData) throws Exception {
        return new ResponseEntity<>(runCodeService.runTestCaseCode(testInputData), HttpStatus.OK);
    }
    @PostMapping("/is-solved")
    @Operation(summary = "테스트 문제 정답 여부 확인", description = "테스트 문제의 정답 여부를 확인 및 반환합니다.")
    public ResponseEntity<List<AttemptProblemDto>> checkAttemptedProblemResult
            (@RequestBody TestCheckDto testCheckDto) {
        return new ResponseEntity<>(solvedCheckService.isSolvedCheck(testCheckDto), HttpStatus.OK);
    }
}
