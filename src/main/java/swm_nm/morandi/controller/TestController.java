package swm_nm.morandi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.dto.BojProblem;
import swm_nm.morandi.dto.BojProblemRequestDto;
import swm_nm.morandi.dto.TestTypeDto;
import swm_nm.morandi.service.MemberService;
import swm_nm.morandi.service.TestTypeService;

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
}
