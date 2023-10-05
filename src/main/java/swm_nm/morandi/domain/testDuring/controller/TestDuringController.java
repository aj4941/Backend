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

    private final TempCodeService tempCodeService;

    @PostMapping("/output")
    @Operation(summary = "코드 실행 결과값 반환", description = "사용자가 특정 코드를 실행할 경우 결과값을 제공합니다.")
    public ResponseEntity<List<OutputDto>> getOutputResult
            (@RequestBody TestInputData testInputData) throws Exception {
        return new ResponseEntity<>(runCodeService.runCode(testInputData), HttpStatus.OK);
    }
    @PostMapping("/is-solved")
    @Operation(summary = "테스트 문제 정답 여부 확인", description = "테스트 문제의 정답 여부를 확인 및 반환합니다.")
    public ResponseEntity<List<AttemptProblemDto>> checkAttemptedProblemResult
            (@RequestBody TestCheckDto testCheckDto) {
        return new ResponseEntity<>(solvedCheckService.isSolvedCheck(testCheckDto), HttpStatus.OK);
    }

    @PostMapping("/code")
    @Operation(summary = "테스트 중인 코드를 저장합니다", description = "테스트 중인 코드를 저장\n" +
            "testId와 attemptProblemId를 통해 테스트 중인 코드를 지속적으로 저장함\n")
    public void saveTempCode(@RequestBody TempCodeDto tempCodeDto) {
        tempCodeService.saveTempCode(tempCodeDto);
    }

    @GetMapping("/code")
    @Operation(summary = "저장된 코드를 확인합니다", description = "테스트 중일 때, 문제 번호를 바꿀 때 코드 정보를\n" +
            "testId와 attemptProblemId를 이용하여 가져온다. \n")
    public ResponseEntity<TempCode> getTempCode(@RequestParam String testId,
                                                @RequestParam String problemNumber) {

        String key = String.format("testId:%s:problemNumber:%s", testId, problemNumber);

        return new ResponseEntity<>(tempCodeService.getTempCode(key), HttpStatus.OK);

    }
}
