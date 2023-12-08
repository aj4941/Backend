package swm_nm.morandi.domain.testStart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm_nm.morandi.domain.testStart.dto.TestStartRequestDto;
import swm_nm.morandi.domain.testStart.dto.TestStartResponseDto;
import swm_nm.morandi.domain.testStart.service.TestStartUseCase;
import swm_nm.morandi.global.annotations.CurrentMember;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Tag(name = "TestStartController", description = "테스트를 시작할 때 호출되는 컨트롤러")
public class TestStartController {

    private final TestStartUseCase testStartUseCase;
    @PostMapping
    @Operation(summary = "테스트 입장", description = "사용자가 테스트에 입장할 때 테스트 문제 세트를 구성합니다.")
    public ResponseEntity<TestStartResponseDto> testStart
            (@CurrentMember Long memberId,
             @RequestBody TestStartRequestDto testStartRequestDto) {
        Long testTypeId = testStartRequestDto.getTestTypeId();
        return new ResponseEntity<>(testStartUseCase.getTestStartsData(memberId, testTypeId), HttpStatus.OK);
    }
}
