package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is the base controller all other controllers should extend, as it implements methods that all controllers will
 * use. Some examples are {@code getCurrentUser} or {@code isLoggedIn}, which are methods that are necessary all across
 * the web app.
 */
public abstract class BaseController {

    /**
     * A user service to make user operations.
     */
    protected UserService userService;

    /**
     * A url creator, which mey be used by child controllers.
     */
    private UrlCreator urlCreator;

    /**
     * Logger, each controller has their own.
     */
    protected final Logger LOG;

    /**
     * Constructor
     *
     * @param userService The user service.
     */
    @Autowired
    public BaseController(UserService userService) {
        this.userService = userService;
        this.urlCreator = new UrlCreator();
        LOG = LoggerFactory.getLogger(getClass());
    }

    /**
     * Url creator getter.
     *
     * @return The url creator.
     */
    protected UrlCreator getUrlCreator() {
        return urlCreator;
    }

    /**
     * Checks whether there is a currently authenticated user.
     *
     * @return Whether a user is currently authenticated with Spring.
     */
    @ModelAttribute("isLoggedIn")
    protected boolean isLoggedIn() {
        //Thanks http://stackoverflow.com/a/12372555
        Authentication auth = getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof  AnonymousAuthenticationToken);
    }

    /**
     * Gets the currently authenticated user's username.
     *
     * @return The currently authenticated user's username.
     */
    @ModelAttribute("currentUsername")
    protected String getCurrentUsername() {
        //noinspection ConstantConditions, isLoggedIn => authentication is not null
        return isLoggedIn() ? getAuthentication().getName() : null;
    }

    /**
     * Gets the current user. <b>NOTE: </b>To check whether a user is currently logged in, use the less costly (and more
     * obvious) {@link #isLoggedIn()} method.
     *
     * @return The currently authenticated user, or {@code null} if none.
     */
    @ModelAttribute("currentUser")
    protected User getCurrentUser() {
        String username = getCurrentUsername();
        return username == null ? null : userService.findByUsername(username);
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

    /**
     * Helper class that creates different types of complex URLs to be sent to the view
     */
    static class UrlCreator {

        /**
         * Creates an URL for the '/search' page.
         *
         * @param name The value for the 'name' query string param.
         * @param filters The value for the 'filters' query string param.
         * @param orderCategory The value for the 'orderCategory' query string param.
         * @param orderBoolean The value for the 'orderBoolean' query string param.
         * @param pageSize The value for the 'pageSize' query string param.
         * @param pageNumber The value for the 'pageNumber' query string param.
         * @return
         */
        public String getSearchUrl(String name, String filters, String orderCategory, String orderBoolean, String pageSize, String pageNumber) {
            Map<String, String> params = new LinkedHashMap<>();
            params.put("name", name);
            params.put("filters", filters);
            params.put("orderCategory", orderCategory);
            params.put("orderBoolean", orderBoolean);
            params.put("pageSize", pageSize);
            params.put("pageNumber", pageNumber);
            return createUrl("/search", params);
        }

        /**
         * Method to create an URL given a {@code baseUrl} and a map of query string parameters - values.
         *
         * @param baseUrl The base URL.
         * @param params A map containing as keys the name of the query string parameters,
         *               and values the corresponding values for those parameters.
         * @return The complex URL.
         */
        private String createUrl(String baseUrl, Map<String, String> params) {
            return baseUrl + createUrl(params, params.keySet().iterator(), true);
        }

        /**
         * Recursive method to create a query string, given a map of parameters - values.
         *
         * @param params The map of parameters - values.
         * @param keysIterator The {@code params#keySet} iterator.
         * @param questionMark A flag that says if the question mark (or the ampersand) must be placed.
         * @return
         */
        private String createUrl(Map<String, String> params, Iterator<String> keysIterator, boolean questionMark) {
            if (!keysIterator.hasNext()) {
                return "";
            }
            String param = keysIterator.next();
            String value = params.get(param);
            String symbol = questionMark ? "?" : "&";
            String result = "";
            if (value != null && !value.equals("")) {
                try {
                    result = symbol + param + "=" + URLEncoder.encode(value, "UTF-8");
                    questionMark = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result + createUrl(params, keysIterator, questionMark);
        }
    }
}
