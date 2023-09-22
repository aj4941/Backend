package swm_nm.morandi.domain.member.service;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.member.dto.MemberInfoDto;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@WithMockUser(username = "1", roles = "USER")
class MemberInfoServiceTest {

    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private MemberRepository memberRepository;
    @Test
    @Transactional
    void getMemberInfo() {
        // given
        Optional<Member> result = memberRepository.findById(1L);
        Member member = result.get();

        // when
        MemberInfoDto memberInfo = memberInfoService.getMemberInfo();

        // then
        assertThat(memberInfo.getBojId()).isEqualTo(member.getBojId());
    }
}