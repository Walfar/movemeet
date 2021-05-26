package com.sdp.movemeet.backend.providers;

import androidx.annotation.VisibleForTesting;

import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationInstanceProvider {

    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public static FirebaseAuth fAuth;

    private AuthenticationInstanceProvider () {
        fAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuth getAuthenticationInstance() {
        if (fAuth == null) {
            fAuth = FirebaseAuth.getInstance();
        }
        return fAuth;
    }
}
