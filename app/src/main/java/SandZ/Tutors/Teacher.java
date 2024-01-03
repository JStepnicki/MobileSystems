package SandZ.Tutors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Teacher extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btnLogout, btnTeacherMeetings;
    TextView email, userType;
    FirebaseUser user;
    private FirebaseManager manager;
    private FirebaseFirestore mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        manager = new FirebaseManager(this);
        btnLogout = findViewById(R.id.btn_logout);
        btnTeacherMeetings = findViewById(R.id.btn_teacher_meetings);
        email = findViewById(R.id.user_email);
        userType = findViewById(R.id.user_type);
        user = manager.getCurrentUser();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        btnTeacherMeetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Meetings.class);
                startActivity(intent);
                finish();
            }
        });
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            manager.getUserData("email", new OnDataRetrievedListener() {
                @Override
                public void onDataRetrieved(String data) {
                    email.setText(data);
                }
            });
            manager.getUserData("userType", new OnDataRetrievedListener() {
                @Override
                public void onDataRetrieved(String data) {
                    userType.setText(data);
                }
            });
        }

    }
}