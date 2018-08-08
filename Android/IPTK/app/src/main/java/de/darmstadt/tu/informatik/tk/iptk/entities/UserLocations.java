package de.darmstadt.tu.informatik.tk.iptk.entities;

/**
 * Created by aditya on 8/27/17.
 */
public class UserLocations {
    private String email;
    private String uname;
    private Double lati;
    private Double longi;


    /**
     * Instantiates a new User locations.
     */
    public UserLocations() {
    }

    /**
     * Instantiates a new User locations.
     *
     * @param email the email
     * @param uname the uname
     * @param lati  the lati
     * @param longi the longi
     */
    public UserLocations(String email, String uname, Double lati, Double longi) {
        this.email = email;
        this.uname = uname;
        this.lati = lati;
        this.longi = longi;
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
     * Gets uname.
     *
     * @return the uname
     */
    public String getUname() {
        return uname;
    }

    /**
     * Gets lati.
     *
     * @return the lati
     */
    public Double getLati() {
        return lati;
    }

    /**
     * Gets longi.
     *
     * @return the longi
     */
    public Double getLongi() {
        return longi;
    }
}
