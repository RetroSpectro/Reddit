package ru.mikesb.reddit.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.mikesb.reddit.exceptions.SpringRedditException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

import static io.jsonwebtoken.Jwts.parser;

@Service
public class JwtProvider {

    private KeyStore keyStore;


    @PostConstruct
    public void init() throws SpringRedditException {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "94cfpmky".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SpringRedditException("Exception occurred while loading keystore");
        }

    }

    public String generateToken(Authentication authentication) throws SpringRedditException {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
               .compact();
    }

    private PrivateKey getPrivateKey() throws SpringRedditException {
        try {
            return (PrivateKey) keyStore.getKey("springblog", "94cfpmky".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SpringRedditException("Exception occurred while retrieving public key from keystore");
        }
    }

    public boolean validateToken(String jwt) throws SpringRedditException {
        parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() throws SpringRedditException {
        try {
            return  keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e ) {
            throw new SpringRedditException("Exception occurred while retrieving public key from keystore");
        }
    }

    public String getUsernameFromJwt(String token) throws SpringRedditException {
        Claims claims = parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
