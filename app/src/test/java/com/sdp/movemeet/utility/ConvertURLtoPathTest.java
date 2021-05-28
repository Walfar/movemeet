package com.sdp.movemeet.utility;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertURLtoPathTest {

    private static final String EXPECTED_PATH = "chats/WdKcCoUX3UOI4x6LRqXg/-MaBMMc88jmAtNS4d-2w/chatImage.jpg";
    private static final String IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/movemeet-4cbf5.appspot.com/o/chats%2FWdKcCoUX3UOI4x6LRqXg%2F-MaBMMc88jmAtNS4d-2w%2FchatImage.jpg?alt=media&token=d77d5be7-85d4-496a-a4d9-a88a97944904";

    @Test
    public void expectedImagePath_isCorrect() {
        assertEquals(EXPECTED_PATH, ImageHandler.convertURLtoPath(IMAGE_URL));
    }
}
