package com.sdp.movemeet.backend;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

/**
 * A generic interface to perform basic storage management
 * operations with a backend
 * @param <T> The type of object that this BackendManager will handle
 */
public interface BackendManager<T> {
    /**
     * Add an instance of T to the backend
     * @param object the instance of T to add.
     * @param path the path of the instance in the backend.
     * @return a Task, the success of which determines the success of the operation.
     */
    Task<?> add(T object, String path);

    /**
     * Set an instance of T to the backend
     * @param object the instance of T to set.
     * @param path the path of the instance in the backend.
     * @return a Task, the success of which determines the success of the operation.
     */
    Task<?> set(T object, String path);

    /**
     * Update (add) a String value to a Firebase Firestore field
     * @param value the String value to update in the array.
     * @param path the path of the instance in the backend.
     * @param field the field to which the array corresponds in the backend.
     * @return a Task, the success of which determines the success of the operation.
     */
    Task<?> updt(String value, String path, String field);

    /**
     * Deletes an entry of T located at the specified path in the backend.
     * @param path the path to the instance to be deleted.
     * @return a Task, the success of which determines the success of the operation.
     */
    Task<?> delete(String path);

    /**
     * Retrieves an instance of T at the specified location from the backend.
     * @param path the path of the instance to retrieve.
     * @return a Task whose getResult method returns the result from the backend.
     */
    Task<?> get(String path);

    /**
     * Searches for instances of T in the backend, containing a field equal to
     * the specified value.
     * @param field The field to use as filter.
     * @param value The value to filter instances by.
     * @return a Task containing a collection of instances fitting the criteria.
     */
    Task<?> search(String field, Object value);
}
