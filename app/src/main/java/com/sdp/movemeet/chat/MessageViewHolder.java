package com.sdp.movemeet.chat;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sdp.movemeet.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "MessageViewHolder";

    TextView messageTextView;
    TextView messengerTextView;
    TextView messageTimeTextView;

    public MessageViewHolder(View v) {
        super(v);
        messageTextView = (TextView) itemView.findViewById(R.id.message_text);
        messengerTextView = (TextView) itemView.findViewById(R.id.message_user);
        messageTimeTextView = (TextView) itemView.findViewById(R.id.message_time);
    }

    public void bindMessage(Message message) {
        if (message.getMessageText() != null) {
            messageTextView.setText(message.getMessageText());
            messageTextView.setVisibility(TextView.VISIBLE);
        }
    }




}
