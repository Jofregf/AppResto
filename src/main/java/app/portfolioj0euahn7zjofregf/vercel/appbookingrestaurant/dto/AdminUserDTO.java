package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;


import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RoleModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserDTO {

    private String userName;
    private String firstName;
    private String lastName;
    private String userPhone;
    private String userEmail;
    private boolean enabled;
    private RoleModel role;

    public AdminUserDTO() {
    }

    public AdminUserDTO(String userName, String firstName, String lastName, String userPhone,
                        String userEmail, boolean enabled, RoleModel role) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.enabled = enabled;
        this.role = role;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public RoleModel getRole() {
        return role;
    }


}
