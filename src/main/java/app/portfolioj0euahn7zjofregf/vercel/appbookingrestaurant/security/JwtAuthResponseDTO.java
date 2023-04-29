package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security;

public class JwtAuthResponseDTO {

    private String accessToken;
    private String tokenType = "Bearer";
    private String msg;
    private String role;

    public JwtAuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    public JwtAuthResponseDTO(String accessToken, String tokenType, String msg, String role) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.msg = msg;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
