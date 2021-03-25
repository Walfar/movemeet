package com.sdp.movemeet.chat;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sdp.movemeet.R;

public class MessageHolder extends RecyclerView.ViewHolder {
    TextView mUser;
    TextView mText;
    TextView mTime;

    public MessageHolder(View itemView){
        super(itemView);
        mUser = itemView.findViewById(R.id.message_user);
        mText = itemView.findViewById(R.id.message_text);
        mTime = itemView.findViewById(R.id.message_time);
    }
}
