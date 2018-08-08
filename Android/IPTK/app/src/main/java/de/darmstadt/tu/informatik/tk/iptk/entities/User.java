package de.darmstadt.tu.informatik.tk.iptk.entities;

/**
 * The type User.
 */
public class User {
    private String email;
    private String userPicture;
    private String userName;
    private boolean hasLoggedIn;


    /**
     * Instantiates a new User.
     */
    public User() {
    }


    /**
     * Instantiates a new User.
     *
     * @param email       the email
     * @param userPicture the user picture
     * @param userName    the user name
     * @param hasLoggedIn the has logged in
     */
    public User(String email, String userPicture, String userName, boolean hasLoggedIn) {
        this.email = email;
        this.userPicture = userPicture;
        this.userName = userName;
        this.hasLoggedIn = hasLoggedIn;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets user picture.
     *
     * @return the user picture
     */
    public String getUserPicture() {
        return userPicture;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Is has logged in boolean.
     *
     * @return the boolean
     */
    public boolean isHasLoggedIn() {
        return hasLoggedIn;
    }
}
