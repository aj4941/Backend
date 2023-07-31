package swm_nm.morandi.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.problem.dto.OutputDto;
import swm_nm.morandi.test.dto.TestInputData;
import swm_nm.morandi.test.dto.TestStartRequestDto;
import swm_nm.morandi.test.dto.TestStartResponseDto;
import swm_nm.morandi.test.dto.TestTypeDto;
import swm_nm.morandi.test.service.TestService;
import swm_nm.morandi.test.service.TestTypeService;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TestController {

    private final TestTypeService testTypeService;

    private final TestService testService;
    @GetMapping("/test-types")
    public ResponseEntity<List<TestTypeDto>> getTestTypeDtos() {
        return new ResponseEntity<>(testTypeService.getTestTypeDtos(), HttpStatus.OK);
    }
    @GetMapping("/test-types/{testTypeId}")
    public ResponseEntity<TestTypeDto> getTestTypeInfo(@PathVariable Long testTypeId) {
        return new ResponseEntity<>(testTypeService.getTestTypeDto(testTypeId), HttpStatus.OK);
    }

    @PostMapping("/tests")
    public ResponseEntity<TestStartResponseDto> testStart
            (@RequestBody TestStartRequestDto testStartRequestDto) throws JsonProcessingException {
        Long testTypeId = testStartRequestDto.getTestTypeId();
        return new ResponseEntity<>(testService.getTestStartsData(testTypeId), HttpStatus.OK);
    }

    @PostMapping("/tests/output")
    public ResponseEntity<OutputDto> getOutputResult
            (@RequestBody TestInputData testInputData) throws IOException, InterruptedException {
        return new ResponseEntity(testTypeService.runCode(testInputData), HttpStatus.OK);
    }
}