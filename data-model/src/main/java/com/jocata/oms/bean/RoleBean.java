package com.jocata.oms.bean;

import java.util.List;

public class RoleBean {

    private Integer roleId;

    private String roleName;

    private List<PermissionBean> permissions;

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

    public List<PermissionBean> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionBean> permissions) {
        this.permissions = permissions;
    }
}
