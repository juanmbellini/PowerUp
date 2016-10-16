package ar.edu.itba.paw.webapp.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enum representing all recognized play status that a user can register for a game. Note that there is no 'unplayed'
 * status.
 */
public enum PlayStatus {
    PLAN_TO_PLAY,
    PLAYING,
    PLAYED;

    private final Pattern p = Pattern.compile(" (\\w)(\\w*)");       //Non-static because it's not initialized otherwise
    private final String pretty = toTitleCase();
    private static final String[] prettyValues = prettyValues();

    public String getPretty() {
        return pretty;
    }

    public static String[] getPrettyValues() {
        return prettyValues;
    }

    private String toTitleCase() {
        String preProcessed = (name().substring(0, 1) + name().substring(1).toLowerCase()).replaceAll("_", " ");
        Matcher m = p.matcher(preProcessed);
        StringBuffer buffer = new StringBuffer();
        while(m.find()) {
            m.appendReplacement(buffer, " " + m.group(1).toUpperCase() + m.group(2));
        }
        return buffer.toString().isEmpty() ? preProcessed : buffer.toString();
    }

    private static String[] prettyValues() {
        PlayStatus[] uglyValues = values();
        String[] result = new String[uglyValues.length];
        for (int i = 0; i < uglyValues.length; i++) {
            result[i] = uglyValues[i].getPretty();
        }
        return result;
    }
}
