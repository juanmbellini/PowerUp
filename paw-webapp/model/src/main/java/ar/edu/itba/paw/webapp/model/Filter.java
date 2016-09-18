package ar.edu.itba.paw.webapp.model;

/**
 * Models different types of criteria that games can be filtered by, such as genre, developers, publishers, etc.
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
        PLATFORMS
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
