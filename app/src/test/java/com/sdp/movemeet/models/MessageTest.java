package com.sdp.movemeet.models;

import org.junit.Test;


import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    private String DUMMY_USER = "user";
    private String DUMMY_TEXT = "text";
    private String DUMMY_USERID = "userId";

    @Test(expected = IllegalArgumentException.class)
    public void messageConstructorFailUserNull(){
        Message message = new Message(null, DUMMY_TEXT, DUMMY_USERID, null /* no image */, Long.toString(new Date().getTime()));
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void messageConstructorFailTextNull(){
//        Message message = new Message(DUMMY_USER, null, DUMMY_USERID, null /* no image */);
//    }

    @Test(expected = IllegalArgumentException.class)
    public void messageConstructorFailUserIdNull(){
        Message message = new Message(DUMMY_USER, DUMMY_TEXT, null, null /* no image */, Long.toString(new Date().getTime()));
    }

    @Test
    public void messageGetCorrect(){

        Message message = new Message(DUMMY_USER, DUMMY_TEXT, DUMMY_USERID, null /* no image */, Long.toString(new Date().getTime()));
        assertEquals(DUMMY_USER, message.getMessageUser());
        assertEquals(DUMMY_TEXT, message.getMessageText());
        assertEquals(DUMMY_USERID, message.getMessageUserId());
    }

    @Test
    public void messageSetUser(){
        Message message = new Message(DUMMY_USER, DUMMY_TEXT, DUMMY_USERID, null /* no image */, Long.toString(new Date().getTime()));
        message.setMessageUser("Bob");
        assertEquals(message.getMessageUser(), "Bob");
    }

    @Test
    public void messageSetText(){
        Message message = new Message(DUMMY_USER, DUMMY_TEXT, DUMMY_USERID, null /* no image */, Long.toString(new Date().getTime()));
        message.setMessageText("Bob");
        assertEquals(message.getMessageText(), "Bob");
    }

    @Test
    public void messageSetUserId(){
        Message message = new Message(DUMMY_USER, DUMMY_TEXT, DUMMY_USERID, null /* no image */, Long.toString(new Date().getTime()));
        message.setMessageUserId("Bob");
        assertEquals(message.getMessageUserId(), "Bob");
    }

}
