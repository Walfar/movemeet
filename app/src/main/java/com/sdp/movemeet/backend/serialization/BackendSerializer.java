package com.sdp.movemeet.backend.serialization;

import java.util.Map;

public interface BackendSerializer<T> {
    Map<String, Object> serialize(T object);

    T deserialize(Map<String, Object> data);
}
