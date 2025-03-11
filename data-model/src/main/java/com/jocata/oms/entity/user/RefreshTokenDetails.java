package com.jocata.oms.entity.user;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table( name = "refresh_tokens" )
public class RefreshTokenDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false )
    private UserDetails user;

    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Integer getTokenId() {
        return tokenId;
    }

    public void setTokenId(Integer tokenId) {
        this.tokenId = tokenId;
    }

    public UserDetails getUser() {
        return user;
    }

    public void setUser(UserDetails user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
