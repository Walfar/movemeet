package com.sdp.movemeet.chat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String messageUser;
    private String messageText;
    private String messageUserId;
    private long messageTime;
    private String imageUrl;

    public Message() {
    }

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

    public String getMessageUser() {
        return messageUser;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageUserId() {
        return messageUserId;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setMessageUserId(String messageUserId) {
        this.messageUserId = messageUserId;
    }
}