package com.jocata.oms.entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "roles")
public class RoleDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    private String roleName;


    @ManyToMany(mappedBy = "roles")
    private List<UserDetails> users;

    @ManyToMany
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<PermissionDetails> permissions;

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

    public List<UserDetails> getUsers()  {
        return users;
    }

    public void setUsers(List<UserDetails> users) {
        this.users = users;
    }

    public List<PermissionDetails> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDetails> permissions) {
        this.permissions = permissions;
    }
}
