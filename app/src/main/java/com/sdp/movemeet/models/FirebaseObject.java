package com.sdp.movemeet.models;

/**
 * An object that can be found in the Firebase Realtime Database/Firestore backend
 */
public interface FirebaseObject {

    /**
     * @return The path of the document in the backend.
     */
    String getDocumentPath();

    /**
     * Sets the path of the document in the backend.
     * @param path the path to attempt to set for the object.
     * @return the path of the object in the backend.
     */
    String setDocumentPath(String path);
}
