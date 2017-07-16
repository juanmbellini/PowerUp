package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model_wrappers.UserWithFollowCountsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService us;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        return Optional.ofNullable(us.findByUsername(username))
                .map(UserWithFollowCountsWrapper::getUser)
                .map(user -> new org.springframework.security.core.userdetails.User(username, user.getHashedPassword(),
                        user.getAuthorities().stream().map(Enum::name).map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())))
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " does not exist"));
    }
}
