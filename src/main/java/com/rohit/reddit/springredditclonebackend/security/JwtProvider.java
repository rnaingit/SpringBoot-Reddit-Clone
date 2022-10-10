package com.rohit.reddit.springredditclonebackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.*;

@Service
public class JwtProvider {
//    private KeyStore keyStore;

    private Key key;

    @PostConstruct
    public void init(){
//        try {
//            keyStore = keyStore.getInstance("JKS");
//            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
//            keyStore.load(resourceAsStream,"secret".toCharArray());
//        }catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e){
//            throw new SpringRedditException("Exception Occured while loading keyStore");
//        }
       key=Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(Authentication authentication){

        User principal = (User) authentication.getPrincipal();

        //using a symmetric encryption
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(key)
                .compact();
    }

//    private PrivateKey getPrivateKey(){
//        try{
//            return (PrivateKey) keyStore.getKey("springblog","secret".toCharArray());
//        }catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e)
//        {
//            throw new SpringRedditException("Exception Occurred while retrieving public key from keyStore");
//        }
//    }

    public boolean validateToken(String jwt){
        Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);
        return true;
    }

    public String getUserNameFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).
                parseClaimsJws(token).getBody();

        return claims.getSubject();
    }
}
