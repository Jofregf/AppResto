package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

    private String userId;
    @NotEmpty
    @Size(min = 3, message = "The username must have at least 3 characters")
    private String userName;

    @NotEmpty(message = "The first name cannot be empty.")
    private String firstName;

    @NotEmpty(message = "The last name cannot be empty.")
    private String lastName;

    @NotEmpty(message = "The number phone cannot be empty.")
    private String userPhone;

    @NotEmpty(message = "The email cannot be empty.")
    @Email
    private String userEmail;

    @NotEmpty(message = "The password cannot be empty.")
    @Size(min = 6, message = "The password must have at least 6 characters")
    private String userPassword;

    @NotNull
    private boolean enabled;

    public RegisterDTO() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
