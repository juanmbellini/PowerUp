package ar.edu.itba.paw.webapp.model;

/**
 * Created by julian on 11/09/16.
 */
public class Filter {

    FilterCategory type;
    String name;

    public Filter(FilterCategory type, String name) {
        this.type = type;
        this.name = name;
    }

    public enum FilterCategory {
        PUBLISHERS,
        DEVELOPERS,
        GENRES,
        KEYWORDS,
//        PLATFORMS
        CONSOLES
    }

    public FilterCategory getType() {
        return type;
    }

    public void setType(FilterCategory type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
