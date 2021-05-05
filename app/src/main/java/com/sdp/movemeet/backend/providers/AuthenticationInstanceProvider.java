package com.sdp.movemeet.backend.providers;

import androidx.annotation.VisibleForTesting;

import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationInstanceProvider {

    private static final AuthenticationInstanceProvider instance = new AuthenticationInstanceProvider();

    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    private FirebaseAuth fAuth;

    private AuthenticationInstanceProvider () {
        fAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuth getAuthenticationInstance() {
        return instance.fAuth;
    }
}
