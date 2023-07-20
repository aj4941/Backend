package swm_nm.morandi.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String email;
    private String nickname;
    private String bojId;
    private String thumbPhoto;
    private String socialInfo;

    public void editProfile(String nickname, String bojId, String thumbPhoto) {
        this.nickname = nickname;
        this.bojId = bojId;
        this.thumbPhoto = thumbPhoto;
    }
}
