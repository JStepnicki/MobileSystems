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


import java.util.ArrayList;
import java.util.Date;

public class Meetings extends AppCompatActivity {

    private FirebaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_meetings);
        manager = new FirebaseManager(this);
        manager.getMeetingsForCurrentUser(successListener, failureListener);
    }


    private OnSuccessListener<ArrayList<Meeting>> successListener = new OnSuccessListener<ArrayList<Meeting>>() {
        @Override
        public void onSuccess(ArrayList<Meeting> meetings) {
            if (meetings.isEmpty()) {
                Toast.makeText(Meetings.this, "Brak spotkań", Toast.LENGTH_SHORT).show();
            } else {
                ArrayList<String> meetingLinks = new ArrayList<>();
                for (Meeting meeting : meetings) {
                    String link = meeting.getLink();
                    if (link != null && !link.isEmpty()) {
                        meetingLinks.add(link);
                    }
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Meetings.this, android.R.layout.simple_list_item_1, meetingLinks);
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
