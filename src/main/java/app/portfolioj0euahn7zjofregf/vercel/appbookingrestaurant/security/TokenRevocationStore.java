package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TokenRevocationStore {
    private Set<String> revokedTokens = new HashSet<>();

    public void revokeToken(String token) {
        revokedTokens.add(token);
        System.out.println(revokedTokens + " SET");
    }

    public boolean isTokenRevoked(String token) {
        System.out.println(revokedTokens.contains(token) + " IS TOKEN REVOKED");
        return revokedTokens.contains(token);
    }
}
