package swm_nm.morandi.domain.auth.constants;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@Getter
public class SecurityConstants {


    private final PublicKey publicKey;

    private final Key privateKey;

    public final long JWT_EXPIRATION = 604800000;
    public final long REFRESH_TOKEN_EXPIRATION = 2592000000L ;


    public final Boolean FOR_DEVELOPER= true;

    public final Boolean FOR_SERVICE= false;

    public SecurityConstants(@Value("${security.publicKey}") String publicKey, @Value("${security.privateKey}") String privateKey) {
        this.publicKey = convertPEMToPublicKey(decoding(publicKey));
        this.privateKey = convertPEMToPrivateKey(decoding(privateKey));
    }

    private String decoding(String key) {
        byte[] decoded = Base64.getDecoder().decode(key);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    private Key convertPEMToPrivateKey(String pemPrivateKey) {
        String privateKeyPEM = pemPrivateKey.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return keyFactory.generatePrivate(keySpec);
        }
        catch(NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new RuntimeException(e);
        }
    }
    public PublicKey convertPEMToPublicKey(String pemPublicKey) {
        String publicKeyPEM = pemPublicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return keyFactory.generatePublic(keySpec);
        }
        catch(NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new RuntimeException(e);
        }
    }

}
