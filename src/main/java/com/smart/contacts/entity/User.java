package com.smart.contacts.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    private String userId;

    @Column(name = "user_name", nullable = false)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @Getter(AccessLevel.NONE)
    @JsonIgnore
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Column(length = 1000)
    @Size(max = 1000, message = "About section cannot exceed 1000 characters")
    private String about;

    @Column(length = 1000)
    @Size(max = 1000, message = "Profile picture URL cannot exceed 1000 characters")
    private String profilePic;

    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phoneNumber;

    @Getter(AccessLevel.NONE)
    private boolean enabled = false;

    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    @Enumerated(EnumType.STRING)
    private Providers provider = Providers.SELF;

    private String providerUserId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

    @JsonIgnore
    private String emailToken;

    // Methods from UserDetails interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @PrePersist
    public void prePersist() {
        if (this.enabled == false) {
            this.enabled = false;
        }
        if (this.roleList == null) {
            this.roleList = new ArrayList<>();
        }
    }
}
