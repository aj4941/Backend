package swm_nm.morandi.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import swm_nm.morandi.auth.security.AuthDetails;
import swm_nm.morandi.exception.errorcode.AuthErrorCode;
import swm_nm.morandi.exception.MorandiException;
import swm_nm.morandi.member.entity.Member;
import swm_nm.morandi.member.repository.MemberRepository;

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