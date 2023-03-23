package com.example.framework.bo;

import com.example.models.entity.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityUserDetails implements UserDetails {
    private String username;
    private String password;
    private List<Permission> permissionList;

    public SecurityUserDetails(String username, String password, List<Permission> permissionList) {
        this.username = username;
        this.password = password;
        this.permissionList = permissionList;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permissionList.stream()
                .map(role ->new SimpleGrantedAuthority(role.getId()+":"+role.getName()))
                .collect(Collectors.toList());
       //return this.authorityList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getCurrentUsername(){
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
}
