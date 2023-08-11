package swm_nm.morandi.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm_nm.morandi.common.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String email;
    private String nickname;
    private String bojId;
    private Long rating;
    private String thumbPhoto;
    private Long currentTestId;

    @Enumerated(EnumType.STRING)
    private SocialType socialInfo;
    public void setBojId(String bojId) {
        this.bojId = bojId;
    }
    public void editProfile(String nickname, String bojId) {
        this.nickname = nickname;
        this.bojId = bojId;
        // this.thumbPhoto = thumbPhoto;
    }
    public void setRating(Long rating) {
        this.rating = rating;
    }

    public void setCurrentTestId(Long currentTestId) {
        this.currentTestId = currentTestId;
    }
}
