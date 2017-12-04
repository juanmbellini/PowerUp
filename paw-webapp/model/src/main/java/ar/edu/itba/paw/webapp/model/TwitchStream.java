package ar.edu.itba.paw.webapp.model;

import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.time.ZonedDateTime;

/**
 * Class representing a Twitch stream.
 */
public class TwitchStream {

    /**
     * Base url for the src field.
     * Has the channel query param to fill with the channel name, and autoplay set to false.
     */
    private final static String SRC_BASE_URL = "http://player.twitch.tv/?channel={0}&autoplay=false";

    // ================ Stream stuff ================

    /**
     * The stream id.
     */
    private final long id;

    /**
     * Zoned date time at which the stream was created.
     */
    private final ZonedDateTime createdAt;

    /**
     * A template for getting a preview image, where width and height can be set.
     */
    private final String previewImageTemplate;

    /**
     * The amount of people watching the stream.
     */
    private final long viewers;


    // ================ Channel stuff ================

    /**
     * The name of the channel broadcasting the stream.
     */
    private final String channelName;

    /**
     * The channel status (i.e display name for the stream).
     */
    private final String channelStatus;

    /**
     * The url of the channel.
     */
    private final String channelUrl;

    /**
     * The amount of views the channel ever received.
     */
    private final long channelViews;

    /**
     * The amount of followers the channel has.
     */
    private final long channelFollowers;

    /**
     * The channel description.
     */
    private final String channelDescription;


    // ================ Embedded stuff ================

    /**
     * The url of the src video for embedding it.
     */
    private final String src;


    /**
     * @param id                   The stream id.
     * @param createdAt            Zoned date time at which the stream was created.
     * @param previewImageTemplate A template for getting a preview image, where width and height can be set.
     * @param viewers              The amount of people watching the stream.
     * @param channelName          The name of the channel broadcasting the stream.
     * @param channelStatus        The channel status (i.e display name for the stream).
     * @param channelUrl           The url of the channel.
     * @param channelViews         The amount of views the channel ever received.
     * @param channelFollowers     The amount of followers the channel has.
     * @param channelDescription   The channel description.
     */
    public TwitchStream(long id, ZonedDateTime createdAt, String previewImageTemplate, long viewers,
                        String channelName, String channelStatus, String channelUrl,
                        long channelViews, long channelFollowers, String channelDescription) {
        Assert.notNull(createdAt, "Must set a \"created\" at zoned date and time");
        Assert.notNull(previewImageTemplate, "Must set a preview image template");
        Assert.notNull(channelName, "Must set a channel name");
        Assert.notNull(channelStatus, "Must set a channel status");
        Assert.notNull(channelUrl, "Must set a channel url");
        Assert.notNull(channelDescription, "Must set a channel description");

        this.id = id;
        this.createdAt = createdAt;
        this.previewImageTemplate = previewImageTemplate;
        this.viewers = viewers;
        this.channelName = channelName;
        this.channelStatus = channelStatus;
        this.channelUrl = channelUrl;
        this.channelViews = channelViews;
        this.channelFollowers = channelFollowers;
        this.channelDescription = channelDescription;
        this.src = MessageFormat.format(SRC_BASE_URL, channelName);
    }

    /**
     * @return The stream id.
     */
    public long getId() {
        return id;
    }

    /**
     * @return Zoned date time at which the stream was created.
     */
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @return A template for getting a preview image, where width and height can be set.
     */
    public String getPreviewImageTemplate() {
        return previewImageTemplate;
    }

    /**
     * @return The amount of people watching the stream.
     */
    public long getViewers() {
        return viewers;
    }

    /**
     * @return The name of the channel broadcasting the stream.
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * @return The channel status (i.e display name for the stream).
     */
    public String getChannelStatus() {
        return channelStatus;
    }

    /**
     * @return The url of the channel.
     */
    public String getChannelUrl() {
        return channelUrl;
    }

    /**
     * @return The amount of views the channel ever received.
     */
    public long getChannelViews() {
        return channelViews;
    }

    /**
     * @return The amount of followers the channel has.
     */
    public long getChannelFollowers() {
        return channelFollowers;
    }

    /**
     * @return The channel description.
     */
    public String getChannelDescription() {
        return channelDescription;
    }

    /**
     * @return The url of the src video for embedding it.
     */
    public String getSrc() {
        return src;
    }
}
