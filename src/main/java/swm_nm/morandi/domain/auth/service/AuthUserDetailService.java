package swm_nm.morandi.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import swm_nm.morandi.config.security.AuthDetails;
import swm_nm.morandi.global.exception.errorcode.AuthErrorCode;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthUserDetailService implements UserDetailsService {



    private final MemberRepository memberRepository;
    @Override
    public AuthDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(()-> new MorandiException(AuthErrorCode.MEMBER_NOT_FOUND));

        return new AuthDetails(member.getMemberId().toString(),member.getBojId());
    }
}