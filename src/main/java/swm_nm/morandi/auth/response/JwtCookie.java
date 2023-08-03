package swm_nm.morandi.auth.response;

import javax.servlet.http.Cookie;

public class JwtCookie extends Cookie {

    private String sameSite;

    public JwtCookie(String name, String value) {
        super(name, value);
    }

    public void setSameSite(String sameSite) {
        this.sameSite = sameSite;
    }

    public String getHeaderValue() {
        String headerValue = super.toString();
        if (this.sameSite != null) {
            headerValue += "; SameSite=" + this.sameSite;
        }
        return headerValue;
    }
}