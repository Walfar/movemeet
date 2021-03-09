package com.sdp.movemeet.Backend;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.Activity.Activity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class BackendActivityManagerDebugTest {

    private final String DEBUG_COLLECTION = "debug";

    public static FirebaseFirestore db;
    public static BackendActivityManager bam;
    static Activity fakeActivity;

    @Rule
    public ActivityScenarioRule<BackendActivityManagerDebug> testRule = new ActivityScenarioRule<>(BackendActivityManagerDebug.class);

    @Before
    public void startUp() {
        Random rand = new Random();
        db = FirebaseFirestore.getInstance();
        bam = new BackendActivityManager(db, DEBUG_COLLECTION);
        fakeActivity = new Activity(
                "testID",
                rand.nextInt(),
                new Date(),
                rand.nextInt(),
                rand.nextInt()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void uploadActivityThrowsIllegalArgumentExceptionOnNullActivity() {
        bam.uploadActivity(null,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        assert (false);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        assert (false);
                    }
                });
    }

    @Test
    public void uploadActivityWorks() {
        CountDownLatch latch = new CountDownLatch(1);

        bam.uploadActivity(fakeActivity,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        latch.countDown();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        assert (false);
                    }
                }
        );

        try {
            latch.await();
        } catch (InterruptedException e) {
            assert (false);
        }

        assert (true);
    }

    @Test
    public void uploadActivityFailsOnUnauthorizedDBAccess() {
        bam = new BackendActivityManager(db, BackendActivityManager.ACTIVITIES_COLLECTION);

        CountDownLatch latch = new CountDownLatch(1);

        bam.uploadActivity(fakeActivity,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        assert (false);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        latch.countDown();
                    }
                }
        );

        try {
            latch.await();
        } catch (InterruptedException e) {
            assert (false);
        }

        assert (true);
    }

    @Test
    public void uploadActivityDoesNotOverWriteReference() {
        CountDownLatch latch = new CountDownLatch(2);


        bam.uploadActivity(fakeActivity,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        latch.countDown();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        assert (false);
                    }
                }
        );

        DocumentReference docRef = fakeActivity.getBackendRef();

        bam.uploadActivity(fakeActivity,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        latch.countDown();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        assert (false);
                    }
                }
        );

        try {
            latch.await();
        } catch (InterruptedException e) {
            assert (false);
        }

        assert (docRef.equals(fakeActivity.getBackendRef()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteActivityThrowsIllegalArgumentExceptionOnNullActivity() {
        bam.deleteActivity(null,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        assert (false);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        assert (false);
                    }
                });
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteActivityThrowsIllegalArgumentExceptionOnNullBackendReference() {
        bam.deleteActivity(fakeActivity,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        assert (false);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        assert (false);
                    }
                });
    }

    @Test
    public void deleteActivityWorks() {
        CountDownLatch latch = new CountDownLatch(2);

        bam.uploadActivity(fakeActivity,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        latch.countDown();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        assert (false);
                    }
                }
        );

        bam.deleteActivity(fakeActivity,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        latch.countDown();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        assert (false);
                    }
                });

        try {
            latch.await();
        } catch (InterruptedException e) {
            assert (false);
        }
    }
}
