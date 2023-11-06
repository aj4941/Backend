package swm_nm.morandi.domain.member.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.member.dto.MemberInfoRequest;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@WithMockUser(username = "1",  roles = "USER")
class MemberEditServiceTest {

    @Autowired
    private MemberEditService memberEditService;

    @Autowired
    private MemberRepository memberRepository;
    @Test
    @Transactional
    void editProfile() {
        // given
        MemberInfoRequest memberInfoDto = new MemberInfoRequest();
        memberInfoDto.setIntroduceInfo("hello world");
        memberInfoDto.setBojId("aj4941");

        // when
        memberEditService.editProfile(memberInfoDto.getIntroduceInfo(), memberInfoDto.getBojId());

        // then
        Optional<Member> result = memberRepository.findById(1L);
        Member findMember = result.get();

        assertThat(findMember.getIntroduceInfo()).isEqualTo("hello world");
        assertThat(findMember.getBojId()).isEqualTo("aj4941");
    }
}