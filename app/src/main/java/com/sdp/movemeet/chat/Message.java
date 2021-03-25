package com.sdp.movemeet.chat;

import java.util.Date;

public class Message {
    private String messageUser;
    private String messageText;
    private String messageUserId;
    private long messageTime;

    public Message(String messageUser, String messageText, String messageUserId){
        if(messageUser == null || messageText == null || messageUserId == null){
            throw new IllegalArgumentException();
        }
        this.messageUser = messageUser;
        this.messageText = messageText;
        this.messageUserId = messageUserId;
        messageTime = new Date().getTime();
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
