package com.jocata.oms.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table( name = "role_permissions")
public class RolePermissionDetails {

    @EmbeddedId
    private RolePermissionId id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @MapsId("roleId")
    private RoleDetails role;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    @MapsId("permissionId")
    private PermissionDetails permission;

    public RolePermissionDetails() {    }

    public RolePermissionDetails(RoleDetails role, PermissionDetails permission) {
        this.role = role;
        this.permission = permission;
        this.id = new RolePermissionId(role.getRoleId(), permission.getPermissionId());
    }

    private RolePermissionId getId() {
        return id;
    }

    private void setId(RolePermissionId id) {
        this.id = id;
    }

    public RoleDetails getRole() {
        return role;
    }

    public void setRole(RoleDetails role) {
        this.role = role;
    }

    public PermissionDetails getPermission() {
        return permission;
    }

    public void setPermission(PermissionDetails permission) {
        this.permission = permission;
    }
}


@Embeddable
class RolePermissionId {

    private Integer roleId;
    private Integer permissionId;

    public RolePermissionId() {}

    public RolePermissionId(Integer roleId, Integer permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePermissionId that = (RolePermissionId) o;
        return roleId.equals(that.roleId) && permissionId.equals(that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, permissionId);
    }

}
