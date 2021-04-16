package com.sdp.movemeet.Backend;

import java.util.List;

public interface BackendInteraction<T> {
    public void add(T object);

    public void delete(T object);

    public List<T> search(String field, Object value);
}
