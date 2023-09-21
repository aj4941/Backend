package swm_nm.morandi.domain.member.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.member.dto.RegisterInfoDto;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@WithMockUser(username = "1", roles = "USER")
class MemberInitServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberInitService memberInitService;
    @Test
    void memberInitialize() {
        // given
        RegisterInfoDto registerInfoDto = RegisterInfoDto.builder()
                .bojId("aj4941")
                .build();

        // when
        memberInitService.memberInitialize(registerInfoDto);

        // then
        Optional<Member> result = memberRepository.findById(1L);
        Member findMember = result.get();
        assertThat(findMember.getBojId()).isEqualTo("aj4941");
    }
}
