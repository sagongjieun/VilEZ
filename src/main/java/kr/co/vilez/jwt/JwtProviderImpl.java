package kr.co.vilez.jwt;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

@Service
public class JwtProviderImpl implements JwtProvider {
    private String secretKey = "kr.co.vilez.secretKey.KimKimSagongParkParkSung";
    private static JwtProviderImpl instacne  = new JwtProviderImpl();

    public static JwtProviderImpl getInstance() {
        return instacne;
    }

    private long tokenValidTime = Duration.ofMinutes(5).toMillis();
    private long refreshTokenValidTime = Duration.ofMinutes(10).toMillis();

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(int userId, String userNickname) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("nickname", userNickname);
        claims.put("expire", new Date((now.getTime() + tokenValidTime)));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(int userId, String userNickname) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("nickname", userNickname);
        claims.put("expire", new Date((now.getTime() + refreshTokenValidTime)));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    public String createExpireToken(int userId, String userNickname) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("nickname", userNickname);
        claims.put("expire", new Date((now.getTime())));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public String createExpireRefreshToken(int userId, String userNickname) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("nickname", userNickname);
        claims.put("expire", new Date((now.getTime())));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUserId(String token) {
        return Jwts.parser().
                setSigningKey(secretKey).
                parseClaimsJws(token).
                getBody().
                get("userId").
                toString();
    }
    public String getUserNickname(String token) {
        return Jwts.parser().
                setSigningKey(secretKey).
                parseClaimsJws(token).
                getBody().
                get("nickname").
                toString();
    }
    public String getExp(String token) {
        try {
            return Jwts.parser().
                    setSigningKey(secretKey).
                    parseClaimsJws(token).
                    getBody().
                    get("expire").
                    toString();
        }catch (Exception e){
            return "jwt parse error";
        }
    }

    public boolean validateToken(String jwtToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(jwtToken)
                    .getBody();

            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("jwt 오류");
            return false;
        }
    }

}
