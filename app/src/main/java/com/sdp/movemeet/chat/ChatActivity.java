package com.sdp.movemeet.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.Backend.FirebaseInteraction;

import com.sdp.movemeet.R;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    public static final String MESSAGE_CHILD = "messages";
    public static final String ANONYMOUS_NAME = "anonymous_name";
    public static final String ANONYMOUS_ID = "anonymous_id";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;

    private static final String MESSAGE_SENT_EVENT = "message_sent";

    private SharedPreferences mSharedPreferences;

    //private ActivityChatBinding mBinding;
    private LinearLayoutManager mLinearLayoutManager;

    // Firebase instance variables
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private FirebaseDatabase mDatabase;
    private FirebaseFirestore fStore;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;

    private final int MESSAGE_IN_VIEW_TYPE  = 1;
    private final int MESSAGE_OUT_VIEW_TYPE = 2;

    String userId;
    String fullNameString;

    MultiAutoCompleteTextView messageInput;
    ProgressBar chatLoader;
    RecyclerView messageRecyclerView;
    FloatingActionButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageInput = findViewById(R.id.message_input_text);
        chatLoader = findViewById(R.id.chat_loader);
        messageRecyclerView = findViewById(R.id.message_recycler_view);
        btnSend = findViewById(R.id.button_send_message);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            fStore = FirebaseFirestore.getInstance();
            getUserName();
        }

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Initializing Firebase Auth and checking if the user is signed in
        FirebaseInteraction.checkIfUserSignedIn(fAuth, ChatActivity.this);

        // Adding all the existing messages and listening for new child entries under the "messages"
        // path in our Firebase Realtime Database (adding a new element to the UI for each message)

        // Initializing Realtime Database
        mDatabase = FirebaseDatabase.getInstance();

        settingUpMessageAdapter();

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(mLinearLayoutManager);
        messageRecyclerView.setAdapter(mFirebaseAdapter);

        // Scrolling down when a new message arrives
        mFirebaseAdapter.registerAdapterDataObserver(
                new MyScrollToBottomObserver(messageRecyclerView, mFirebaseAdapter, mLinearLayoutManager)
        );

        // Disabling the send button when there's no text in the input field
        messageInput.addTextChangedListener(new MyButtonObserver(btnSend));

    }

    private void settingUpMessageAdapter() {

        DatabaseReference messagesRef = mDatabase.getReference().child(MESSAGE_CHILD);

        // The FirebaseRecyclerAdapter class comes from the FirebaseUI library
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(messagesRef, Message.class)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {

            @Override
            public int getItemViewType(int position) {
                if(getItem(position).getMessageUserId().equals(userId)){
                    return MESSAGE_OUT_VIEW_TYPE;
                }
                return MESSAGE_IN_VIEW_TYPE;
            }

            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = null;
                if(viewType == MESSAGE_IN_VIEW_TYPE) {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.message, parent, false);
                } else {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.message_out, parent, false);
                }
                return new MessageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(MessageViewHolder vh, int position, Message message) {
                chatLoader.setVisibility(ProgressBar.INVISIBLE);
                vh.bindMessage(message);
            }
        };
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
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        // Start listening for updates from Firebase Realtime Database
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void sendMessage(View view) {
        String userName = fullNameString;
        String messageText = messageInput.getText().toString();
        Message message = new Message(userName, messageText, userId);

        mDatabase.getReference().child(MESSAGE_CHILD).push().setValue(message);
        messageInput.setText("");
    }

}