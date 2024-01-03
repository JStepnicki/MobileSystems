package SandZ.Tutors;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Meetings extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseManager manager;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<String> meetingList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        setContentView(R.layout.activity_meetings);

        getMeetingsForCurrentUser(user.getUid(), successListener, failureListener);
    }

    private void getMeetingsForCurrentUser(String userId, OnSuccessListener<ArrayList<String>> successListener, OnFailureListener failureListener) {
        ArrayList<String> meetings = new ArrayList<>();


        db.collection("users").document(userId).collection("meetings")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            meetings.add(document.getId());
                        }
                        successListener.onSuccess(meetings);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failureListener.onFailure(e);
                    }
                });
    }

    private OnSuccessListener<ArrayList<String>> successListener = new OnSuccessListener<ArrayList<String>>() {
        @Override
        public void onSuccess(ArrayList<String> meetings) {
            if (meetings.isEmpty()) {
                Toast.makeText(Meetings.this, "Brak spotkań", Toast.LENGTH_SHORT).show();
            } else {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Meetings.this, android.R.layout.simple_list_item_1, meetings);
                ListView meetingView = findViewById(R.id.meetingView);
                meetingView.setAdapter(arrayAdapter);
            }
        }
    };

    private OnFailureListener failureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(Meetings.this, "Błąd: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    };
}
