package swm_nm.morandi.domain.testInfo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testRecord.dto.TestRecordDto;
import swm_nm.morandi.domain.testExit.entity.Tests;
import swm_nm.morandi.domain.testRecord.mapper.TestRecordMapper;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LatestTestInfoService {

    private final TestRepository testRepository;

    public List<TestRecordDto> getTestRecordDtosLatest() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Pageable pageable = PageRequest.of(0, 5);
        List<Tests> recentTests = testRepository.findAllByMember_MemberIdOrderByTestDateDesc(memberId, pageable);
        List<TestRecordDto> testRecordDtos = recentTests.stream().map(TestRecordMapper::convertToDto).collect(Collectors.toList());
        return testRecordDtos;
    }
}
