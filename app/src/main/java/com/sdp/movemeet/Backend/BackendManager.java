package com.sdp.movemeet.Backend;

import com.google.android.gms.tasks.Task;

import java.util.concurrent.Future;

/**
 * A generic interface to perform basic storage management
 * operations with a backend
 * @param <T> The type of object that this BackendManager will hande
 */
public interface BackendManager<T> {
    /**
     * Add an instance of T to the backend
     * @param object the instance of T to add.
     * @param path the path of the instance in the backend.
     * @return a Task, the success of which determines the success of the operation.
     */
    public Task<?> add(T object, String path);

    /**
     * Deletes an entry of T located at the specified path in the backend.
     * @param path the path to the instance to be deleted.
     * @return a Task, the success of which determines the success of the operation.
     */
    public Task<?> delete(String path);

    /**
     * Retrieves an instance of T at the specified location from the backend.
     * @param path the path of the instance to retrieve.
     * @return a Task whose getResult method returns the result from the backend.
     */
    public Task<?> get(String path);

    /**
     * Searches for instances of T in the backend, containing a field equal to
     * the specified value.
     * @param field The field to use as filter.
     * @param value The value to filter instances by.
     * @return a Task containing a collection of instances fitting the criteria.
     */
    public Task<?> search(String field, Object value);
}
