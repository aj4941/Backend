package swm_nm.morandi.domain.test.ongoingTest.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.domain.test.dto.TempCode;
import swm_nm.morandi.domain.test.dto.TempCodeDto;
import swm_nm.morandi.domain.test.ongoingTest.service.TempCodeService;

@RestController
@RequiredArgsConstructor
@Tag(name = "TempCodeController", description = "테스트 시행 중에 코드를 저장하는 컨트롤러")
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
                                                @RequestParam String problemNumber){

        String key =String.format("testId:%s:problemNumber:%s",testId, problemNumber);

        return new ResponseEntity<>(tempCodeService.getTempCode(key), HttpStatus.OK);

    }


}
