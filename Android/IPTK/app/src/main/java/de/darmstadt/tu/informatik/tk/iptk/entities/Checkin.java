package de.darmstadt.tu.informatik.tk.iptk.entities;

/**
 * Created by aditya on 8/27/17.
 */
public class Checkin {

    private String UserPicture;
    private String UserName;
    private String email;
    private String latestCheckin;

    /**
     * Instantiates a new Checkin.
     */
    public Checkin() {
    }


    /**
     * Instantiates a new Checkin.
     *
     * @param userPicture   the user picture
     * @param userName      the user name
     * @param email         the email
     * @param latestCheckin the latest checkin
     */
    public Checkin(String userPicture, String userName, String email,String latestCheckin) {
        UserPicture = userPicture;
        UserName = userName;
        this.email = email;
        this.latestCheckin = latestCheckin;
    }

    /**
     * Gets user picture.
     *
     * @return the user picture
     */
    public String getUserPicture() {
        return UserPicture;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return UserName;
    }

    /**
     * Gets checkin time.
     *
     * @return the checkin time
     */
    public String getEmail() {
        return email;
    }


    /**
     * Gets latest checkin.
     *
     * @return the latest checkin
     */
    public String getLatestCheckin() {
        return latestCheckin;
    }
}
