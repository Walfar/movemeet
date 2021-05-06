package com.sdp.movemeet.backend.providers;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.sdp.movemeet.view.home.HomeScreenActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class BackendInstanceProviderTest {

    FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
    FirebaseStorage mockStorage = mock(FirebaseStorage.class);
    FirebaseDatabase mockDatabase = mock(FirebaseDatabase.class);

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> testRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

    @Before
    public void setup() {
        BackendInstanceProvider.firestore = mockFirestore;
        BackendInstanceProvider.storage = mockStorage;
        BackendInstanceProvider.database = mockDatabase;
    }

    @Test
    public void backendInstanceProviderReturnsMocksCorrectly() {
        assertEquals(mockFirestore, BackendInstanceProvider.getFirestoreInstance());
        assertEquals(mockDatabase, BackendInstanceProvider.getDatabaseInstance());
        assertEquals(mockStorage, BackendInstanceProvider.getStorageInstance());
    }
}
