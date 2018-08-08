package de.darmstadt.tu.informatik.tk.iptk.entities;

/**
 * The type Chat room.
 */
public class ChatRoom {
    private String friendPicture;
    private String friendName;
    private String friendEmail;
    private String lastMessage;
    private String lastMessageSenderEmail;
    private boolean lastMessageRead;
    private boolean sentLastMessage;


    /**
     * Instantiates a new Chat room.
     */
    public ChatRoom() {
    }


    /**
     * Instantiates a new Chat room.
     *
     * @param friendPicture          the friend picture
     * @param friendName             the friend name
     * @param friendEmail            the friend email
     * @param lastMessage            the last message
     * @param lastMessageSenderEmail the last message sender email
     * @param lastMessageRead        the last message read
     * @param sentLastMessage        the sent last message
     */
    public ChatRoom(String friendPicture, String friendName, String friendEmail, String lastMessage, String lastMessageSenderEmail, boolean lastMessageRead, boolean sentLastMessage) {
        this.friendPicture = friendPicture;
        this.friendName = friendName;
        this.friendEmail = friendEmail;
        this.lastMessage = lastMessage;
        this.lastMessageSenderEmail = lastMessageSenderEmail;
        this.lastMessageRead = lastMessageRead;
        this.sentLastMessage = sentLastMessage;
    }

    /**
     * Gets last message sender email.
     *
     * @return the last message sender email
     */
    public String getLastMessageSenderEmail() {
        return lastMessageSenderEmail;
    }

    /**
     * Gets friend picture.
     *
     * @return the friend picture
     */
    public String getFriendPicture() {
        return friendPicture;
    }

    /**
     * Gets friend name.
     *
     * @return the friend name
     */
    public String getFriendName() {
        return friendName;
    }

    /**
     * Gets friend email.
     *
     * @return the friend email
     */
    public String getFriendEmail() {
        return friendEmail;
    }

    /**
     * Gets last message.
     *
     * @return the last message
     */
    public String getLastMessage() {
        return lastMessage;
    }

    /**
     * Is last message read boolean.
     *
     * @return the boolean
     */
    public boolean isLastMessageRead() {
        return lastMessageRead;
    }

    /**
     * Is sent last message boolean.
     *
     * @return the boolean
     */
    public boolean isSentLastMessage() {
        return sentLastMessage;
    }
}
