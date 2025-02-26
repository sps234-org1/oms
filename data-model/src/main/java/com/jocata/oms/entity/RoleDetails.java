package com.jocata.oms.entity;


import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class RoleDetails {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer roleId;

    private String roleName;

    @OneToMany( mappedBy = "role", cascade = CascadeType.ALL )
    private List<UserRoleDetails> users;

    @OneToMany( mappedBy = "role", cascade = CascadeType.ALL )
    private List<RolePermissionDetails> permissions;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<UserRoleDetails> getUsers() {
        return users;
    }

    public void setUsers(List<UserRoleDetails> users) {
        this.users = users;
    }

    public List<RolePermissionDetails> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<RolePermissionDetails> permissions) {
        this.permissions = permissions;
    }
}
