package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.interfaces.SessionService;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model_wrappers.UserWithFollowCountsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Spring-based implementation of the {@link SessionService session service}.
 */
@Service
public class SessionServiceImpl implements SessionService {

    private final UserService userService;

    private User currentUser;

    @Autowired
    public SessionServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isLoggedIn() {
        //Thanks http://stackoverflow.com/a/12372555
        Authentication auth = getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    @Override
    public long getCurrentUserId() {
        return isLoggedIn() ? getCurrentUser().getId() : -1;
    }

    @Override
    public String getCurrentUsername() {
        //noinspection ConstantConditions, isLoggedIn => authentication is not null
        return isLoggedIn() ? getAuthentication().getName() : null;
    }

    @Override
    public User getCurrentUser() {
        String username = getCurrentUsername();
        if (username == null) {
            return null;
        }
        // Avoid unnecessary extra DB calls by caching the current user.
        if (currentUser == null || !currentUser.getUsername().equals(username)) {
            currentUser = Optional.ofNullable(userService.findByUsername(username))
                    .map(UserWithFollowCountsWrapper::getUser)
                    .orElse(null);
        }
        return currentUser;
    }

    /**
     * Gets the current Spring {@link Authentication}, to validate if a user is logged in, get the current user's
     * username, etc.
     *
     * @return The current context's authentication.
     */
    private Authentication getAuthentication() {
        SecurityContext sc = SecurityContextHolder.getContext();
        return sc == null ? null : sc.getAuthentication();
    }
}
