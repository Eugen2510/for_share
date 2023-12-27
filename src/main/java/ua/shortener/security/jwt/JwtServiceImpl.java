package ua.shortener.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService{


    private String jwtSecretKey = "984hg493gh0439rthr0429uruj2309yh937gc763fe87t3f89723gf";

    @Value("${jwt.lifetime}")
    private  int JWT_LIFETIME;
    @Override
    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> role = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("roles", role);

        Date issueDate = new Date();
        Date expiredDate = new Date(issueDate.getTime() + JWT_LIFETIME);

        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .issuedAt(issueDate)
                .expiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUserName(token);
        Date expirationDate = extractAllClaims(token).getExpiration();
        return (username.equals(userDetails.getUsername())) && (new Date().before(expirationDate));
    }

    private Claims extractAllClaims(String token){
        return Jwts.
                parser()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public List<String> getRole(String token){
        return extractAllClaims(token).get("roles", List.class);
    }
}
