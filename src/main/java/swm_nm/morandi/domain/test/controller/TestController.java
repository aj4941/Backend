package swm_nm.morandi.domain.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.domain.problem.dto.OutputDto;
import swm_nm.morandi.domain.test.dto.*;
import swm_nm.morandi.domain.test.service.TestService;
import swm_nm.morandi.domain.test.service.TestTypeService;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "TestController", description = "테스트와 관련된 컨트롤러")
public class TestController {

    private final TestTypeService testTypeService;

    private final TestService testService;
    @GetMapping("/test-types/latest")
    @Operation(summary = "최근에 본 테스트 목록", description = "메인 페이지에서 최근에 본 테스트 5개를 제공합니다.")
    public ResponseEntity<List<TestRecordDto>> getLatestTestDtos() {
        return new ResponseEntity<>(testService.getTestRecordDtosLatest(), HttpStatus.OK);
    }
    @GetMapping("/test-types/practice")
    @Operation(summary = "연습 테스트 목록", description = "메인 페이지에서 현재 있는 연습 테스트 정보를 제공합니다.")
    public ResponseEntity<List<TestTypeDto>> getPracticeTestTypeDtos() {
        return new ResponseEntity<>(testTypeService.getPracticeTestTypeDtos(), HttpStatus.OK);
    }

    @GetMapping("/test-types/company")
    @Operation(summary = "기업 테스트 목록", description = "메인 페이지에서 현재 있는 기업 테스트 정보를 제공합니다.")
    public ResponseEntity<List<TestTypeDto>> getCompanyTestTypeDtos() {
        return new ResponseEntity<>(testTypeService.getCompanyTestTypeDtos(), HttpStatus.OK);
    }
    @GetMapping("/test-types/{testTypeId}")
    @Operation(summary = "특정 테스트 정보", description = "사용자가 원하는 테스트를 눌렀을 때 상세 테스트 정보를 제공합니다.")
    public ResponseEntity<TestTypeDto> getTestTypeInfo(@PathVariable Long testTypeId) {
        return new ResponseEntity<>(testTypeService.getTestTypeDto(testTypeId), HttpStatus.OK);
    }

    @PostMapping("/tests")
    @Operation(summary = "테스트 입장", description = "사용자가 테스트에 입장할 때 테스트 문제 세트를 구성합니다.")
    public ResponseEntity<TestStartResponseDto> testStart
            (@RequestBody TestStartRequestDto testStartRequestDto) {
        Long testTypeId = testStartRequestDto.getTestTypeId();
        return new ResponseEntity<>(testService.getTestStartsData(testTypeId), HttpStatus.OK);
    }

    @PostMapping("/tests/output")
    @Operation(summary = "코드 실행 결과값 반환", description = "사용자가 특정 코드를 실행할 경우 결과값을 제공합니다.")
    public ResponseEntity<OutputDto> getOutputResult
            (@RequestBody TestInputData testInputData) throws IOException, InterruptedException {
        return new ResponseEntity(testTypeService.runCode(testInputData), HttpStatus.OK);
    }
}