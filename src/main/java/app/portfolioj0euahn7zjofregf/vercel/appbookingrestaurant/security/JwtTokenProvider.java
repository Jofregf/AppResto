package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.RestoAppException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpiration;

    @Autowired
    private TokenRevocationStore tokenRevocationStore;

    public String generateToken(Authentication authentication){
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        String role = userPrincipal.getAuthorities().iterator().next().getAuthority();
        String username = authentication.getName();
        Date actualDate = new Date();
        Date expirationDate = new Date(actualDate.getTime() + jwtExpiration);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .claim("userId", userPrincipal.getUserId())
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        return token;
    }

    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getUserIdFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.get("userId").toString();
    }

    public String getUserRoleFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.get("role").toString();
    }

    public boolean validateToken(String token) {
        if (tokenRevocationStore.isTokenRevoked(token)) {
            System.out.println("es falso en validate token???");
            return false; // Token revocado, no es válido
        }
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }
        catch (SignatureException exception) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Invalid signature");
        }
        catch (MalformedJwtException exception) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Invalid token");
        }
        catch (ExpiredJwtException exception) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }
        catch (UnsupportedJwtException exception) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Unsupported token");
        }
        catch (IllegalArgumentException exception) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Empty claims");
        }
    }

}
