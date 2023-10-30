package swm_nm.morandi.domain.testInfo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.testInfo.dto.TestTypeDto;
import swm_nm.morandi.domain.testInfo.entity.TestType;
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

    @Transactional(readOnly = true)
    public List<TestTypeDto> getPracticeTestTypeDtos() {
        List<Long> ids = LongStream.rangeClosed(1, 6).boxed().toList();
        List<TestType> testTypes = testTypeRepository.findAllById(ids);

        //검증 로직 추가
        if(testTypes.size() != ids.size())
            throw new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND);

        return testTypes.stream().map(TestTypeMapper::convertToDto).collect(Collectors.toList());
    }
}
