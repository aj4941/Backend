package swm_nm.morandi.testMemberTempCode.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.testMemberTempCode.dto.TempCodeDto;
import swm_nm.morandi.testMemberTempCode.entity.TempCode;
import swm_nm.morandi.testMemberTempCode.service.TempCodeService;

@RestController
@RequiredArgsConstructor

public class TempCodeController {

    private final TempCodeService tempCodeService;

    @PostMapping("/code")
    @Operation(summary = "테스트 중인 코드를 저장합니다", description = "테스트 중인 코드를 저장\n" +
            "testId와 attemptProblemId를 통해 테스트 중인 코드를 지속적으로 저장함\n")
    public void saveTempCode(@RequestBody TempCodeDto tempCodeDto) {
        tempCodeService.saveTempCode(tempCodeDto);

    }

    @GetMapping("/code")
    @Operation(summary = "저장된 코드를 확인합니다", description = "테스트 중일 때, 문제 번호를 바꿀 때 코드 정보를\n" +
            "testId와 attemptProblemId를 이용하여 가져온다. \n")
    public ResponseEntity<TempCode> getTempCode(@RequestParam String testId,
                                                @RequestParam String attemptProblemId){

        String key =String.format("tests:%s:problems:%s",testId, attemptProblemId);

        return new ResponseEntity<>(tempCodeService.getTempCode(key), HttpStatus.OK);

    }


}
