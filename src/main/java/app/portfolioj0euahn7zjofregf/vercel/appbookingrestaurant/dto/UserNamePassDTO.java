package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

public class UserNamePassDTO {

    private String usernameOrEmail;

    public UserNamePassDTO() {
    }

    public UserNamePassDTO(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
}
