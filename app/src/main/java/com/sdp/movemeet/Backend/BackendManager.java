package com.sdp.movemeet.Backend;

import com.google.android.gms.tasks.Task;

public interface BackendManager<T> {
    public Task<?> add(T object, String path);

    public Task<?> delete(String path);

    public Task<?> get(String path);

    public Task<?> search(String field, Object value);
}
