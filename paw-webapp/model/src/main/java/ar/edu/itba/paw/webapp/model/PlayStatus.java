package ar.edu.itba.paw.webapp.model;

/**
 * Enum representing all recognized play status that a user can register for a game. Note that there is no 'unplayed'
 * status.
 */
public enum PlayStatus {
    PLAN_TO_PLAY,
    PLAYING,
    PLAYED,
    NO_PLAY_STATUS;

    public String asInDatabase() {
        return super.toString();
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase().replace("_", "-");
    }

    public static PlayStatus fromString(String value) {
        return valueOf(value.replace("-", "_").toUpperCase());
    }
}
