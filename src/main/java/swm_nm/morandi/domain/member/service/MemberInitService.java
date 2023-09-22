package swm_nm.morandi.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.member.dto.RegisterInfoDto;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.AuthErrorCode;
import swm_nm.morandi.global.utils.SecurityUtils;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberInitService {

    private final MemberRepository memberRepository;

    @Transactional
    public RegisterInfoDto memberInitialize(RegisterInfoDto registerInfoDto) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new MorandiException(AuthErrorCode.MEMBER_NOT_FOUND));
        member.setBojId(registerInfoDto.getBojId());
        return registerInfoDto;
    }
}
