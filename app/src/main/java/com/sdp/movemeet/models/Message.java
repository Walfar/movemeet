package com.sdp.movemeet.models;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents a message
 */
public class Message implements FirebaseObject {
    private String messageUser;
    private String messageText;
    private String messageUserId;
    private long messageTime;
    private String imageUrl;
    private String documentPath;


    /**
     * Empty constructor for message
     */
    public Message() {
    }

    /**
     * Constructor for a new message
     *
     * @param messageUser   : full name of the user
     * @param messageText   : content of the message
     * @param messageUserId : id of the user
     * @param imageUrl      : send image
     */
    public Message(String messageUser, String messageText, String messageUserId, String imageUrl) {

        if (messageUser == null || messageUserId == null) {
            throw new IllegalArgumentException();
        }

        this.messageUser = messageUser;
        this.messageText = messageText;
        this.messageUserId = messageUserId;
        this.messageTime = new Date().getTime();
        this.imageUrl = imageUrl;
    }

    /**
     * @return the user's full name
     */
    public String getMessageUser() {
        return messageUser;
    }

    /**
     * @return the content of the message
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * @return the user's id
     */
    public String getMessageUserId() {
        return messageUserId;
    }

    /**
     * @return the time when the message is sent
     */
    public long getMessageTime() {
        return messageTime;
    }

    /**
     * @return the url of the image to send
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param messageUser set the user's full name
     */
    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    /**
     * @param messageText set the content of the message
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    /**
     * @param messageUserId set the user's id
     */
    public void setMessageUserId(String messageUserId) {
        this.messageUserId = messageUserId;
    }


    public String getDocumentPath() {
        return this.documentPath;
    }

    public String setDocumentPath(String path) {
        if (documentPath == null) documentPath = path;
        return documentPath;
    }

}