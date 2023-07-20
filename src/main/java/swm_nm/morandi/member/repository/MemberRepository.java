package swm_nm.morandi.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
