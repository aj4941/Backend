package swm_nm.morandi.domain.auth.constants;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class SecurityConstants {

    //임시로
    public static final long JWT_EXPIRATION = 600000000;
    public static final long REFRESH_TOKEN_EXPIRATION = 36000000;
    public static final Key JWT_KEY = new SecretKeySpec("r5QyAI4s7GFJh4Nn4bUJHfZfQ5ks9ZlIwE4QwdZNCm5wefMpNkZq4T7GWM5szMZa".getBytes(), "HmacSHA512");//Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static final Boolean FOR_DEVELOPER= true;

    public static final Boolean FOR_SERVICE= false;
}
