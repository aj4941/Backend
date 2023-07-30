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

    private final MemberService memberService;

    private final AttemptProblemService attemptProblemService;
    @GetMapping("/test-types")
    public ResponseEntity<List<TestTypeDto>> getTestTypeDtos() {
        List<TestTypeDto> testTypeDtos = testTypeService.getTestTypeDtos();
        return new ResponseEntity<>(testTypeDtos, HttpStatus.OK);
    }
    @GetMapping("/test-types/{testTypeId}")
    public ResponseEntity<TestTypeDto> getTestTypeInfo(@PathVariable Long testTypeId) {
        TestTypeDto testTypeDto = testTypeService.getTestTypeDto(testTypeId);
        return new ResponseEntity<>(testTypeDto, HttpStatus.OK);
    }

    @PostMapping("/tests")
    public ResponseEntity<Map<String, Object>> testStart
            (@RequestBody Map<String, Long> testTypeMap) throws JsonProcessingException {
        Long testTypeId = testTypeMap.get("testTypeId");
        Long memberId = SecurityUtils.getCurrentMemberId();
        Long testId = testService.getTestByTestTypeId(testTypeId, memberId);
        String bojId = memberService.getBojId(memberId);
        List<BojProblem> bojProblems = new ArrayList<>();
        testTypeService.getProblemsByTestType(testTypeId, bojProblems);
        testTypeService.getProblemsByApi(testTypeId, bojId, bojProblems);
        List<Long> attemptProblemIds = attemptProblemService.saveAttemptProblems(memberId, testId, bojProblems);
        Map<String, Object> map = new HashMap<>();
        map.put("testId", testId);
        map.put("attemptProblemIds", attemptProblemIds);
        map.put("bojProblems", bojProblems);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/tests/output")
    public ResponseEntity<OutputDto> getOutputResult
            (@RequestBody TestInputData testInputData) throws IOException, InterruptedException {
        String language = testInputData.getLanguage();
        String code = testInputData.getCode();
        String input = testInputData.getInput();
        String output = testTypeService.runCode(language, code, input);
        OutputDto outputDto = OutputDto.builder()
                .output(output)
                .build();
        return ResponseEntity.ok(outputDto);
    }
}