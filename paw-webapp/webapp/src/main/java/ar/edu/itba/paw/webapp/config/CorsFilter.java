package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Jersey filter to allow for cross-origin requests on local deploys (grunt serve by default deploys on port 9000
 * while Jersey runs on 8080). TODO find a way to not enable this filter on production
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    public static boolean isEnabled;

    @Autowired
    public CorsFilter(Environment environment) {
        isEnabled = environment.getProperty("cors.enabled", Boolean.class, false);
    }

    public static final Map<String, String> CORS_HEADERS = new HashMap<>();
    static {
        CORS_HEADERS.put("Access-Control-Allow-Origin", "http://localhost:9000");   //Allow access from grunt server
        CORS_HEADERS.put("Access-Control-Allow-Credentials", "true");               //Allow sending credentials in CORS
        CORS_HEADERS.put("Access-Control-Allow-Headers", "Authorization");          //Allow sending Authorization header for protected endpoints
        //Allow reading X-TOKEN header when logging in and all pagination headers when searching
        CORS_HEADERS.put("Access-Control-Expose-Headers", "Location, X-TOKEN, X-Total-Pages, X-Amount-Of-Elements, X-Overall-Amount-Of-Elements, X-Page-Number, X-Page-Size, X-Prev-Page-Url, X-Next-Page-Url, X-First-Page-Url, X-Last-Page-Url");
    }

    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if(isEnabled) {
            MultivaluedMap<String, Object> headers = responseContext.getHeaders();
            for (Map.Entry<String, String> header : CORS_HEADERS.entrySet()) {
                headers.add(header.getKey(), header.getValue());
            }
        }
    }
}
