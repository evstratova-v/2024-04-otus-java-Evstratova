package ru.otus.project.coffee.auth.security;

import java.util.Collection;
import java.util.Set;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.project.coffee.auth.model.User;

@SuppressWarnings("java:S1948")
public class CustomUserDetails implements UserDetails {

    @Getter
    private final User user;

    private final Set<GrantedAuthority> authorities;

    public CustomUserDetails(User user, Set<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }
}
