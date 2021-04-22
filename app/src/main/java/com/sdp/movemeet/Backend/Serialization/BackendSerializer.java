package com.sdp.movemeet.Backend.Serialization;

import java.util.Map;

public interface BackendSerializer<T> {
    public Map<String, Object> serialize(T object);

    public T deserialize(Map<String, Object> data);
}
