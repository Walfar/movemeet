package com.sdp.movemeet.Backend;

import com.google.android.gms.tasks.Task;

public interface BackendStorage<T> {
    public Task<?> add(T object);

    public Task<?> delete(T object);

    public Task<?> get(String path);

    public Task<?> search(String field, Object value);
}
