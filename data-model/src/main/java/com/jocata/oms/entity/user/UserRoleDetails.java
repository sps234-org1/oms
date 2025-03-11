package com.jocata.oms.entity.user;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table( name = "user_role" )
@IdClass( UserRoleId.class )
public class UserRoleDetails {

    @Id
    @ManyToOne
    @JoinColumn( name = "user_id" , nullable = false)
    private UserDetails user;

    @Id
    @ManyToOne
    @JoinColumn( name = "role_id" , nullable = false)
    private RoleDetails role;

    public UserRoleDetails() {}

    public UserRoleDetails(UserDetails user, RoleDetails role) {
        this.user = user;
        this.role = role;
    }

    public UserDetails getUser() {
        return user;
    }

    public void setUser(UserDetails user) {
        this.user = user;
    }

    public RoleDetails getRole() {
        return role;
    }

    public void setRole(RoleDetails role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleDetails that = (UserRoleDetails) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, role);
    }

}


class UserRoleId implements Serializable {

    private Integer user;

    private Integer role;

    public UserRoleId() {    }

    public UserRoleId(Integer user, Integer role) {
        this.user = user;
        this.role = role;
    }

    public Integer getUserId() {
        return user;
    }

    public void setUserId(Integer user) {
        this.user = user;
    }

    public Integer getRoleId() {
        return role;
    }

    public void setRoleId(Integer role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, role);
    }
}
