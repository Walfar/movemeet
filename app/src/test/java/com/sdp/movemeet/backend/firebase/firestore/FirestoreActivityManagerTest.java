package com.sdp.movemeet.backend.firebase.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.modelsTest.ActivityTest;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4ClassRunner.class)
public class FirestoreActivityManagerTest {

    FirebaseFirestore db;
    Activity activity;
    BackendSerializer<Activity> serializer;
    BackendManager<Activity> activityManager;

    DocumentReference docRef = mock(DocumentReference.class);
    CollectionReference colRef = mock(CollectionReference.class);
    DocumentSnapshot docSnap = mock(DocumentSnapshot.class);
    Query query = mock(Query.class);

    Task<Void> addTask = mock(Task.class);
    Task<Void> deleteTask = mock(Task.class);
    Task<QuerySnapshot> searchTask = mock(Task.class);
    Task<DocumentSnapshot> getTask;

    @Before
    public void setup() {
        activity = ActivityTest.createFakeActivity();
        serializer = new ActivitySerializer();

        db = mock(FirebaseFirestore.class);

        when(db.document(any())).thenReturn(docRef);
        when(db.collection(any())).thenReturn(colRef);

        // Search
        when(colRef.document()).thenReturn(docRef);
        when(colRef.whereEqualTo((String) any(), any())).thenReturn(query);
        when(query.get()).thenReturn(searchTask);

        // Delete
        when(docRef.delete()).thenReturn(deleteTask);

        // Get
        Map<String, Object> data = serializer.serialize(activity);
        when(docSnap.getData()).thenReturn(data);

        getTask = new Task<DocumentSnapshot>() {
            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public boolean isSuccessful() {
                return true;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public DocumentSnapshot getResult() {
                return docSnap;
            }

            @Override
            public <X extends Throwable> DocumentSnapshot getResult(@NonNull @NotNull Class<X> aClass) throws X {
                return null;
            }

            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            public Exception getException() {
                return null;
            }

            @Override
            public Task<DocumentSnapshot> addOnSuccessListener(@NonNull @NotNull OnSuccessListener<? super DocumentSnapshot> onSuccessListener) {
                return null;
            }


            @Override
            public Task<DocumentSnapshot> addOnSuccessListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnSuccessListener<? super DocumentSnapshot> onSuccessListener) {
                return null;
            }

            @Override
            public Task<DocumentSnapshot> addOnSuccessListener(@NonNull @NotNull android.app.Activity activity, @NonNull @NotNull OnSuccessListener<? super DocumentSnapshot> onSuccessListener) {
                return null;
            }


            @Override
            public Task<DocumentSnapshot> addOnFailureListener(@NonNull @NotNull OnFailureListener onFailureListener) {
                return null;
            }


            @Override
            public Task<DocumentSnapshot> addOnFailureListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnFailureListener onFailureListener) {
                return null;
            }


            @Override
            public Task<DocumentSnapshot> addOnFailureListener(@NonNull @NotNull android.app.Activity activity, @NonNull @NotNull OnFailureListener onFailureListener) {
                return null;
            }
        };

        when(docRef.get()).thenReturn(getTask);

        // Add
        when(docRef.set(any())).thenReturn(addTask);

        activityManager = new FirestoreActivityManager(db,
                FirestoreActivityManager.ACTIVITIES_COLLECTION,
                serializer);
    }

    @Test
    public void constructorThrowsIllegalArgumentExceptionOnNullParameter() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FirestoreActivityManager(null, FirestoreActivityManager.ACTIVITIES_COLLECTION, serializer);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new FirestoreActivityManager(db, null, serializer);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new FirestoreActivityManager(db, FirestoreActivityManager.ACTIVITIES_COLLECTION, null);
        });
    }

    @Test
    public void addThrowsIllegalArgumentExceptionOnNullObject() {
        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.add(null, "");
        });
    }

    @Test
    public void addReturnsCorrectTask() {
        assertEquals(addTask, activityManager.add(activity, null));
    }

    @Test
    public void deleteThrowsIllegalArgumentExceptionOnNullOrEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.delete(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.delete("");
        });
    }

    @Test
    public void deleteReturnsCorrectTask() {
        assertEquals(deleteTask, activityManager.delete("path"));
    }

    @Test
    public void searchThrowsIllegalArgumentExceptionOnNullParameter() {
        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.search(null, 6);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.search("field", null);
        });
    }

    @Test
    public void searchReturnsCorrectTask() {
        assertEquals(searchTask, activityManager.search("field", 6));
    }

    @Test
    public void getThrowsIllegalArgumentExceptionOnNullOrEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.get(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            activityManager.get("");
        });
    }

    @Test
    public void getReturnsCorrectTask() {
        assertEquals(getTask, activityManager.get("path"));
    }

    @Test
    public void resultOfGetCanBeDeserialized() {
        DocumentSnapshot retrieved = (DocumentSnapshot) activityManager.get("path").getResult();

        assertEquals(activity,
                serializer.deserialize(retrieved.getData()));
    }
}
