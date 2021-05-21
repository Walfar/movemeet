package com.sdp.movemeet.view.profile;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * This class allows to check whether two ImageViews (more precisely their contents) are matching or not.
 * It has as field the id {@link DrawableMatcher#expectedId} of the expected drawable. This class is
 * directly taken from the Medium article "Android UI Test - Espresso Matcher for ImageView".
 * @see <a href="https://medium.com/@dbottillo/android-ui-test-espresso-matcher-for-imageview-1a28c832626f">
 *     Android UI Test - Espresso Matcher for ImageView</a> for more information.
 */
public class DrawableMatcher extends TypeSafeMatcher<View> {

    private final int expectedId;
    private String resourceName;
    static final int EMPTY = -1;
    static final int ANY = -2;

    DrawableMatcher(int expectedId) {
        super(View.class);
        this.expectedId = expectedId;
    }

    /**
     * Method that performs the actual ImageView matching test.
     *
     * @param target Target View which context is accessed to create the expected Drawable that is
     *               then compared to the target ImageView via Bitmap conversion.
     */
    @Override
    protected boolean matchesSafely(View target) {
        if (!(target instanceof ImageView)) {
            return false;
        }
        ImageView imageView = (ImageView) target;
        if (expectedId == EMPTY) {
            return imageView.getDrawable() == null;
        }
        if (expectedId == ANY) {
            return imageView.getDrawable() != null;
        }
        Resources resources = target.getContext().getResources();
        Drawable expectedDrawable = resources.getDrawable(expectedId);
        resourceName = resources.getResourceEntryName(expectedId);

        if (expectedDrawable == null) {
            return false;
        }

        Bitmap bitmap = getBitmap(imageView.getDrawable());
        Bitmap otherBitmap = getBitmap(expectedDrawable);
        return bitmap.sameAs(otherBitmap);
    }

    /**
     * Method that creates a Bitmap from a Drawable object.
     *
     * @param drawable Drawable object from which a Bitmap is created.
     */
    private Bitmap getBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * This method is used by espresso when it needs to add a description for the output's matcher.
     *
     * @param description Output description to add to the matcher.
     */
    @Override
    public void describeTo(Description description) {
        description.appendText("with drawable from resource id: ");
        description.appendValue(expectedId);
        if (resourceName != null) {
            description.appendText("[");
            description.appendText(resourceName);
            description.appendText("]");
        }
    }
}