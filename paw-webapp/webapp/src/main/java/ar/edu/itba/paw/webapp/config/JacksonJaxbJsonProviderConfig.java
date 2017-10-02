package ar.edu.itba.paw.webapp.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Class used to configure the Jackson Json Provider.
 * <p>
 * Created by Juan Marcos Bellini on 18/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public class JacksonJaxbJsonProviderConfig extends ResourceConfig {


    public JacksonJaxbJsonProviderConfig() {
        final ObjectMapper om = new ObjectMapper();
        om.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
        om.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);

        // Allows enums be serialized/deserialized using toString method (instead of name)
        om.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        om.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);


        register(new JacksonJaxbJsonProvider(om, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
    }


}