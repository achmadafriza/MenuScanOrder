package net.uqcloud.infs7202.project.auth.service;

import lombok.extern.log4j.Log4j2;
import net.uqcloud.infs7202.project.auth.repository.UserRepository;
import net.uqcloud.infs7202.project.auth.repository.model.AuthUser;
import net.uqcloud.infs7202.project.auth.repository.model.Menu;
import net.uqcloud.infs7202.project.auth.repository.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("userDetailsService")
@Log4j2
public class AuthUserDetailsService implements UserDetailsService {
    private UserRepository repository;

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = repository.findByEmailAndIsActiveTrue(username);
        log.info("User loaded: {}", user);
        if(Objects.isNull(user)) {
            throw new UsernameNotFoundException("User not found" + username);
        }

        return new User(user.getEmail(), user.getPassword(), generateGrantedAuthorities(user));
    }

    private List<GrantedAuthority> generateGrantedAuthorities(AuthUser user) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Role role = user.getRole();
        grantedAuthorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", role.getName())));

        for(Menu menu : role.getMenus()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(menu.getName()));
        }

        return grantedAuthorities;
    }
}