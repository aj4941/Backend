package swm_nm.morandi.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import swm_nm.morandi.auth.security.AuthDetails;
import swm_nm.morandi.exception.errorcode.AuthErrorCode;
import swm_nm.morandi.exception.MorandiException;
import swm_nm.morandi.member.domain.Member;
import swm_nm.morandi.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthUserDetailService implements UserDetailsService {



    private final MemberRepository memberRepository;
    @Override
    public AuthDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.parseLong(userId))
                .orElseThrow(()-> new MorandiException(AuthErrorCode.MEMBER_NOT_FOUND));

        //여기서 백준 id없으면 Throw 하기


        return new AuthDetails(member.getMemberId().toString());
    }
}