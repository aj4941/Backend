package swm_nm.morandi.domain.testInfo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testInfo.dto.TestTypeDto;
import swm_nm.morandi.domain.testInfo.mapper.TestTypeMapper;
import swm_nm.morandi.domain.testInfo.repository.TestTypeRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestTypeErrorCode;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PracticeTestInfoService {

    private final TestTypeRepository testTypeRepository;
    public List<TestTypeDto> getPracticeTestTypeDtos() {
        List<TestTypeDto> testTypeDtos = LongStream.rangeClosed(1, 6).mapToObj(i -> testTypeRepository.findById(i)
                        .orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND)))
                .map(TestTypeMapper::convertToDto).collect(Collectors.toList());
        return testTypeDtos;
    }
}
