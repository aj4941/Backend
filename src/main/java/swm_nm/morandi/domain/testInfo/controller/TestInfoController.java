package swm_nm.morandi.domain.testInfo.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.domain.testRecord.dto.TestRecordDto;
import swm_nm.morandi.domain.testInfo.dto.TestTypeDto;
import swm_nm.morandi.domain.testInfo.service.*;

import java.util.List;

@RestController
@RequestMapping("/test-types")
@RequiredArgsConstructor
@Tag(name = "TestInfoController", description = "테스트 정보와 관련된 컨트롤러")
public class TestInfoController {

    private final LatestTestInfoService latestTestInfoService;

    private final PracticeTestInfoService practiceTestInfoService;

    private final CompanyTestInfoService companyTestInfoService;

    private final RandomDefenseInfoService randomDefenseInfoService;

    private final TestTypeInfoService testTypeInfoService;
    @GetMapping("/latest")
    @Operation(summary = "최근에 본 테스트 목록", description = "마이 페이지에서 최근에 본 테스트 4개를 제공합니다.")
    public ResponseEntity<List<TestRecordDto>> getLatestTestDtos() {
        return new ResponseEntity<>(latestTestInfoService.getTestRecordDtosLatest(), HttpStatus.OK);
    }
    @GetMapping("/practice")
    @Operation(summary = "연습 테스트 목록", description = "메인 페이지에서 현재 있는 연습 테스트 정보를 제공합니다.")
    public ResponseEntity<List<TestTypeDto>> getPracticeTestTypeDtos() {
        return new ResponseEntity<>(practiceTestInfoService.getPracticeTestTypeDtos(), HttpStatus.OK);
    }
    @GetMapping("/company")
    @Operation(summary = "기업 테스트 목록", description = "메인 페이지에서 현재 있는 기업 테스트 정보를 제공합니다.")
    public ResponseEntity<List<TestTypeDto>> getCompanyTestTypeDtos() {
        return new ResponseEntity<>(companyTestInfoService.getCompanyTestTypeDtos(), HttpStatus.OK);
    }
    @GetMapping("/random-defense")
    @Operation(summary = "랜덤 디펜스 목록", description = "메인 페이지에서 현재 있는 랜덤 디펜스 정보를 제공합니다.")
    public ResponseEntity<List<TestTypeDto>> getRandomDefenseTestTypeDtos() {
        return new ResponseEntity<>(randomDefenseInfoService.getRandomDefenseTestTypeDtos(), HttpStatus.OK);
    }
    @GetMapping("/{testTypeId}")
    @Operation(summary = "테스트 상세 정보", description = "사용자가 원하는 테스트 유형을 눌렀을 때 상세 테스트 정보를 제공합니다.")
    public ResponseEntity<TestTypeDto> getTestTypeInfo(@PathVariable Long testTypeId) {
        return new ResponseEntity<>(testTypeInfoService.getTestTypeDto(testTypeId), HttpStatus.OK);
    }
}