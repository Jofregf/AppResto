package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities;

import jakarta.persistence.*;
import org.apache.catalina.User;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class RoleModel {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "role_id", updatable = false, columnDefinition = "VARCHAR(50)")
    private String roleId;

    @Column(name = "role_name", unique = true, updatable = false, nullable = false, columnDefinition = "VARCHAR(20)")
    private String roleName;



    public RoleModel() {
    }

    public RoleModel(String roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public RoleModel(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
