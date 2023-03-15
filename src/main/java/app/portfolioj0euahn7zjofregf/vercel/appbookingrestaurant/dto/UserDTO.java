package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

public class UserDTO {

    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String userPhone;
    private String userEmail;
    private String userPassword;
    private Boolean enabled;

    public UserDTO() {
    }

    public UserDTO(String userId, String userName, String firstName, String lastName, String userPhone,
                   String userEmail, String userPassword, Boolean enabled) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.enabled = enabled;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", name='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
