package com.jocata.oms;

import com.jocata.oms.response.RoleBean;

import java.util.List;
import java.util.Set;

public class UserDetailsDTO {

    private String userName;
    private String password;

    Set<String> roles;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
