package com.sdp.movemeet.backend.serialization;

import java.util.Map;

public interface BackendSerializer<T> {
    public Map<String, Object> serialize(T object);

    public T deserialize(Map<String, Object> data);
}
