package com.sdp.movemeet.backend;

import com.google.android.gms.tasks.Task;

public interface BackendStorage<T> {
    Task<?> add(T object, String path);

    Task<?> delete(String path);

    Task<?> get(String path);

    Task<?> search(String field, Object value);
}
