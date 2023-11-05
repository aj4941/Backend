package swm_nm.morandi.domain.testRecord.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testRecord.dto.CurrentRatingResponse;
import swm_nm.morandi.domain.testRecord.dto.RatingGraphResponse;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.MemberErrorCode;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final TestRepository testRepository;
    private final MemberRepository memberRepository;

    //현재 레이팅 정보 반환
    @Transactional(readOnly = true)
    public CurrentRatingResponse getCurrentRatingDto() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));
        CurrentRatingResponse currentRatingDto = CurrentRatingResponse.builder()
                .rating(member.getRating())
                .build();
        return currentRatingDto;
    }

    // 1년동안의 레이팅 그래프 데이터를 가져옴
    @Transactional(readOnly = true)
    public List<RatingGraphResponse> getRatingGraphSinceOneYear(){
        Long memberId = SecurityUtils.getCurrentMemberId();
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        List<Tests> tests = testRepository.findAllTestsByMember_MemberIdAndTestStatusAndTestDateAfterOrderByTestDateAsc(memberId, TestStatus.COMPLETED, oneYearAgo);

        return tests.stream().map(test ->
                RatingGraphResponse.builder()
                        .testDate(test.getTestDate())
                        .testRating(test.getTestRating())
                        .testTypeName(test.getTestTypename())
                        .build())

                .collect(Collectors.toList());
    }
}
