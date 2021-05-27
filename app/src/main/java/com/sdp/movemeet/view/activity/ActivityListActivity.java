package com.sdp.movemeet.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.navigation.Navigation;

import java.util.ArrayList;

/***
 *  This class create the list of activities, on witch the user is registered.
 */


public class ActivityListActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    StorageReference storageReference;

    BackendManager<Activity> activityManager;
    BackendManager<User> userManager;
    ArrayList<String> listOfRegisteredActivityId = new ArrayList<String>();

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static boolean enableNav = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        fStore = FirebaseFirestore.getInstance();

        userManager = new FirestoreUserManager(fStore, FirestoreUserManager.USERS_COLLECTION, new UserSerializer());
        activityManager = new FirestoreActivityManager(fStore, FirestoreActivityManager.ACTIVITIES_COLLECTION, new ActivitySerializer());

        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else {
            userId = fAuth.getCurrentUser().getUid();
        }
        if (enableNav) new Navigation(this, R.id.nav_list_activities).createDrawer();
    }

    /*public void getListOfActivity(View view) {
        Task<DocumentSnapshot> document = (Task<DocumentSnapshot>) userManager.get(FirestoreUserManager.USERS_COLLECTION + "/registeredActivities");
        document.addOnSuccessListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        listOfRegisteredActivityId = document.getData().get("id");
                    }
                }
            }

        });
    }*/
}
