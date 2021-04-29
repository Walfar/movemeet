package com.sdp.movemeet.Activity;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ListActivityTest {

    private FirebaseAuth fAuth;
    private FirebaseDatabase database;

    @Test
    public void listOfActivityTest() {
        CountDownLatch latch = new CountDownLatch(1);
        fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword("movemeet@gmail.com", "password").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                latch.countDown();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assert (false);
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            assert(false);
        }

        ActivityScenario scenario = ActivityScenario.launch(ListActivity.class);
        FirebaseUser user = fAuth.getCurrentUser();
        if(user != null){
            FirebaseAuth.getInstance().signOut();
        }
    }
}
