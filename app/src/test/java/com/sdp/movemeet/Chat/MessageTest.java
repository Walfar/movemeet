package com.sdp.movemeet.Chat;

import com.sdp.movemeet.chat.Message;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    private String DUMMY_USER = "user";
    private String DUMMY_TEXT = "text";
    private String DUMMY_USERID = "userId";

    @Test(expected = IllegalArgumentException.class)
    public void messageConstructorFailUserNull(){
        Message message = new Message(null, DUMMY_TEXT, DUMMY_USERID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void messageConstructorFailTextNull(){
        Message message = new Message(DUMMY_USER, null, DUMMY_USERID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void messageConstructorFailUserIdNull(){
        Message message = new Message(DUMMY_USER, DUMMY_TEXT, null);
    }

    @Test
    public void messageGetCorrect(){
        Message message = new Message(DUMMY_USER, DUMMY_TEXT, DUMMY_USERID);
        assertEquals(DUMMY_USER, message.getMessageUser());
        assertEquals(DUMMY_TEXT, message.getMessageText());
        assertEquals(DUMMY_USERID, message.getMessageUserId());
    }

    @Test
    public void messageSetUser(){
        Message message = new Message(DUMMY_USER, DUMMY_TEXT, DUMMY_USERID);
        message.setMessageUser("Bob");
        assertEquals(message.getMessageUser(), "Bob");
    }

    @Test
    public void messageSetText(){
        Message message = new Message(DUMMY_USER, DUMMY_TEXT, DUMMY_USERID);
        message.setMessageText("Bob");
        assertEquals(message.getMessageText(), "Bob");
    }

    @Test
    public void messageSetUserId(){
        Message message = new Message(DUMMY_USER, DUMMY_TEXT, DUMMY_USERID);
        message.setMessageUserId("Bob");
        assertEquals(message.getMessageUserId(), "Bob");
    }

}
