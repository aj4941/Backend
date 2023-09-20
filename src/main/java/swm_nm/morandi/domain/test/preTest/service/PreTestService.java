package swm_nm.morandi.domain.test.preTest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.test.dto.TestRatingDto;
import swm_nm.morandi.domain.test.dto.TestRecordDto;
import swm_nm.morandi.domain.test.dto.TestStatus;
import swm_nm.morandi.domain.test.dto.TestTypeDto;
import swm_nm.morandi.domain.test.preTest.mapper.TestTypeMapper;
import swm_nm.morandi.domain.test.repository.TestRepository;
import swm_nm.morandi.global.utils.SecurityUtils;
import swm_nm.morandi.domain.member.dto.CurrentRatingDto;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.test.repository.AttemptProblemRepository;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.test.entity.Tests;
import swm_nm.morandi.domain.test.entity.TestType;
import swm_nm.morandi.domain.test.preTest.mapper.TestRecordMapper;
import swm_nm.morandi.domain.test.repository.TestTypeRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.MemberErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestErrorCode;
import swm_nm.morandi.global.exception.errorcode.TestTypeErrorCode;

import swm_nm.morandi.domain.test.entity.AttemptProblem;
import swm_nm.morandi.domain.test.postTest.request.AttemptProblemDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;


@Service
@RequiredArgsConstructor
public class PreTestService {

    private final TestRepository testRepository;

    private final TestTypeRepository testTypeRepository;

    public List<TestRecordDto> getTestRecordDtosLatest() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Pageable pageable = PageRequest.of(0, 5);
        List<Tests> recentTests = testRepository.findAllByMember_MemberIdOrderByTestDateDesc(memberId, pageable);
        List<TestRecordDto> testRecordDtos = recentTests.stream().map(TestRecordMapper::convertToDto).collect(Collectors.toList());
        return testRecordDtos;
    }
    public List<TestTypeDto> getPracticeTestTypeDtos() {
        List<TestTypeDto> testTypeDtos = LongStream.rangeClosed(1, 6).mapToObj(i -> testTypeRepository.findById(i)
                        .orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND)))
                .map(TestTypeMapper::convertToDto).collect(Collectors.toList());
        return testTypeDtos;
    }

    public List<TestTypeDto> getCompanyTestTypeDtos() {
        List<TestTypeDto> testTypeDtos = LongStream.rangeClosed(7, 12).mapToObj(i -> testTypeRepository.findById(i)
                        .orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND)))
                .map(TestTypeMapper::convertToDto).collect(Collectors.toList());
        return testTypeDtos;
    }

    public TestTypeDto getTestTypeDto(Long testTypeId) {
        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        TestTypeDto testTypeDto = TestTypeMapper.convertToDto(testType);
        return testTypeDto;
    }
}
