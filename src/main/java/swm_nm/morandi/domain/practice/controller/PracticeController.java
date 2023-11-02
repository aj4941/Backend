package swm_nm.morandi.domain.practice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm_nm.morandi.domain.practice.dto.PracticeExitDto;
import swm_nm.morandi.domain.practice.dto.PracticeResultDto;
import swm_nm.morandi.domain.practice.dto.PracticeStartDto;
import swm_nm.morandi.domain.practice.dto.PracticeStartResponseDto;
import swm_nm.morandi.domain.practice.service.PracticeProblemService;
import swm_nm.morandi.domain.testDuring.dto.InputData;
import swm_nm.morandi.domain.testDuring.dto.OutputDto;
import swm_nm.morandi.domain.testDuring.dto.TestCaseInputData;
import swm_nm.morandi.domain.testDuring.service.RunCodeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/practices")
public class PracticeController {

    private final PracticeProblemService practiceProblemService;

    private final RunCodeService runCodeService;
    @PostMapping
    @Operation(summary = "연습 문제 시작하기", description = "사용자가 임의로 백준 문제 번호를 골라서 문제를 풀려고 시도할 때 테이블을 구성합니다.")
    public ResponseEntity<PracticeStartResponseDto> practiceStart(@RequestBody PracticeStartDto practiceStartDto) {
        PracticeStartResponseDto practiceStartResponseDto = practiceProblemService.startPractices(practiceStartDto.getBojProblemId());
        return new ResponseEntity(practiceStartResponseDto, HttpStatus.OK);
    }
    @PostMapping("/exit")
    @Operation(summary = "연습 문제 종료하기", description = "사용자가 시도한 연습문제를 종료할 경우 결과 데이터를 반환합니다.")
    public ResponseEntity<PracticeResultDto> practiceExit(@RequestBody PracticeExitDto practiceExitDto) {
        PracticeResultDto practiceResultDto = practiceProblemService.practiceResult(practiceExitDto.getPracticeProblemId());
        return new ResponseEntity<>(practiceResultDto, HttpStatus.OK);
    }

    @PostMapping("/output")
    @Operation(summary = "연습문제 코드 실행 결과값 반환", description = "사용자가 특정 코드를 실행할 경우 결과값을 제공합니다. " +
            "입력 데이터는 practiceProblemId, language, code, input으로 구성됩니다." )
    public ResponseEntity<OutputDto> getOutputResult(@RequestBody InputData inputData) throws Exception {
        return new ResponseEntity<>(runCodeService.runCode(inputData), HttpStatus.OK);
    }

    @PostMapping("/tc-output")
    @Operation(summary = "테스트케이스 실행 결과값 반환", description = "사용자가 특정 코드를 실행할 경우 결과값을 제공합니다. " +
            "입력 데이터는 practiceProblemId, language, code, input, output으로 구성됩니다.")
    public ResponseEntity<List<OutputDto>> getTestCaseOutputResult(@RequestBody TestCaseInputData testCaseInputData) throws Exception {
        return new ResponseEntity<>(runCodeService.runTestCaseCode(testCaseInputData), HttpStatus.OK);
    }
}
