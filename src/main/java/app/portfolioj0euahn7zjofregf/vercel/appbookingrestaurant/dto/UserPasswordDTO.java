package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

import jakarta.validation.constraints.Size;

public class UserPasswordDTO {

    @Size(min = 6, message = "The password must have at least 6 characters")
    private String userPassword;

    public UserPasswordDTO() {}

    public UserPasswordDTO(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
