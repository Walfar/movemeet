package com.sdp.movemeet.backend.serialization;

import com.sdp.movemeet.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserSerializer implements BackendSerializer<User> {

    // The key used to access the fullName attribute of a serialized User
    public static final String FULL_NAME_KEY = "fullName";
    // The key used to access the email attribute of a serialized User
    public static final String EMAIL_KEY = "email";
    // The key used to access the phone attribute of a serialized User
    public static final String PHONE_KEY = "phone";
    // The key used to access the description attribute of a serialized User
    public static final String DESCRIPTION_KEY = "description";
    // The key used to access the created activities attribute of a serialized User
    private static  final String CREATED_ACTIVITIES_KEY = "createdActivity";
    // The key used to access the registered activities attribute of a serialized User
    private static final String REGISTERED_ACTIVITIES_KEY = "registeredActivity";

    @Override
    public User deserialize(Map<String, Object> data) {

        User user = new User (
                (String) data.get(FULL_NAME_KEY),

                (String) data.get(EMAIL_KEY),

                (String) data.get(PHONE_KEY),

                (String) data.get(DESCRIPTION_KEY),

                (ArrayList<String>) data.get(CREATED_ACTIVITIES_KEY),

                (ArrayList<String>) data.get(REGISTERED_ACTIVITIES_KEY)
        );

        return user;
    }

    @Override
    public Map<String, Object> serialize(User user) {
        Map<String, Object> data = new HashMap<String, Object>();

        data.put(FULL_NAME_KEY, user.getFullName());

        data.put(EMAIL_KEY, user.getEmail());

        data.put(PHONE_KEY, user.getPhoneNumber());

        data.put(DESCRIPTION_KEY, user.getDescription());

        data.put(CREATED_ACTIVITIES_KEY, user.getCreatedActivitiesId());

        data.put(REGISTERED_ACTIVITIES_KEY, user.getRegisteredActivitiesId());

        return data;
    }

}
