package com.sdp.movemeet.view.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firebaseDB.FirebaseDBMessageManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.firebase.storage.StorageImageManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.MessageSerializer;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.models.Message;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.navigation.Navigation;

import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static boolean enableNav = true;

    private static final String TAG = "ChatActivity";
    public static final String CHATS_CHILD = "chats";

    public static String GENERAL_CHAT_CHILD = "general_chat_new_format"; //"general_chat";
    public static String CHAT_ROOM_ID;

    private static final int REQUEST_IMAGE = 2;

    public static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    public static final String CHAT_IMAGE_NAME = "chatImage.jpg";
    private static final String PATH_SEPARATOR = "/";

    public static final String noMessageText = "no messageText";
    public static final String noImageUrl = "no imageUrl";

    // Firebase instance variables
    private FirebaseAuth fAuth;
    private FirebaseStorage fStorage;
    private StorageReference storageReference;
    private BackendManager<Image> imageBackendManager;
    private FirebaseDatabase database;
    private DatabaseReference chatRef;
    private DatabaseReference chatRoom;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> firebaseAdapter;

    private BackendManager<Message> messageManager;
    private BackendManager<User> userManager;

    private User user;

    private String userId, fullNameString, activityChatId, receivedActivityChatId, receivedActivityTitle, imagePath;

    private int initialMessageCounter = 0;

    private MultiAutoCompleteTextView messageInput;
    private ProgressBar chatLoader;
    private RecyclerView messageRecyclerView;
    private ImageButton btnSend;

    private TextView initialChatWelcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        // The aim is to block any direct access to this page if the user is not logged in
        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else {
            userId = fAuth.getCurrentUser().getUid();
        }

        if(enableNav) new Navigation(this, R.id.nav_home).createDrawer();

        // Initializing Firebase Realtime Database
        database = BackendInstanceProvider.getDatabaseInstance();
        chatRef = database.getReference().child(CHATS_CHILD); // "chats" node reference in Firebase Realtime Database

        messageInput = findViewById(R.id.message_input_text);
        chatLoader = findViewById(R.id.chat_loader);
        messageRecyclerView = findViewById(R.id.message_recycler_view);
        btnSend = findViewById(R.id.button_send_message);
        initialChatWelcomeMessage = findViewById(R.id.initial_chat_welcome_message);

        messageManager = new FirebaseDBMessageManager(new MessageSerializer());

        fStorage = BackendInstanceProvider.getStorageInstance();
        storageReference = fStorage.getReference();

        userManager = new FirestoreUserManager(FirestoreUserManager.USERS_COLLECTION, new UserSerializer());
        getRegisteredUserData();

        Intent data = getIntent();
        settingUpChatRoom(data);

        // The rest of the onCreate is dedicated to add all the existing messages and listening for
        // new child entries under the "chats" path of the sport activity in our Firebase
        // Realtime Database. A new element for each message is automatically added to the UI.

        addExistingMessagesAndListenForNewMessages();
    }


    private void addExistingMessagesAndListenForNewMessages() {
        // Using the MessageAdapter class to create the overall view of the chat room
        firebaseAdapter = new MessageAdapter(chatRoom, userId, ChatActivity.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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


    public void getRegisteredUserData() {
        Task<DocumentSnapshot> document = (Task<DocumentSnapshot>) userManager.get(FirestoreUserManager.USERS_COLLECTION + "/" + userId);
        document.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserSerializer userSerializer = new UserSerializer();
                        user = userSerializer.deserialize(document.getData());
                        fullNameString = user.getFullName();
                    } else {
                        Log.d(TAG, "No such document!");
                    }
                } else {
                    Log.d(TAG, "Get failed with: ", task.getException());
                }
            }
        });
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
        Message message = new Message(userName, messageText, userId, noImageUrl, Long.toString(new Date().getTime()));
        if (messageText.length() > 0) {
            Log.d(TAG, "message.getImageUrl(): " + message.getImageUrl());
            messageManager.add(message, chatRoom.toString().split(PATH_SEPARATOR, 4)[3]);
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
                createTempMessage(uri, fullNameString, userId);
            }
        }
    }


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    // making this method always public for testing and private otherwise
    public void createTempMessage(Uri uri, String fullNameString, String userId) {
        Message tempMessage = new Message(fullNameString, "Image loading...", userId, LOADING_IMAGE_URL, Long.toString(new Date().getTime()));
        // TODO: Make abstraction for this part of code below (Firebase Realtime Database abstraction) --> difficult!
        //  Probably add another .add method that can deal with "DatabaseReference.CompletionListener" ? --> ask Kepler for advice
        chatRoom.push().setValue(tempMessage, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.w(TAG, "Unable to write message to database.", databaseError.toException());
                    return;
                }
                String key = databaseReference.getKey();
                imageBackendManager = new StorageImageManager();
                imagePath = CHATS_CHILD + PATH_SEPARATOR + CHAT_ROOM_ID + PATH_SEPARATOR + key + PATH_SEPARATOR + CHAT_IMAGE_NAME;
                Image image = new Image(uri, null);
                image.setDocumentPath(imagePath); // probably useless
                UploadTask uploadTask = (UploadTask) imageBackendManager.add(image, imagePath);
                putImageInStorage(uploadTask, key);
            }
        });
    }


    private void putImageInStorage(UploadTask uploadTask, String key) {
        // Upload the image to Firebase Storage
        uploadTask.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // After the image loads, get a URI for the image
                // and add it to the message.
                taskSnapshot.getMetadata().getReference().getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Message imageMessage = new Message(fullNameString, noMessageText, userId, uri.toString(), Long.toString(new Date().getTime()));
                            messageManager.set(imageMessage, chatRoom.toString().split(PATH_SEPARATOR, 4)[3] + PATH_SEPARATOR + key);
                        }
                    });
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Image upload task was not successful.", e);
            }
        });

    }

}
