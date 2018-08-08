package de.darmstadt.tu.informatik.tk.iptk.entities;

/**
 * Created by aditya on 8/21/17.
 */
public class Results {

    private String name;
    private String place_id;
    private String formatted_address;
    private String icon;

    /**
     * Instantiates a new Results.
     *
     * @param name              the name
     * @param place_id          the place id
     * @param formatted_address the formatted address
     * @param icon              the icon
     */
    public Results(String name, String place_id, String formatted_address,String icon) {
        this.name = name;
        this.place_id = place_id;
        this.formatted_address = formatted_address;
        this.icon = icon;
    }


    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets place id.
     *
     * @return the place id
     */
    public String getPlaceId() {
        return place_id;
    }

    /**
     * Gets formatted address.
     *
     * @return the formatted address
     */
    public String getFormatted_address() {
        return formatted_address;
    }

    /**
     * Gets icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }
}
