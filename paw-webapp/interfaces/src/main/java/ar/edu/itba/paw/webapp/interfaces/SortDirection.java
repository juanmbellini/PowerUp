package ar.edu.itba.paw.webapp.interfaces;

/**
 * Enum indicating direction of sorting (i.e Ascending or Descending).
 * <p>
 * Created by Juan Marcos Bellini on 23/3/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public enum SortDirection {
    ASC {
        @Override
        public String getQLKeyword() {
            return "ASC";
        }
    }, DESC {
        @Override
        public String getQLKeyword() {
            return "DESC";
        }
    };

    /**
     * Creates an enum from the given {@code name} (can be upper, lower or any case)
     *
     * @param name The value of the enum as a string.
     * @return The enum value.
     */
    public static SortDirection fromString(String name) {
        return valueOf(name.toUpperCase());
    }

    /**
     * Gets the keyword to be used in the final query.
     *
     * @return
     */
    public abstract String getQLKeyword();
}
