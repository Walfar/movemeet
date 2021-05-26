package com.sdp.movemeet.view.chat;

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
import com.sdp.movemeet.models.Message;

/**
 * This Adapter class makes the bridge between the UI components of the {@link ChatActivity} and
 * the data source (i.e. the messages). More precisely, this class allows to link the message data
 * to the ViewHolder ({@link MessageViewHolder}) in order to construct the RecyclerView of the
 * {@link ChatActivity} with two types of message view: one for the messages sent by the user
 * (aligned to the right) and one for the received messages (aligned to the left).
 */
public class MessageAdapter extends FirebaseRecyclerAdapter<Message, MessageViewHolder> {

    private static final String TAG = "MessageAdapter";

    private final int MESSAGE_IN_VIEW_TYPE = 1;
    private final int MESSAGE_OUT_VIEW_TYPE = 2;

    private Activity chatActivity;
    private String userId;

    /**
     * Constructor that creates a MessageAdapter object.
     *
     * @param messagesRef  Firebase Storage reference of the chat room which messages will be
     *                     displayed
     * @param userId       ID of the user
     * @param chatActivity The activity from which this function is called
     */
    public MessageAdapter(DatabaseReference messagesRef, String userId, ChatActivity chatActivity) {
        super(new FirebaseRecyclerOptions.Builder<Message>().setQuery(messagesRef, Message.class).build());

        Log.i(TAG, "MessageAdapter: created");

        this.chatActivity = chatActivity;
        this.userId = userId;

    }

    /**
     * Bind the different ViewHolders containing the different messages of the chat room
     *
     * @param vh       {@link MessageViewHolder} object containing one message
     * @param position Position of the {@link MessageViewHolder} in the RecyclerView containing all
     *                 the messages of the chat room
     * @param message  Actual {@link Message} object to be displayed
     */
    @Override
    protected void onBindViewHolder(MessageViewHolder vh, int position, Message message) {
        ProgressBar chatLoader = (chatActivity).findViewById(R.id.chat_loader);
        chatLoader.setVisibility(ProgressBar.INVISIBLE);
        TextView initialChatWelcomeMessage = (chatActivity).findViewById(R.id.initial_chat_welcome_message);
        initialChatWelcomeMessage.setVisibility(ProgressBar.INVISIBLE);
        vh.bindMessage(message, (ChatActivity) chatActivity);
    }

    /**
     * Get the view type of a particular message in the chat room. It the message has been sent by
     * the user it will be aligned to the right. Otherwise it will be aligned to the left.
     *
     * @param position Position of the message ViewHolder in the RecyclerView
     * @return The view type of the message
     */
    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getMessageUserId().equals(userId)) {
            return MESSAGE_OUT_VIEW_TYPE;
        }
        return MESSAGE_IN_VIEW_TYPE;
    }

    /**
     * Create the {@link MessageViewHolder} for a particular message. If the message is sent by the
     * user (i.e. it is of type MESSAGE_OUT_VIEW_TYPE) the message will be displayed to the right.
     * If however the message has been written by someone else, the message will be displayed to the
     * left.
     *
     * @param parent   ViewGroup to which the {@link MessageViewHolder} belongs
     * @param viewType Type of view to be applied to the message
     * @return The {@link MessageViewHolder} corresponding to the type of the message (i.e. either
     * sent or received)
     */
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == MESSAGE_IN_VIEW_TYPE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_out, parent, false);
        }
        return new MessageViewHolder(view);
    }

}
