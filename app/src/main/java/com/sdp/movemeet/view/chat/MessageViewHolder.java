package com.sdp.movemeet.view.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.models.Message;
import com.sdp.movemeet.utility.ImageHandler;

import java.text.SimpleDateFormat;

/**
 * Objects of this class embed a message in an adapted view.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {

    private final TextView messengerTextView;
    private final TextView messageTextView;
    private final ImageView messageImageView;
    private final ImageView userProfilePicture;
    private final TextView messageTimeTextView;

    /**
     * Reference the message data in UI elements (TextViews and ImageView)
     *
     * @param v View of the message
     */
    public MessageViewHolder(View v) {
        super(v);
        messengerTextView = itemView.findViewById(R.id.message_user);
        messageTextView = itemView.findViewById(R.id.message_text);
        messageImageView = itemView.findViewById(R.id.messageImageView);
        userProfilePicture = itemView.findViewById(R.id.imageProfile);
        messageTimeTextView = itemView.findViewById(R.id.message_time);
    }

    /**
     * Populate the different message views composing the {@link MessageViewHolder} with data got
     * from the Message object.
     *
     * @param message Message object containing the message data
     */
    public void bindMessage(Message message, ChatActivity chatActivity) {
        // Handling the view for the author of the message
        if (message.getMessageUser() != null) {
            messengerTextView.setText(message.getMessageUser());
            messengerTextView.setVisibility(TextView.VISIBLE);
        }
        // Handling the view for the text message
        if (message.getMessageText() != null) {
            messageTextView.setText(message.getMessageText());
            messageTextView.setVisibility(TextView.VISIBLE);
            messageImageView.setVisibility(ImageView.GONE);
        }
        // Handling the ImageView of the image message
        if (message.getImageUrl() != null && message.getImageUrl().contains("http")) {
            handlingImageMessage(message, chatActivity);
            messageImageView.setVisibility(ImageView.VISIBLE);
            messageTextView.setVisibility(TextView.GONE);
        }
        // Handling the ImageView of the user profile picture in any case
        handlingUserProfilePicture(message, chatActivity);
        // Handling the date view of the message
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String messageTimeString = simpleDateFormat.format(Long.valueOf(message.getMessageTime()));
        messageTimeTextView.setText(messageTimeString);
        messageTimeTextView.setVisibility(TextView.VISIBLE);
    }

    /**
     * Download user profile picture to be displayed in the {@link MessageViewHolder} from Firebase Storage
     *
     * @param message Message object containing the message data (in this case the message data of
     *                interest is the URL of the image)
     */
    private void handlingUserProfilePicture(Message message, ChatActivity chatActivity) {
        String userImagePath = FirestoreUserManager.USERS_COLLECTION + ImageHandler.PATH_SEPARATOR
                + message.getMessageUserId() + ImageHandler.PATH_SEPARATOR + ImageHandler.USER_IMAGE_NAME;
        Image image = new Image(null, userProfilePicture);
        image.setDocumentPath(userImagePath);
        ImageHandler.loadImage(image, chatActivity);
    }

    /**
     * Download image message to be displayed in the {@link MessageViewHolder} from Firebase Storage
     *
     * @param message Message object containing the message data (in this case the message data of
     *                interest is the URL of the image)
     */
    private void handlingImageMessage(Message message, ChatActivity chatActivity) {
        String imageUrl = message.getImageUrl();

        if (imageUrl.equals(ChatActivity.LOADING_IMAGE_URL)) {
            // Temporarily uploading the loading wheel to Firebase Storage
            Glide.with(messageImageView.getContext())
                    .load(message.getImageUrl())
                    .into(messageImageView);
        } else {
            String imagePath = ImageHandler.convertURLtoPath(imageUrl);
            Image image = new Image(null, messageImageView);
            image.setDocumentPath(imagePath);
            ImageHandler.loadImage(image, chatActivity);
        }
    }

}

