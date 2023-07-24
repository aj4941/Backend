package swm_nm.morandi.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.problem.dto.BojProblem;
import swm_nm.morandi.problem.dto.BojProblemRequestDto;
import swm_nm.morandi.test.dto.TestInputData;
import swm_nm.morandi.test.dto.TestTypeDto;
import swm_nm.morandi.member.service.MemberService;
import swm_nm.morandi.test.service.TestTypeService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {

    private final TestTypeService testTypeService;

    private final MemberService memberService;
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
    public ResponseEntity<List<BojProblem>> getTestInfo
            (@RequestBody BojProblemRequestDto bojProblemRequestDto) throws JsonProcessingException {
        Long memberId = bojProblemRequestDto.getMemberId();
        Long testTypeId = bojProblemRequestDto.getTestTypeId();
        String bojId = memberService.getBojId(memberId);
        List<BojProblem> bojProblems = testTypeService.getBojProblems(testTypeId, bojId);
        return new ResponseEntity<>(bojProblems, HttpStatus.OK);
    }

    @PostMapping("/tests/output")
    public ResponseEntity<String> getOutputResult
            (@RequestBody TestInputData testInputData) throws IOException, InterruptedException {
        String language = testInputData.getLanguage();
        String code = testInputData.getCode();
        String input = testInputData.getInput();
        String output = testTypeService.runCode(language, code, input);
        return ResponseEntity.ok(output);
    }
}
