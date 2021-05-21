package com.sdp.movemeet.models;

import android.net.Uri;
import android.widget.ImageView;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ImageTest {

    private Uri DUMMY_IMAGE_URI = mock(Uri.class);
    private ImageView DUMMY_IMAGE_IMAGE_VIEW = mock(ImageView.class);
    private String DUMMY_DOCUMENT_PATH = "documentPath";


    @Test(expected = IllegalArgumentException.class)
    public void imageConstructorFailBothFieldsNull() {
        Image image = new Image(null, null);
    }

    @Test
    public void imageGetCorrect() {
        Image image = new Image(DUMMY_IMAGE_URI, DUMMY_IMAGE_IMAGE_VIEW);
        image.setDocumentPath(DUMMY_DOCUMENT_PATH);
        assertEquals(DUMMY_IMAGE_URI, image.getImageUri());
        assertEquals(DUMMY_IMAGE_IMAGE_VIEW, image.getImageView());
        assertEquals(DUMMY_DOCUMENT_PATH, image.getDocumentPath());
    }

    @Test
    public void imageSetImageUri() {
        Image image = new Image(DUMMY_IMAGE_URI, DUMMY_IMAGE_IMAGE_VIEW);
        image.setImageUri(DUMMY_IMAGE_URI);
        assertEquals(image.getImageUri(), DUMMY_IMAGE_URI);
    }

    @Test
    public void imageSetImageView() {
        Image image = new Image(DUMMY_IMAGE_URI, DUMMY_IMAGE_IMAGE_VIEW);
        image.setImageImageView(DUMMY_IMAGE_IMAGE_VIEW);
        assertEquals(image.getImageView(), DUMMY_IMAGE_IMAGE_VIEW);
    }

    @Test
    public void imageSetDoucumentPath() {
        Image image = new Image(DUMMY_IMAGE_URI, DUMMY_IMAGE_IMAGE_VIEW);
        image.setDocumentPath(DUMMY_DOCUMENT_PATH);
        assertEquals(image.getDocumentPath(), DUMMY_DOCUMENT_PATH);
    }

}
