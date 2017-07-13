package ar.edu.itba.paw.webapp.model;

// TODO: move to interfaces
public enum OrderCategory {
    NAME {
        @Override
        public String getFieldName() {
            return toString();
        }
    },
    AVG_SCORE {
        @Override
        public String getFieldName() {
            return "avgScore";
        }
    },
    RELEASE {
        @Override
        public String getFieldName() {
            return "releaseDate";
        }
    };


    /**
     * Returns the "sorting by" field name.
     *
     * @return The name.
     */
    abstract public String getFieldName();


    @Override
    public String toString() {
        return super.toString().toLowerCase().replace("_","-");
    }

    /**
     * Creates an enum from the given {@code name} (can be upper, lower or any case)
     *
     * @param name The value of the enum as a string.
     * @return The enum value.
     */
    public static OrderCategory fromString(String name) {
        return valueOf(name.replace("-","_").toUpperCase());
    }
}

