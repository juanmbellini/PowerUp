package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.TwitchStream;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Data transfer object for twitch streams (i.e it wraps a {@link TwitchStream} in order to serialize it accordingly).
 */
public class TwitchStreamDto {

    /**
     * The wrapped {@link TwitchStream} from where data is taken.
     */
    private final TwitchStream twitchStream;

    /**
     * Constructor.
     *
     * @param twitchStream The wrapped {@link TwitchStream} from where data is taken.
     */
    public TwitchStreamDto(TwitchStream twitchStream) {
        this.twitchStream = twitchStream;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public long getId() {
        return twitchStream.getId();
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    public ZonedDateTime getCreatedAt() {
        return twitchStream.getCreatedAt();
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getPreviewImageTemplate() {
        return twitchStream.getPreviewImageTemplate();
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public long getViewers() {
        return twitchStream.getViewers();
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getChannelName() {
        return twitchStream.getChannelName();
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getChannelStatus() {
        return twitchStream.getChannelStatus();
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getChannelUrl() {
        return twitchStream.getChannelUrl();
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public long getChannelViews() {
        return twitchStream.getChannelViews();
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public long getChannelFollowers() {
        return twitchStream.getChannelFollowers();
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getChannelDescription() {
        return twitchStream.getChannelDescription();
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getSrc() {
        return twitchStream.getSrc();
    }

    /**
     * A {@link com.fasterxml.jackson.databind.JsonSerializer} to serialize {@link ZonedDateTime}.
     */
    private final static class ZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {

        /**
         * Private constructor.
         */
        private ZonedDateTimeSerializer() {
            super(ZonedDateTime.class);
        }


        @Override
        public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(value));
        }
    }
}
