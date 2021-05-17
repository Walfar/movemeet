package com.sdp.movemeet.models;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * This class represents a message
 *
 */
public class Message implements FirebaseObject {
    private String messageUser;
    private String messageText;
    private String messageUserId;
    private String messageTime;
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
     * @param messageTime   : time at which the message has been sent
     */
    public Message(String messageUser, String messageText, String messageUserId, String imageUrl, String messageTime) {

        if (messageUser == null || messageUserId == null) {
            throw new IllegalArgumentException();
        }

        this.messageUser = messageUser;
        this.messageText = messageText;
        this.messageUserId = messageUserId;
        this.imageUrl = imageUrl;
        this.messageTime = messageTime;
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
    public String getMessageTime() {
        return messageTime;
    }

    /**
     * @return the url of the image to send
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @return the document path of the message
     */
    public String getDocumentPath() {
        return this.documentPath;
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

    /**
     * @param path change activity's document path
     */
    public String setDocumentPath(String path) {
        if (documentPath == null) documentPath = path;
        return documentPath;
    }


}