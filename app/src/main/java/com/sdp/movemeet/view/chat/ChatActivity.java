package com.sdp.movemeet.view.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdp.movemeet.backend.FirebaseInteraction;
import com.sdp.movemeet.models.Message;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.navigation.Navigation;
import com.sdp.movemeet.R;


public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    public static final String CHATS_CHILD = "chats";

    public static String GENERAL_CHAT_CHILD = "general_chat";
    public static String CHAT_ROOM_ID;

    private static final int REQUEST_IMAGE = 2;

    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";

    private LinearLayoutManager linearLayoutManager;

    // Firebase instance variables
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private FirebaseFirestore fStore;
    private StorageReference storageReference;
    private DatabaseReference chatRef;
    private DatabaseReference chatRoom;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> firebaseAdapter;

    String userId;
    String fullNameString;
    String activityChatId;
    String receivedActivityChatId;
    String receivedActivityTitle;

    int initialMessageCounter = 0;

    MultiAutoCompleteTextView messageInput;
    ProgressBar chatLoader;
    RecyclerView messageRecyclerView;
    ImageButton btnSend;

    TextView fullName;
    TextView email;
    TextView phone;
    TextView initialChatWelcomeMessage;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initializing Firebase Realtime Database
        database = FirebaseDatabase.getInstance();
        chatRef = database.getReference().child(CHATS_CHILD); // "chats" node reference in Firebase Realtime Database

        messageInput = findViewById(R.id.message_input_text);
        chatLoader = findViewById(R.id.chat_loader);
        messageRecyclerView = findViewById(R.id.message_recycler_view);
        btnSend = findViewById(R.id.button_send_message);
        initialChatWelcomeMessage = findViewById(R.id.initial_chat_welcome_message);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            fStore = FirebaseFirestore.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference();
            getUserName();
        }

        // Initializing Firebase Auth and checking if the user is signed in
        FirebaseInteraction.checkIfUserSignedIn(fAuth, ChatActivity.this);

        Intent data = getIntent();
        settingUpChatRoom(data);

        // The rest of the onCreate is dedicated to add all the existing messages and listening for
        // new child entries under the "messages" path of the sport activity in our Firebase
        // Realtime Database. A new element for each message is automatically added to the UI.

        addExistingMessagesAndListenForNewMessages();

        createDrawer();

        handleRegisterUser();

        //The aim is to block any direct access to this page if the user is not logged
        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }

    }

    public void createDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        textView = findViewById(R.id.textView);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        View hView = navigationView.inflateHeaderView(R.layout.header);

        fullName = hView.findViewById(R.id.text_view_profile_name);
        phone = hView.findViewById(R.id.text_view_profile_phone);
        email = hView.findViewById(R.id.text_view_profile_email);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigationView.setCheckedItem(R.id.nav_chat);
    }

    private void addExistingMessagesAndListenForNewMessages() {
        // Use the MessageAdapter class to create the overall view of the chat room
        firebaseAdapter = new MessageAdapter(chatRoom, userId, ChatActivity.this);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
        messageRecyclerView.setAdapter(firebaseAdapter);

        // Scrolling down when a new message arrives from the database
        firebaseAdapter.registerAdapterDataObserver(
                new MyScrollToBottomObserver(messageRecyclerView, firebaseAdapter, linearLayoutManager)
        );
    }

    private void settingUpChatRoom(Intent data) {
        // Create a new chat room for the sport activity in case it deosn't yet exist
        receivedActivityChatId = data.getStringExtra("ACTIVITY_CHAT_ID");
        receivedActivityTitle = data.getStringExtra("ACTIVITY_TITLE");
        if (receivedActivityChatId != null) {
            Log.d(TAG, "DocumentSnapshot data: " + receivedActivityChatId);
            activityChatId = receivedActivityChatId;
            // Dynamically creating a new child under the branch "chats" in Firebase Realtime
            // Database with the value of "activityChatId" in case it doesn't exist yet
            CHAT_ROOM_ID = activityChatId;
        } else {
            CHAT_ROOM_ID = GENERAL_CHAT_CHILD; // default general chat room
        }
        chatRoom = chatRef.child(CHAT_ROOM_ID);
        countMessagesInChatRoom();
    }

    private void countMessagesInChatRoom() {
        // Count the number of messages (children) in the current chatRoom. In case the chat room
        // doesn't contain any message a welcome message is displayed
        chatRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.v(TAG, "childDataSnapshot.getKey(): " + childDataSnapshot.getKey());
                    initialMessageCounter += 1;
                }
                Log.v(TAG, "initialMessageCounter: " + initialMessageCounter);
                if (initialMessageCounter == 0) {
                    chatLoader.setVisibility(View.INVISIBLE);
                    initialChatWelcomeMessage.setText("Welcome to the chat of " + receivedActivityTitle + "! Feel free to initiate the discussion by sending your first message!");
                    initialChatWelcomeMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "databaseError: " + databaseError);
            }
        });
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Navigation.goToHome(this.navigationView);
                finish();
                break;
            case R.id.nav_edit_profile:
                Navigation.goToUserProfileActivity(this.navigationView);
                finish();
                break;
            case R.id.nav_add_activity:
                Navigation.goToActivityUpload(this.navigationView);
                finish();
                break;
            case R.id.nav_logout:
                FirebaseInteraction.logoutIfUserNonNull(fAuth, this);
                finish();
                break;
            case R.id.nav_start_activity:
                Navigation.startActivity(this.navigationView);
                finish();
                break;
            case R.id.nav_chat:
                break;
            case R.id.nav_list_activities:
                Navigation.goToListOfActivities(this.navigationView);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(View view) {
        if (fAuth.getCurrentUser() != null) {
            fAuth.getInstance().signOut(); // this will do the logout of the user from Firebase
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }
    }

    private void getUserName() {
        DocumentReference docRef = fStore.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        fullNameString = (String) document.getData().get("fullName");
                        Log.i(TAG, "fullNameString: " + fullNameString);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Checking if user is signed in
        FirebaseInteraction.checkIfUserSignedIn(fAuth, ChatActivity.this);
    }

    @Override
    public void onPause() {
        // Stop listening for updates from Firebase Realtime Database
        firebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        // Start listening for updates from Firebase Realtime Database
        super.onResume();
        firebaseAdapter.startListening();
    }

    public void sendMessage(View view) {
        String userName = fullNameString;
        String messageText = messageInput.getText().toString();
        Message message = new Message(userName, messageText, userId, null /* no image */);
        if (messageText.length() > 0) {
            chatRoom.push().setValue(message);
            messageInput.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Empty message.", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendImage(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                final Uri uri = data.getData();
                Log.d(TAG, "Uri: " + uri.toString());
                Message tempMessage = new Message(fullNameString, null, userId, LOADING_IMAGE_URL);
                chatRoom.push().setValue(tempMessage, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.w(TAG, "Unable to write message to database.", databaseError.toException());
                            return;
                        }
                        // Building a StorageReference and then uploading the image file
                        String key = databaseReference.getKey();
                        StorageReference fileRef = storageReference
                                .child(CHATS_CHILD)
                                .child(CHAT_ROOM_ID)
                                .child(key)
                                .child(uri.getLastPathSegment());
                        putImageInStorage(fileRef, uri, key);
                    }
                });
            }
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        // Upload the image to Firebase Storage
        storageReference.putFile(uri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // After the image loads, get a public downloadUrl for the image
                        // and add it to the message.
                        taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Message imageMessage = new Message(fullNameString, null, userId, uri.toString());
                                        chatRoom.child(key).setValue(imageMessage);
                                    }
                                });
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Image upload task was not successful.", e);
                    }
                });
    }

    public void handleRegisterUser() {
        // Retrieve user data (full name, email and phone number) from Firebase Firestore
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        if (fAuth.getCurrentUser() != null) {
            userId = fAuth.getCurrentUser().getUid();
            TextView[] textViewArray = {fullName, email, phone};
            FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, ChatActivity.this);
        }
    }

}