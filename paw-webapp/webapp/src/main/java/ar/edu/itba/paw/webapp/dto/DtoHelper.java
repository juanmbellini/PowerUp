package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.PlayStatus;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Class implementing logic to be shared among all DTOs.
 */
/* package */ class DtoHelper {


    /**
     * {@link JsonDeserializer} used to deserialize {@link PlayStatus}.
     */
    /* package */ final static class PlayStatusEnumDeserializer extends JsonDeserializer<PlayStatus> {

        @Override
        public PlayStatus deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return PlayStatus.fromString(p.getText());
        }
    }


}
