package com.first_project.shopapp.components;

import com.first_project.shopapp.exceptions.InvalidParamException;
import com.first_project.shopapp.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {
    private static final long expiration=2592000;// don vi mili giay 30 ngay
    private static final String secret_key="6a9a1761e17aebfbcffe9f36f26c4f423c0b8a3f405533f9389f9ff0e715ff55";
    public String generateToken(User user) throws Exception{
        try{
            Map<String,Object> claims=new HashMap<>();
            claims.put("phoneNumber",user.getPhoneNumber());
            return Jwts.builder()
                    .setSubject(user.getPhoneNumber())
                    .setClaims(claims)
                    .setExpiration(new Date(System.currentTimeMillis()+expiration*1000L))
                    .signWith(getSecretKey(),SignatureAlgorithm.HS256)
                    .compact();
        }catch(Exception e){
            throw new InvalidParamException("Cannot create jwt token,error "+e.getMessage());
        }
    }
    private Claims extraAllClaims(String token){

        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String extraPhoneNumber(String token){
        return extraAllClaims(token).get("phoneNumber",String.class);
    }
    private Key getSecretKey(){
        byte[] byteKey= Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(byteKey);
    }


    //check expiration token
    public boolean checkExpiredToken(String token){
        Date expiredToken=extraAllClaims(token).getExpiration();
        return expiredToken.before(new Date());
    }
    public boolean validToken(String token, UserDetails userDetails){
        String phoneNumber=extraPhoneNumber(token);
        return phoneNumber.equals(userDetails.getUsername())
                &&!checkExpiredToken(token);
    }
}
