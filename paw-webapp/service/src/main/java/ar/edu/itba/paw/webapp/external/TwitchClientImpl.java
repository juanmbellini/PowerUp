package ar.edu.itba.paw.webapp.external;

import ar.edu.itba.paw.webapp.exceptions.ExternalServiceException;
import ar.edu.itba.paw.webapp.interfaces.TwitchClient;
import ar.edu.itba.paw.webapp.model.TwitchStream;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete implementation of the {@link TwitchClient}.
 */
@Component
public class TwitchClientImpl implements TwitchClient {

    /**
     * The {@link RestTemplate} to operate as an HTTP Client.
     */
    private final RestTemplate restTemplate;

    /**
     * The base url for the twitch API.
     */
    private final String baseUrl;

    /**
     * The Twitch API streams endpoint.
     */
    private final String streamsEndpoint;

    /**
     * The entity to be sent to get data from the Twitch API.
     */
    private final HttpEntity<String> requestEntity;

    @Autowired
    public TwitchClientImpl(RestTemplate restTemplate,
                            @Value("#{environment.getRequiredProperty(\"twitch.request.base-url\")}") String baseUrl,
                            @Value("#{environment.getRequiredProperty(\"twitch.request.streams-endpoint\")}") String streamsEndpoint,
                            @Value("#{environment.getRequiredProperty(\"twitch.request.content-type\")}") String contentType,
                            @Value("#{environment.getRequiredProperty(\"twitch.clientId\")}") String clientId) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.streamsEndpoint = streamsEndpoint;
        this.requestEntity = new HttpEntity<>("", createHeaders(contentType, clientId));
    }


    @Override
    public List<TwitchStream> getStreamsByName(String gameName) {
        final URI endpointUri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(streamsEndpoint)
                .queryParam("game", gameName)
                .build()
                .encode()
                .toUri();
        try {
            final ResponseEntity<StreamsCollectionResponse> response = restTemplate
                    .exchange(endpointUri, HttpMethod.GET, this.requestEntity, StreamsCollectionResponse.class);

            return fromStreamResponse(response.getBody());
        } catch (Throwable e) {
            throw new ExternalServiceException("Could not access the Twitch API", e);
        }
    }


    /**
     * Maps a {@link StreamsCollectionResponse} into a {@link List} of {@link TwitchStream}.
     *
     * @param response The {@link StreamsCollectionResponse} from where data will be taken.
     * @return The {@link List} of {@link TwitchStream}.
     */
    private static List<TwitchStream> fromStreamResponse(StreamsCollectionResponse response) {
        return response.streams.stream()
                .map(each -> new TwitchStream(each.id, each.createdAt, each.preview.template, each.viewers,
                        each.channel.name, each.channel.status, each.channel.url,
                        each.channel.views, each.channel.followers, each.channel.description))
                .collect(Collectors.toList());
    }

    /**
     * Creates an {@link HttpHeaders} instance.
     *
     * @param contentType The content type to be accepted by the request.
     * @param clientId    The client id to use to communicate with the Twitch API.
     * @return The created {@link HttpHeaders} instance.
     */
    private static HttpHeaders createHeaders(String contentType, String clientId) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", contentType);
        headers.add("Client-ID", clientId);

        return headers;
    }


    // ================ Classes to be used to map the response into an own model ================

    /**
     * A streams collection response entity.
     */
    private final static class StreamsCollectionResponse {

        @SuppressWarnings("unused")
        @JsonProperty(value = "_total", access = JsonProperty.Access.WRITE_ONLY)
        private int total;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private List<Stream> streams;
    }

    /**
     * A stream response entity.
     */
    private static final class Stream {
        @JsonProperty(value = "_id", access = JsonProperty.Access.WRITE_ONLY)
        private long id;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private Channel channel;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private Preview preview;

        @JsonProperty(value = "created_at", access = JsonProperty.Access.WRITE_ONLY)
        @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
        private ZonedDateTime createdAt;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private int viewers;
    }

    /**
     * A channel response entity.
     */
    private final static class Channel {

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String status;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String name;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String url;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private int views;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private int followers;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String description;
    }

    /**
     * A preview response entity.
     */
    private final static class Preview {
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String template;
    }

    // ================ Helper classes ================

    /**
     * A {@link com.fasterxml.jackson.databind.JsonDeserializer} to deserialize {@link ZonedDateTime}.
     */
    private static final class ZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

        /**
         * Private constructor.
         */
        private ZonedDateTimeDeserializer() {
            super(ZonedDateTime.class);
        }

        @Override
        public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException {

            final String zonedDateTimeString = p.getText();
            try {
                return ZonedDateTime.from(DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(zonedDateTimeString));
            } catch (DateTimeParseException e) {
                throw new RuntimeException("There was an error when deserializing date " + zonedDateTimeString, e);
            }
        }
    }
}
