package com.sdp.movemeet.chat;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sdp.movemeet.R;

import java.text.SimpleDateFormat;

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
        if (message.getMessageUser() != null) {
            messengerTextView.setText(message.getMessageUser());
            messengerTextView.setVisibility(TextView.VISIBLE);
        }
        //messageTimeTextView.setText(String.valueOf((int) message.getMessageTime()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String messageTimeString = simpleDateFormat.format(message.getMessageTime());
        messageTimeTextView.setText(messageTimeString);
        messageTimeTextView.setVisibility(TextView.VISIBLE);
    }




}