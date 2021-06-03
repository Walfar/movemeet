package com.sdp.movemeet.backend.providers;

import androidx.annotation.VisibleForTesting;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Singleton class which provides a single authentication service instance for use inside
 * other classes.
 */
public class AuthenticationInstanceProvider {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static FirebaseAuth fAuth;

    private AuthenticationInstanceProvider () {
        fAuth = FirebaseAuth.getInstance();
    }

    /**
     * Retrieves an authentication service instance
     *
     * @return a singleton FirebaseAuth instance
     */
    public static FirebaseAuth getAuthenticationInstance() {
        if (fAuth == null) {
            fAuth = FirebaseAuth.getInstance();
        }
        return fAuth;
    }
}
