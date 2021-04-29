package com.sdp.movemeet.Backend.Firebase.Firestore;

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
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Activity.ActivityTest;
import com.sdp.movemeet.Backend.BackendManager;
import com.sdp.movemeet.Backend.Serialization.BackendSerializer;
import com.sdp.movemeet.Backend.Serialization.ActivitySerializer;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FirestoreActivityManagerTest {

    private BackendManager<Activity> fam;
    private BackendSerializer<Activity> serializer;
    private Activity fakeActivity;

    // Dependencies that need to be mocked
    private FirebaseFirestore db;
    private CollectionReference colRef;
    private DocumentReference docRef;

    private DocumentSnapshot snap;
    private Query query;
    private QuerySnapshot querySnap;

    private Task<Void> fakeDeleteTask = new Task<Void>() {
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
        public Void getResult() {
            return null;
        }

        @Override
        public <X extends Throwable> Void getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull android.app.Activity activity, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull android.app.Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };

    private Task<Void> fakeAddTask = new Task<Void>() {
        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public Void getResult() {
            return null;
        }

        @Override
        public <X extends Throwable> Void getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull android.app.Activity activity, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull android.app.Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };

    private Task<DocumentSnapshot> fakeGetTask = new Task<DocumentSnapshot>() {
        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public DocumentSnapshot getResult() {
            return snap;
        }

        @Override
        public <X extends Throwable> DocumentSnapshot getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentSnapshot> addOnSuccessListener(@NonNull OnSuccessListener<? super DocumentSnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentSnapshot> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super DocumentSnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentSnapshot> addOnSuccessListener(@NonNull android.app.Activity activity, @NonNull OnSuccessListener<? super DocumentSnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentSnapshot> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentSnapshot> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentSnapshot> addOnFailureListener(@NonNull android.app.Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };

    private Task<QuerySnapshot> fakeSearchTask = new Task<QuerySnapshot>() {

        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public QuerySnapshot getResult() {
            return querySnap;
        }

        @Override
        public <X extends Throwable> QuerySnapshot getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnSuccessListener(@NonNull OnSuccessListener<? super QuerySnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super QuerySnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnSuccessListener(@NonNull android.app.Activity activity, @NonNull OnSuccessListener<? super QuerySnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnFailureListener(@NonNull android.app.Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };

    @Before
    public void setup() {
        fakeActivity = ActivityTest.createFakeActivity();
        serializer = new ActivitySerializer();

        db = mock(FirebaseFirestore.class);
        colRef = mock(CollectionReference.class);
        docRef = mock(DocumentReference.class);
        snap = mock(DocumentSnapshot.class);
        query = mock(Query.class);
        querySnap = mock(QuerySnapshot.class);

        when(db.collection(any())).thenReturn(colRef);
        when(colRef.document(any())).thenReturn(docRef);
        when(colRef.document()).thenReturn(docRef);

        // delete
        when(docRef.delete()).thenReturn(fakeDeleteTask);

        // add
        when(docRef.getPath()).thenReturn("doc");
        when(docRef.set(any())).thenReturn(fakeAddTask);

        // get
        when(docRef.get()).thenReturn(fakeGetTask);

        // search
        when(colRef.whereEqualTo(contains("field"), any())).thenReturn(query);
        when(query.get()).thenReturn(fakeSearchTask);

        fam = new FirestoreActivityManager(db, FirestoreActivityManager.ACTIVITIES_COLLECTION, serializer);
    }

    @Test
    public void addThrowsIllegalArgumentExceptionOnNullParameter() {
        assertThrows(IllegalArgumentException.class, () -> {
            fam.add(null, "path");
        });
    }

    @Test
    public void addReturnsCorrectTask() {
        Task<Void> result = (Task<Void>) fam.add(fakeActivity, "doc");
        assertEquals(fakeActivity.getDocumentPath(), "doc");
        assertEquals(fakeAddTask, result);
    }

    @Test
    public void deleteThrowsIllegalArgumentExceptionOnNullOrEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            fam.delete(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            fam.delete("");
        });
    }

    @Test
    public void deleteReturnsCorrectTask() {
        assertEquals(fakeDeleteTask, fam.delete("doc"));
    }

    @Test
    public void getThrowsIllegalArgumentExceptionOnNullOrEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            fam.get(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            fam.get("");
        });
    }

    @Test
    public void getReturnsCorrectResult() {
        assertEquals(snap, fam.get("doc").getResult());
    }

    @Test
    public void searchThrowsIllegalArgumentExceptionOnNullParameter() {
        assertThrows(IllegalArgumentException.class, () -> {
            fam.search("field", null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            fam.search(null, new Integer(1));
        });
    }

    @Test
    public void searchThrowsIllegalArgumentExceptionOnEmptyField() {
        assertThrows(IllegalArgumentException.class, () -> {
            fam.search("", new Integer(1));
        });
    }

    @Test
    public void searchReturnsCorrectResult() {
        assertEquals(querySnap, fam.search("field", fakeActivity).getResult());
    }
}
