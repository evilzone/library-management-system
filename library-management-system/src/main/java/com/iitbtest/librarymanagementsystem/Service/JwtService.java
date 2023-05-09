package com.iitbtest.librarymanagementsystem.Service;

import com.iitbtest.librarymanagementsystem.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final static String SECRET = "2A462D4A614E645267556B58703272357538782F413F4428472B4B6250655368";
    public boolean isValidToken(String token, UserDetails userDetails) {
        return retrieveUsername(token).equals(userDetails.getUsername()) && !isExpiredToken(token);
    }
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .claim("role", ((User) userDetails).getRole().name())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 60 * 1000)) //10 hours from current time
                .signWith(SignatureAlgorithm.HS256, fetchSigningKey())
                .compact();
    }

    private boolean isExpiredToken(String token) {
        return retrieveExpiration(token).before(new Date());
    }
    public Claims retrieveAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(fetchSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private <T> T fetchClaim(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = retrieveAllClaims(token);
        return claimsTFunction.apply(claims);
    }
    private Date retrieveExpiration(String token) {
        return fetchClaim(token, Claims::getExpiration);
    }
    public String retrieveUsername(String token) {
        return fetchClaim(token, Claims::getSubject);
    }
    private byte[] fetchSigningKey() {
        byte[] bytes =  Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(bytes).getEncoded();
    }
}
