package swm_nm.morandi.domain.testInfo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.testInfo.dto.TestTypeInfoResponse;
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
public class RandomDefenseInfoService {

    private final TestTypeRepository testTypeRepository;

    @Transactional(readOnly = true)
    public List<TestTypeInfoResponse> getRandomDefenseTestTypeDtos() {
        List<Long> ids = LongStream.rangeClosed(13, 15).boxed().toList();
        List<TestType> testTypes = testTypeRepository.findAllById(ids);

        //검증 로직 추가
        if(testTypes.size() != ids.size())
            throw new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND);

        return testTypes.stream()
                .map(TestTypeMapper::convertToDto)
                .collect(Collectors.toList());
    }
}
