package com.sdp.movemeet;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
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
            users = mapper.readValue(new File(filename), Map.class);
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Change filename.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(filename), users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUser(String email, String password) {
        if (isInUsers(email)) {
            return;
        } else {
            users.put(email, password);
            writeToFile();
        }
    }

    // checks whether a given user is in the database or not
    public boolean isInUsers(String email) {
        return users.containsKey(email);
    }
}
