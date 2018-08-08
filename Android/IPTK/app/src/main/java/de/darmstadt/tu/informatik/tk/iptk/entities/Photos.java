package de.darmstadt.tu.informatik.tk.iptk.entities;

/**
 * Created by aditya on 8/28/17.
 */
public class Photos {
    private String photoUrl;

    /**
     * Instantiates a new Photos.
     */
    public Photos() {
    }

    /**
     * Instantiates a new Photos.
     *
     * @param photoUrl the photo url
     */
    public Photos(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * Gets photo url.
     *
     * @return the photo url
     */
    public String getPhotoUrl() {
        return photoUrl;
    }
}
