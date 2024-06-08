package edu.bethlehem.user_service;

import java.time.LocalDateTime;
import java.util.Collection;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.JSONPObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

// @Entity
// @Table(name = "_user")

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonSerialize
@Builder
@Getter
@Setter
@Table(name = "_user") // Specify the custom table name here
public class AppUser implements UserDetailsImpl{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    private String firstName;

    private String lastName;

    private String username;

    @Column(unique = true)
    private String email;

    /*
     * Password pattern validation Will Not be held here because the password is
     * stored in the database in encrypted version
     * However The password vaildation will be in the RegisterRequest class
     */

    @JsonIgnore
    private String password;

    private String bio;

    private String phoneNumber;

    private String fieldOfWork;

    // @NotNull
    private Boolean locked;
    // @NotNull
    private Boolean enabled;

    @JdbcTypeCode(SqlTypes.JSON)
    private JSONPObject userSettings;

    // Academic Specific Fields
    private String badge;

    private String education;

    private String organizationName;

    private String OrganizationAddress;

    private String location;

    private Boolean verified;

    private String contactEmail;

    private String contactPhoneNumber;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
        return true;
    }

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    // return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    // }

    // @Override
    // public String getUsername() {
    // return email;
    // }

    // @Override
    // @JsonIgnore
    // public boolean isAccountNonExpired() {
    // return true;
    // }

    // @Override
    // // @JsonIgnore
    // public boolean isAccountNonLocked() {
    // return true;
    // }

    // @Override
    // @JsonIgnore
    // public boolean isCredentialsNonExpired() {
    // return true;
    // }

    // @Override
    // // @JsonIgnore
    // public boolean isEnabled() {
    // return true;
    // }

    // @Override
    // public Long getId() {
    // return id;
    // }

}