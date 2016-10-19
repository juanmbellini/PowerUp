package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.Authority;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.*;

import java.util.*;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService us;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = us.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " does not exist");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Authority authority : user.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(authority.name()));
        }
        return new org.springframework.security.core.userdetails.User(username, user.getHashedPassword(), authorities);
    }
}
