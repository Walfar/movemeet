package com.sdp.movemeet.Backend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Sport;
import com.sdp.movemeet.R;

import java.util.ArrayList;
import java.util.Date;

public class BackendActivityManagerDemo extends AppCompatActivity {

    private FirebaseFirestore db;
    private BackendActivityManager bam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backend_manager_demo);

        db = FirebaseFirestore.getInstance();
        bam = new BackendActivityManager(db, "debug");
    }

    public void uploadActivity(View view) {
        EditText editText = findViewById(R.id.editHostUpload);
        String host = editText.getText().toString();
        if (host.isEmpty()) host = "NO_HOST_GIVEN";

        Activity act = new Activity("activity",
                host,
                "title",
        10,
        new ArrayList<String>(),
        0,
        0,
        "desc",
        new Date(),
        10,
        Sport.Running,
        "address");

        TextView res = findViewById(R.id.searchResult);

        bam.uploadActivity(act,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        res.setText("Successfully uploaded activity");
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        res.setText("Could not upload activity");
                    }
                });
    }

    public void searchActivity(View view) {
        EditText editText = findViewById(R.id.editHostSearch);
        String host = editText.getText().toString();

        Query q = bam.getActivitiesCollectionReference()
                .whereEqualTo("organizerId", host)
                //.orderBy("time")
                .limit(1);

        TextView res = findViewById(R.id.searchResult);
        res.setText("No result found");

        q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot docSnap: queryDocumentSnapshots.getDocuments()) {
                    res.setText(docSnap.get("organizerId") + "\n" + docSnap.get("date"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                res.setText("Failed to retrieve data");
            }
        });
    }

    public void deleteActivity(View view) {
        EditText editText = findViewById(R.id.editHostDelete);
        String host = editText.getText().toString();

        Query q = bam.getActivitiesCollectionReference()
                .whereEqualTo("organizerId", host)
                //.orderBy("time")
                .limit(1);

        TextView res = findViewById(R.id.searchResult);

        q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.getDocuments().isEmpty()) {
                    Activity act = new Activity("",
                            queryDocumentSnapshots.getDocuments().get(0).get("organizerId").toString(),
                            "title",
                            10,
                            new ArrayList<String>(),
                            0,
                            0,
                            "desc",
                            new Date(),
                            10,
                            Sport.Running,
                            "address");
                    act.setBackendRef(queryDocumentSnapshots.getDocuments().get(0).getReference());

                    bam.deleteActivity(act, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    res.setText("Successfully deleted activity");
                                }
                            },
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    res.setText("Failed to delete activity");
                                }
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                res.setText("Failed to retrieve activity to be deleted");
            }
        });
    }
}