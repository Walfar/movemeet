package com.sdp.movemeet.chat;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.sdp.movemeet.R;

public class MessageAdapter extends FirebaseRecyclerAdapter<Message, MessageViewHolder> {

    private static final String TAG = "MessageAdaper";

    private final int MESSAGE_IN_VIEW_TYPE  = 1;
    private final int MESSAGE_OUT_VIEW_TYPE = 2;

    Activity chatActivity;
    String userId;

    public MessageAdapter(DatabaseReference messagesRef, String userId, ChatActivity chatActivity) {
        super(new FirebaseRecyclerOptions.Builder<Message>().setQuery(messagesRef, Message.class).build());

        Log.i(TAG, "MessageAdapter: created");

        this.chatActivity = chatActivity;
        this.userId = userId;

    }

    @Override
    protected void onBindViewHolder(MessageViewHolder vh, int position, Message message) {
        ProgressBar chatLoader = (chatActivity).findViewById(R.id.chat_loader);
        chatLoader.setVisibility(ProgressBar.INVISIBLE);
        TextView initialChatWelcomeMessage = (chatActivity).findViewById(R.id.initial_chat_welcome_message);
        initialChatWelcomeMessage.setVisibility(ProgressBar.INVISIBLE);
        vh.bindMessage(message);
    }

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

}
