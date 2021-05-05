package com.sdp.movemeet.backend.serialization;

import java.util.Map;

/**
 * An interface describing operations necessary to (de)serialize objects
 * so that they may be stored and retrieved from a backend.
 * @param <T> The type of object that can be (de)serialized
 */
public interface BackendSerializer<T> {
    /**
     * Serializes an object into a map of keys to their associated values
     * @param object the object to serialize
     * @return the newly constructed Map
     */
    public Map<String, Object> serialize(T object);

    /**
     * Deserializes an object from a <key, value> map into an instance of T.
     * @param data the map of <key, value> entries to deserialize
     * @return a newly created instance of T
     */
    public T deserialize(Map<String, Object> data);
}
