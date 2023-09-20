package swm_nm.morandi.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.member.dto.MemberInfoDto;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.MemberErrorCode;
import swm_nm.morandi.global.utils.SecurityUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberInfoService {

    private final MemberRepository memberRepository;
    public MemberInfoDto getMemberInfo() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setIntroduceInfo(member.getIntroduceInfo());
        memberInfoDto.setBojId(member.getBojId());
        return memberInfoDto;
    }
}
