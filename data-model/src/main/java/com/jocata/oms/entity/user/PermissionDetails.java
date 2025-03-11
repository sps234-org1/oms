package com.jocata.oms.entity;


import jakarta.persistence.*;

import javax.management.relation.Role;
import java.util.List;

@Entity
@Table( name = "permissions")
public class PermissionDetails {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer permissionId;

    private String permissionName;

    @ManyToMany(mappedBy = "permissions")
    private List<RoleDetails> roles;

    public PermissionDetails() {    }

    public PermissionDetails(String permissionName) {
        this.permissionName = permissionName;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

}
