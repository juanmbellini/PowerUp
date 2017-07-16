package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.UserFollow;
import ar.edu.itba.paw.webapp.utilities.Page;

/**
 * Created by julian on 16/07/17.
 */
public interface UserFollowDao {

    Page<UserFollow> getUserFollowing(User user, int pageNumber, int pageSize,
                                      SortingType sortingType, SortDirection sortDirection);

    Page<UserFollow> getUserFollowedBy(User user, int pageNumber, int pageSize, SortingType sortingType, SortDirection sortDirection);

    /**
     * Finds a UserFollow made by a given User to a given User
     *
     * @param following The User that followed the User.
     * @param followed The followed User
     * @return The matching UserFollow, or {@code null} if not present.
     */
    UserFollow find(User following, User followed);

    /**
     * Checks whether a given user has followed a given user.
     *
     * @param following The User that followed the User.
     * @param followed The followed User
     * @return Whether the user has followed the specified thread.
     */
    boolean exists(User following, User followed);

    /**
     * Creates a new UserFollow (i.e likes a User by a given User).
     *
     * @param following The User that followed the User.
     * @param followed The followed User
     * @return The new UserFollow.
     */
    UserFollow create(User following, User followed);

    /**
     * Deletes the given UserFollow (i.e unlikes a User).
     *
     * @param UserFollow The UserFollow representing the User being followed.
     */
    void delete(UserFollow UserFollow);

    /**
     * Enum indicating the sorting type for the "get users following" method.
     */
    enum SortingType {
        ID {
            @Override
            public String getFieldName() {
                return "id";
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
            return super.toString().toLowerCase().replace("_", "-");
        }

        /**
         * Creates an enum from the given {@code name} (can be upper, lower or any case)
         *
         * @param name The value of the enum as a string.
         * @return The enum value.
         */
        public static UserFollowDao.SortingType fromString(String name) {
            return valueOf(name.replace("-", "_").toUpperCase());
        }
    }
}
