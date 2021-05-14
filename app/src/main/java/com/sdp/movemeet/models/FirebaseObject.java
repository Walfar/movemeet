package com.sdp.movemeet.models;

/**
 * An object that can be found in the Firebase Realtime Database/Firestore backend
 */
public abstract class FirebaseObject {

    protected String documentPath;
    /**
     * @return The path of the document in the backend.
     */
    public String getDocumentPath() {
        return this.documentPath;
    }

    /**
     * Sets the path of the document in the backend.
     *
     * @param path the path to attempt to set for the object.
     * @return the path of the object in the backend.
     */
    public String setDocumentPath(String path) {
        if (documentPath == null) documentPath = path;
        return documentPath;
    }
}
