package de.darmstadt.tu.informatik.tk.iptk.utilities;

import java.util.HashMap;

import de.darmstadt.tu.informatik.tk.iptk.entities.User;

/**
 * The type Constants.
 */
public class Constants {

    /**
     * The constant IP_HOST.
     */
    public static final String IP_HOST = "https://nodeserverfs.herokuapp.com";

    /**
     * The constant USER_INFO_PREFERENCE.
     */
    public static final String USER_INFO_PREFERENCE = "USER_INFO_PREFERENCE";
    /**
     * The constant USER_EMAIL.
     */
    public static final String USER_EMAIL = "USER_EMAIL";
    /**
     * The constant USER_NAME.
     */
    public static final String USER_NAME = "USER_NAME";
    /**
     * The constant USER_PICTURE.
     */
    public static final String USER_PICTURE = "USER_PICTURE";


    /**
     * The constant FIRE_BASE_PATH_USERS.
     */
    public static final String FIRE_BASE_PATH_USERS = "users";
    /**
     * The constant FIRE_BASE_PATH_FRIEND_REQUEST_SENT.
     */
    public static final String FIRE_BASE_PATH_FRIEND_REQUEST_SENT = "friendRequestsSent";
    /**
     * The constant FIRE_BASE_PATH_FRIEND_REQUEST_RECIEVED.
     */
    public static final String FIRE_BASE_PATH_FRIEND_REQUEST_RECIEVED = "friendRequestRecieved";
    /**
     * The constant FIRE_BASE_PATH_USER_FRIENDS.
     */
    public static final String FIRE_BASE_PATH_USER_FRIENDS = "userFriends";
    /**
     * The constant FIRE_BASE_PATH_USER_TOKEN.
     */
    public static final String FIRE_BASE_PATH_USER_TOKEN = "userToken";
    /**
     * The constant FIRE_BASE_PATH_USER_MESSAGES.
     */
    public static final String FIRE_BASE_PATH_USER_MESSAGES = "userMessages";
    /**
     * The constant FIRE_BASE_PATH_USER_NEW_MESSAGES.
     */
    public static final String FIRE_BASE_PATH_USER_NEW_MESSAGES = "newUserMessages";
    /**
     * The constant FIRE_BASE_PATH_USER_CHAT_ROOMS.
     */
    public static final String FIRE_BASE_PATH_USER_CHAT_ROOMS = "userChatRooms";

    /**
     * The constant MAP_INFO_PREFERENCE.
     */
    public static final String MAP_INFO_PREFERENCE = "MAP_INFO_PREFERENCE";
    /**
     * The constant MAP_DATA.
     */
    public static final String MAP_DATA = "MAP_DATA";

    /**
     * The constant PLACE_INFO_PREFERENCE.
     */
    public static final String PLACE_INFO_PREFERENCE = "PLACE_INFO_PREFERENCE";

    /**
     * The constant PLACE_DATA.
     */
    public static final String PLACE_DATA ="PLACE_DATA";


    /**
     * Encode email string.
     *
     * @param email the email
     * @return the string
     */
    public static String encodeEmail(String email) {
        return email.replace(".", ",");
    }

    /**
     * Is included in map boolean.
     *
     * @param userHashMap the user hash map
     * @param user        the user
     * @return the boolean
     */
    public static boolean isIncludedInMap(HashMap<String, User> userHashMap, User user) {
        return userHashMap != null && userHashMap.size() != 0 &&
                userHashMap.containsKey(user.getEmail());
    }


}
