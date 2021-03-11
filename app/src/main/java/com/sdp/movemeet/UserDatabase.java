package com.sdp.movemeet;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
    private final String filename = "users.json";
    private Map<String, String> users = new HashMap<String, String>();

    public UserDatabase() {
        readFromFile();
    }

    public void readFromFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();

            //users = mapper.readValue(, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToFile() {}

    public void addUser(String email, String password) {}

    public boolean isInUsers(String email) {
        return false;
    }
}
