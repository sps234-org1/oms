package com.jocata.oms.bean;

import com.jocata.oms.entity.AddressDetails;
import com.jocata.oms.entity.UserRoleDetails;

import java.util.Date;
import java.util.List;

public class UserBean {

    private Integer userId;

    private String fullName;

    private String email;

    private String passwordHash;

    private String phone;

    private String profilePicture;

    private String otpSecret;

    private boolean smsEnabled;

    private boolean isActive;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private List<AddressDetails> addresses;

    private List<UserRoleDetails> roles;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getOtpSecret() {
        return otpSecret;
    }

    public void setOtpSecret(String otpSecret) {
        this.otpSecret = otpSecret;
    }

    public boolean isSmsEnabled() {
        return smsEnabled;
    }

    public void setSmsEnabled(boolean smsEnabled) {
        this.smsEnabled = smsEnabled;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<AddressDetails> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDetails> addresses) {
        this.addresses = addresses;
    }

    public List<UserRoleDetails> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoleDetails> roles) {
        this.roles = roles;
    }
}
