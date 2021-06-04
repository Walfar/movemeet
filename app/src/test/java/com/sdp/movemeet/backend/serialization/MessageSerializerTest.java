package com.sdp.movemeet.backend.serialization;

import com.sdp.movemeet.models.Message;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MessageSerializerTest {

    @Test
    public void serializeDeserializeMessageWorks() {
        Message message = new Message("user", "my message", "user id", "my url", "message time");
        MessageSerializer serializer = new MessageSerializer();
        Map<String, Object> serialized = serializer.serialize(message);
        Message deserializedMessage = serializer.deserialize(serialized);
        assertEquals(message.getMessageText(), deserializedMessage.getMessageText());
        assertEquals(message.getMessageUser(), deserializedMessage.getMessageUser());
        assertEquals(message.getMessageUserId(), deserializedMessage.getMessageUserId());
        assertEquals(message.getImageUrl(), deserializedMessage.getImageUrl());
        assertEquals(message.getMessageTime(), deserializedMessage.getMessageTime());
    }
}
