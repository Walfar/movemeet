package com.sdp.movemeet.chat;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.R;

import java.text.SimpleDateFormat;

/**
 * Objects of this class embed a message in an adapted view.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "MessageViewHolder";

    TextView messageTextView;
    ImageView messageImageView;
    TextView messengerTextView;
    TextView messageTimeTextView;

    /**
     * Reference the message data in UI elements (TextViews and ImageView)
     *
     * @param v View of the message
     */
    public MessageViewHolder(View v) {
        super(v);
        messageTextView = itemView.findViewById(R.id.message_text);
        messageImageView = itemView.findViewById(R.id.messageImageView);
        messengerTextView = itemView.findViewById(R.id.message_user);
        messageTimeTextView = itemView.findViewById(R.id.message_time);
    }

    /**
     * Populate the different message views composing the {@link MessageViewHolder} with data got
     * from the Message object.
     *
     * @param message Message object containing the message data
     */
    public void bindMessage(Message message) {

        if (message.getMessageUser() != null) {
            messengerTextView.setText(message.getMessageUser());
            messengerTextView.setVisibility(TextView.VISIBLE);
        }

        // Handling the text message
        if (message.getMessageText() != null) {
            messageTextView.setText(message.getMessageText());
            messageTextView.setVisibility(TextView.VISIBLE);
            // Handling the image message
        } else if (message.getImageUrl() != null) {
            handlingImageMessage(message);

            messageImageView.setVisibility(ImageView.VISIBLE);
            messageTextView.setVisibility(TextView.GONE);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String messageTimeString = simpleDateFormat.format(message.getMessageTime());
        messageTimeTextView.setText(messageTimeString);
        messageTimeTextView.setVisibility(TextView.VISIBLE);
    }

    /**
     * Download image message to be displayed in the {@link MessageViewHolder} from Firebase Storage
     *
     * @param message Message object containing the message data (in this case the message data of
     *                interest is the URL of the image)
     */
    private void handlingImageMessage(Message message) {
        String imageUrl = message.getImageUrl();
        if (imageUrl.startsWith("gs://")) {

            StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(imageUrl);

            storageReference.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            Glide.with(messageImageView.getContext())
                                    .load(downloadUrl)
                                    .into(messageImageView);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Getting download url was not successful.", e);
                        }
                    });
        } else {
            Glide.with(messageImageView.getContext())
                    .load(message.getImageUrl())
                    .into(messageImageView);
        }
    }

}

