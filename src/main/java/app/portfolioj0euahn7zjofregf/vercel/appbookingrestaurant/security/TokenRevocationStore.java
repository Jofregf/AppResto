package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TokenRevocationStore {
    private Set<String> revokedTokens = new HashSet<>();

    public void revokeToken(String token) {
        revokedTokens.add(token);
    }

    public boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }
}
