//package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities;
//
//import jakarta.persistence.*;
//import org.hibernate.annotations.GenericGenerator;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Entity
//@Table(name = "roles")
//public class RoleModel {
//
//    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
//    @Column(name = "role_id", updatable = false, columnDefinition = "VARCHAR(50)")
//    private String roleId;
//
//    @Column(name = "role_name", unique = true, updatable = false, nullable = false, columnDefinition = "VARCHAR(20)")
//    private String roleName;
//
//    @ManyToMany(mappedBy = "roles")
//    private Set<UserModel> users = new HashSet<>();
//
//    public RoleModel() {
//    }
//
//    public RoleModel(String roleId, String roleName, Set<UserModel> users) {
//        this.roleId = roleId;
//        this.roleName = roleName;
//    }
//
//    public String getRoleId() {
//        return roleId;
//    }
//
//    public void setRoleId(String roleId) {
//        this.roleId = roleId;
//    }
//
//    public String getRoleName() {
//        return roleName;
//    }
//
//    public void setRoleName(String roleName) {
//        this.roleName = roleName;
//    }
//
//    public Set<UserModel> getUsers() {
//        return users;
//    }
//
//    public void setUsers(Set<UserModel> users) {
//        this.users = users;
//    }
//
//    @Override
//    public String toString() {
//        return "RoleModel{" +
//                "roleId='" + roleId + '\'' +
//                ", roleName='" + roleName + '\'' +
//                '}';
//    }
//}
