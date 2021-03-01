package com.tobdev.qywxthird.utils;

import com.tobdev.qywxthird.model.entity.QywxThirdUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JWTUtils {

    private static  final long EXPIRE = 60000*60*24*7;
    private static final String SECRET = "tobdev.com";

    private  static final String TOKEN_PREFIX = "tobdev";

    private static final String SUBJECT = "tobdev";

    public static String geneJsonWebToken(QywxThirdUser user){
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("corp_id",user.getCorpId())
                .claim("user_id",user.getUserId())
                .claim("user_type",user.getUserType())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256,SECRET).compact();
        token = TOKEN_PREFIX + token;
        return  token;
    }

    public static Claims checkJWT(String token){
        try{
            final Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX,"")).getBody();
            return  claims;
        }catch (Exception e){
            return  null;
        }
    }

}
