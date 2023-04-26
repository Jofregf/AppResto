package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security;

public class JwtAuthResponseDTO {

    private String accessToken;
    private String tokenType = "Bearer";

    private String msg;

    public JwtAuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    public JwtAuthResponseDTO(String accessToken, String tokenType, String msg) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.msg = msg;
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
}
