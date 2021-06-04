package com.sdp.movemeet.backend.firebase.storage;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.models.Image;

public class StorageImageManager implements BackendManager<Image> {

    private final StorageReference storageReference;

    public StorageImageManager() {
        this.storageReference = BackendInstanceProvider.getStorageInstance().getReference();
    }

    @Override
    public StorageTask add(Image image, String path) {
        return storageReference.child(path).putFile(image.getImageUri());
    }

    @Override
    public Task<?> set(Image object, String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Task<?> update(String path, String field, String value, String method) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Task<?> delete(String path) {
        return storageReference.child(path).delete();
    }

    @Override
    public Task<Uri> get(String path) {
        return storageReference.child(path).getDownloadUrl();
    }

    @Override
    public Task<?> search(String field, Object value) {
        throw new UnsupportedOperationException();
    }
}
