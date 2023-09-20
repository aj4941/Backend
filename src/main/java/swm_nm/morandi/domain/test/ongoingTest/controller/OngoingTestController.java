package swm_nm.morandi.domain.test.ongoingTest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm_nm.morandi.domain.test.ongoingTest.service.OngoingTestService;
import swm_nm.morandi.domain.test.postTest.request.AttemptProblemDto;
import swm_nm.morandi.domain.test.dto.TestCheckDto;
import swm_nm.morandi.domain.test.dto.TestInputData;
import swm_nm.morandi.domain.test.dto.TestStartRequestDto;
import swm_nm.morandi.domain.test.dto.TestStartResponseDto;
import swm_nm.morandi.domain.test.dto.OutputDto;

import java.util.List;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Tag(name = "OngoingTestController", description = "테스트 시행 중에 호출되는 컨트롤러")
public class OngoingTestController {

    private final OngoingTestService ongoingTestService;
    @PostMapping
    @Operation(summary = "테스트 입장", description = "사용자가 테스트에 입장할 때 테스트 문제 세트를 구성합니다.")
    public ResponseEntity<TestStartResponseDto> testStart
            (@RequestBody TestStartRequestDto testStartRequestDto) {
        Long testTypeId = testStartRequestDto.getTestTypeId();
        return new ResponseEntity<>(ongoingTestService.getTestStartsData(testTypeId), HttpStatus.OK);
    }
    @PostMapping("/output")
    @Operation(summary = "코드 실행 결과값 반환", description = "사용자가 특정 코드를 실행할 경우 결과값을 제공합니다.")
    public ResponseEntity<OutputDto> getOutputResult
            (@RequestBody TestInputData testInputData) throws Exception {
        return new ResponseEntity(ongoingTestService.runCode(testInputData), HttpStatus.OK);
    }
    @PostMapping("/is-solved")
    @Operation(summary = "테스트 문제 정답 여부 확인", description = "테스트 문제의 정답 여부를 확인 및 반환합니다.")
    public ResponseEntity<List<AttemptProblemDto>> checkAttemptedProblemResult
            (@RequestBody TestCheckDto testCheckDto) {
        return new ResponseEntity<>(ongoingTestService.isSolvedCheck(testCheckDto), HttpStatus.OK);
    }
}
