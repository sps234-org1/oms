package com.jocata.oms.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class UserDetails {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer userId;

    private String fullName;

    private String email;

    private String passwordHash;

    private String phone;

    private String profilePicture;

    private String otpSecret;

    private boolean smsEnabled;

    private boolean isActive;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AddressDetails> addresses;

    @OneToMany( mappedBy="user", cascade = CascadeType.ALL )
    private List<UserRoleDetails> roles;

    public UserDetails() {    }

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
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
