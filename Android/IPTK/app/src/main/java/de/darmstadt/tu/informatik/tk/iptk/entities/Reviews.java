package de.darmstadt.tu.informatik.tk.iptk.entities;

/**
 * Created by aditya on 8/29/17.
 */
public class Reviews {

    private String email;
    private String latestReviewDate;
    private String review;
    private String UserName;
    private String userPicture;

    /**
     * Instantiates a new Reviews.
     */
    public Reviews() {
    }

    /**
     * Instantiates a new Reviews.
     *
     * @param email            the email
     * @param latestReviewDate the latest review date
     * @param review           the review
     * @param userName         the user name
     * @param userPicture      the user picture
     */
    public Reviews(String email, String latestReviewDate, String review, String userName, String userPicture) {
        this.email = email;
        this.latestReviewDate = latestReviewDate;
        this.review = review;
        UserName = userName;
        this.userPicture = userPicture;
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
     * Gets latest review date.
     *
     * @return the latest review date
     */
    public String getLatestReviewDate() {
        return latestReviewDate;
    }

    /**
     * Gets review.
     *
     * @return the review
     */
    public String getReview() {
        return review;
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
     * Gets user picture.
     *
     * @return the user picture
     */
    public String getUserPicture() {
        return userPicture;
    }
}
