package swm_nm.morandi.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.auth.security.SecurityUtils;
import swm_nm.morandi.problem.dto.BojProblem;
import swm_nm.morandi.problem.dto.BojProblemRequestDto;
import swm_nm.morandi.problem.dto.OutputDto;
import swm_nm.morandi.test.dto.TestInputData;
import swm_nm.morandi.test.dto.TestTypeDto;
import swm_nm.morandi.member.service.MemberService;
import swm_nm.morandi.test.service.TestService;
import swm_nm.morandi.test.service.TestTypeService;
import swm_nm.morandi.testResult.service.AttemptProblemService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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
    public ResponseEntity<Map<String, Object>> testStart
            (@RequestBody Map<String, Long> testTypeMap) throws JsonProcessingException {
        Long testTypeId = testTypeMap.get("testTypeId");
        return new ResponseEntity<>(testService.getTestStartsData(testTypeId), HttpStatus.OK);
    }

    @PostMapping("/tests/output")
    public ResponseEntity<OutputDto> getOutputResult
            (@RequestBody TestInputData testInputData) throws IOException, InterruptedException {
        return new ResponseEntity(testTypeService.runCode(testInputData), HttpStatus.OK);
    }
}