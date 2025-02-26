package com.jocata.oms.entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table( name = "permissions")
public class PermissionDetails {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer permissionId;

    private String permissionName;

    @OneToMany(mappedBy = "permission")
    private List<RolePermissionDetails> roles ;

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
