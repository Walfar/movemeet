package com.sdp.movemeet.backend.providers;

import androidx.annotation.VisibleForTesting;

import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationInstanceProvider {

    public static final AuthenticationInstanceProvider instance = new AuthenticationInstanceProvider();

    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public FirebaseAuth fAuth;

    private AuthenticationInstanceProvider () {
        fAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuth getAuthenticationInstance() {
        return instance.fAuth;
    }
}
