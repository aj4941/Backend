package swm_nm.morandi.auth.security;


import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class AuthDetails implements UserDetails {

    private String userId;
    private String bojId;
//    private Role role;


    public String getBojId() {
        return bojId;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
//        return Collections.singletonList(new SimpleGrantedAuthority(Role.MEMBER.getRole()));
      //  new SimpleGrantedAuthority(role.getRole());
    }
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId;

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
