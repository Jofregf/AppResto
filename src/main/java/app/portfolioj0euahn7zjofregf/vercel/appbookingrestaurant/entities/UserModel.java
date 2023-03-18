package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "user_id", columnDefinition = "VARCHAR(50)")
    private String userId;

    @Column(name = "user_name", unique = true, nullable = false, columnDefinition = "VARCHAR(50)")
    private String userName;

    @Column(name = "first_name", nullable = false, columnDefinition = "VARCHAR(50)")
    private String firstName;

    @Column(name = "last_name", nullable = false, columnDefinition = "VARCHAR(50)")
    private String lastName;

    @Column(name = "user_phone", nullable = false, columnDefinition = "VARCHAR(50)")
    private String userPhone;

    @Column(name = "user_email", nullable = false, unique = true, columnDefinition = "VARCHAR(50)")
    private String userEmail;

    @Column(name = "user_password", nullable = false, columnDefinition = "VARCHAR(50)")
    private String userPassword;

    @Column(name = "user_enabled", nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReviewModel> reviews = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RestaurantModel> restaurants = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<BookingModel> bookings = new HashSet<>();

    public UserModel() {
    }

    public UserModel(String userId, String userName, String firstName, String lastName, String userPhone,
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

    public void setFirstName(String name) {
        this.firstName = name;
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

//    public Set<RoleModel> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<RoleModel> roles) {
//        this.roles = roles;
//    }


    @Override
    public String toString() {
        return "UserModel{" +
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
