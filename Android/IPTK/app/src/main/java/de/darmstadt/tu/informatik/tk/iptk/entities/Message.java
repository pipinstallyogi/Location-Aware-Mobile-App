package de.darmstadt.tu.informatik.tk.iptk.entities;

/**
 * The type Message.
 */
public class Message {
    private String messageId;
    private String messageText;
    private String messageSenderEmail;
    private String messageSenderPicture;

    /**
     * Instantiates a new Message.
     */
    public Message() {
    }

    /**
     * Instantiates a new Message.
     *
     * @param messageId            the message id
     * @param messageText          the message text
     * @param messageSenderEmail   the message sender email
     * @param messageSenderPicture the message sender picture
     */
    public Message(String messageId, String messageText, String messageSenderEmail, String messageSenderPicture) {
        this.messageId = messageId;
        this.messageText = messageText;
        this.messageSenderEmail = messageSenderEmail;
        this.messageSenderPicture = messageSenderPicture;
    }

    /**
     * Gets message id.
     *
     * @return the message id
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Gets message text.
     *
     * @return the message text
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * Gets message sender email.
     *
     * @return the message sender email
     */
    public String getMessageSenderEmail() {
        return messageSenderEmail;
    }

    /**
     * Gets message sender picture.
     *
     * @return the message sender picture
     */
    public String getMessageSenderPicture() {
        return messageSenderPicture;
    }
}
