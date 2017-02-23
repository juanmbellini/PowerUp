package ar.edu.itba.paw.webapp.config;

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

    public static final Map<String, String> CORS_HEADERS = new HashMap<>();

    static {
        CORS_HEADERS.put("Access-Control-Allow-Origin", "http://localhost:9000");                                       //TODO remove in production
        CORS_HEADERS.put("Access-Control-Allow-Credentials", "true");                                                   //Allow sending cookies for authentication for cross-origin requests
        CORS_HEADERS.put("Access-Control-Allow-Headers", "X-AUTH-TOKEN, X-CSRF-TOKEN");                                 //TODO remove in production? I think we need this and the next line
        CORS_HEADERS.put("Access-Control-Expose-Headers", "X-AUTH-TOKEN, X-CSRF-HEADER, X-CSRF-PARAM, X-CSRF-TOKEN");
    }

    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        for (Map.Entry<String, String> header : CORS_HEADERS.entrySet()) {
            headers.add(header.getKey(), header.getValue());
        }
    }
}
