package com.sdp.movemeet.backend.serialization;

import com.sdp.movemeet.models.Message;

import java.util.HashMap;
import java.util.Map;

public class MessageSerializer implements BackendSerializer<Message> {

    // The key used to access the messageUser attribute of a serialized Message
    public static final String MESSAGE_USER_KEY = "messageUser";
    // The key used to access the messageText attribute of a serialized Message
    public static final String MESSAGE_TEXT_KEY = "messageText";
    // The key used to access the messageUserId attribute of a serialized Message
    public static final String MESSAGE_USER_ID_KEY = "messageUserId";
    // The key used to access the imageUrl attribute of a serialized Message
    public static final String MESSAGE_IMAGE_URL_KEY = "imageUrl";

    @Override
    public Message deserialize(Map<String, Object> data) {

        Message message = new Message (
                (String) data.get(MESSAGE_USER_KEY),

                (String) data.get(MESSAGE_TEXT_KEY),

                (String) data.get(MESSAGE_USER_ID_KEY),

                (String) data.get(MESSAGE_IMAGE_URL_KEY)
        );

        return message;
    }


    @Override
    public Map<String, Object> serialize(Message message) {

        Map<String, Object> data = new HashMap<String, Object>();

        data.put(MESSAGE_USER_KEY, message.getMessageUser());

        data.put(MESSAGE_TEXT_KEY, message.getMessageText());

        data.put(MESSAGE_USER_ID_KEY, message.getMessageUserId());

        data.put(MESSAGE_IMAGE_URL_KEY, message.getImageUrl());

        return data;
    }

}
