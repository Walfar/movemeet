package com.sdp.movemeet.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.databinding.ActivityChatBinding;

import com.sdp.movemeet.R;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    public static final String MESSAGE_CHILD = "messages";
    public static final String ANONYMOUS_NAME = "anonymous_name";
    public static final String ANONYMOUS_ID = "anonymous_id";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;

    private static final String MESSAGE_SENT_EVENT = "message_sent";

    private SharedPreferences mSharedPreferences;

    private ActivityChatBinding mBinding;
    private LinearLayoutManager mLinearLayoutManager;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chat);

        // Using View Binding
        mBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Initializing Firebase Auth and checking if the user is signed in
        checkIfUserSignedIn();

        // Adding all the existing messages and listening for new child entries under the "messages"
        // path in our Firebase Realtime Database. It adds a new element to the UI for each message

        // Initializing Realtime Database
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference messagesRef = mDatabase.getReference().child(MESSAGE_CHILD);

        // The FirebaseRecyclerAdapter class comes from the FirebaseUI library
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(messagesRef, Message.class)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {
            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.message, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(MessageViewHolder vh, int position, Message message) {
                mBinding.chatLoader.setVisibility(ProgressBar.INVISIBLE);
                vh.bindMessage(message);
            }
        };

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mBinding.messageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mBinding.messageRecyclerView.setAdapter(mFirebaseAdapter);

        // Scrolling down when a new message arrives
        mFirebaseAdapter.registerAdapterDataObserver(
                new MyScrollToBottomObserver(mBinding.messageRecyclerView, mFirebaseAdapter, mLinearLayoutManager)
        );

        // Disabling the send button when there's no text in the input field
        mBinding.messageInput.addTextChangedListener(new MyButtonObserver(mBinding.btnSend));




        mBinding.messageInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mBinding.messageInput.setHint("");
                return false;
            }
        });

        mBinding.messageInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    mBinding.messageInput.setHint("Hiiiint");
                }
            }
        });



    }

    private void checkIfUserSignedIn() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth.getCurrentUser() == null) {
            // Not signed in, lauch the LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in.
        checkIfUserSignedIn();
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
        Message message = new Message(
                    getUserName(),
                    mBinding.messageInput.getText().toString(),
                    getUserId());

        mDatabase.getReference().child(MESSAGE_CHILD).push().setValue(message);
        mBinding.messageInput.setText("");
    }

    private String getUserName() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            return user.getDisplayName();
        }
        return ANONYMOUS_NAME;
    }

    private String getUserId() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return ANONYMOUS_ID;
    }

}