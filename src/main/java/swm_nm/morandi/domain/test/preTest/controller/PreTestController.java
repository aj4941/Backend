package swm_nm.morandi.domain.test.preTest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.domain.test.dto.TestRecordDto;
import swm_nm.morandi.domain.test.dto.TestTypeDto;
import swm_nm.morandi.domain.test.preTest.service.PreTestService;

import java.util.List;

@RestController
@RequestMapping("/test-types")
@RequiredArgsConstructor
@Tag(name = "PreTestController", description = "테스트 시행 전에 호출되는 컨트롤러")
public class PreTestController {

    private final PreTestService preTestService;
    @GetMapping("/latest")
    @Operation(summary = "최근에 본 테스트 목록", description = "메인 페이지에서 최근에 본 테스트 5개를 제공합니다.")
    public ResponseEntity<List<TestRecordDto>> getLatestTestDtos() {
        return new ResponseEntity<>(preTestService.getTestRecordDtosLatest(), HttpStatus.OK);
    }
    @GetMapping("/practice")
    @Operation(summary = "연습 테스트 목록", description = "메인 페이지에서 현재 있는 연습 테스트 정보를 제공합니다.")
    public ResponseEntity<List<TestTypeDto>> getPracticeTestTypeDtos() {
        return new ResponseEntity<>(preTestService.getPracticeTestTypeDtos(), HttpStatus.OK);
    }
    @GetMapping("/company")
    @Operation(summary = "기업 테스트 목록", description = "메인 페이지에서 현재 있는 기업 테스트 정보를 제공합니다.")
    public ResponseEntity<List<TestTypeDto>> getCompanyTestTypeDtos() {
        return new ResponseEntity<>(preTestService.getCompanyTestTypeDtos(), HttpStatus.OK);
    }
    @GetMapping("/{testTypeId}")
    @Operation(summary = "특정 테스트 정보", description = "사용자가 원하는 테스트를 눌렀을 때 상세 테스트 정보를 제공합니다.")
    public ResponseEntity<TestTypeDto> getTestTypeInfo(@PathVariable Long testTypeId) {
        return new ResponseEntity<>(preTestService.getTestTypeDto(testTypeId), HttpStatus.OK);
    }
}