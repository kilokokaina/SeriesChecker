package com.serieschecker.SeriesChecker.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.serieschecker.SeriesChecker.view.UserView;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Data
@Entity
public class UserModel implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(UserView.IdUsernamePassword.class)
    private Long userId;

    @JsonView(UserView.IdUsernamePassword.class)
    private String username;

    @JsonView(UserView.IdUsernamePassword.class)
    private String password;

    private String loginIp;
    private LocalDateTime loginDate;

    private Long telegramBotChatId;

    @JsonView(UserView.FullData.class)
    private boolean status2FA;

    @JsonView(UserView.FullData.class)
    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @JsonView(UserView.FullData.class)
    private Set<Role> roleSet;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return getRoleSet(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return isActive(); }

    @Override
    public String toString() {
        return String.format("User=[" +
                "%d %s]", getUserId(), getUsername());
    }
}
