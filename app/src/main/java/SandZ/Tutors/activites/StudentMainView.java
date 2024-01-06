package SandZ.Tutors.activites;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;

import SandZ.Tutors.R;
import SandZ.Tutors.database_handlers.FirebaseManager;
import SandZ.Tutors.database_handlers.OnDataRetrievedListener;


public class StudentMainView extends AppCompatActivity {
    Button btnLogout, btnMeetings, btnSearch;
    TextView name;
    FirebaseUser user;
    private FirebaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        manager = new FirebaseManager(this);
        FirebaseApp.initializeApp(this);
        btnLogout = findViewById(R.id.btn_logout_student);
        btnMeetings = findViewById(R.id.btn_meetings);
        btnSearch = findViewById(R.id.go_to_search);
        name = findViewById(R.id.user_name_student);
        user = manager.getCurrentUser();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginView.class);
                startActivity(intent);
                finish();
            }
        });

        btnMeetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MeetingsView.class);
                startActivity(intent);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TutorBrowserView.class);
                startActivity(intent);
            }
        });

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginView.class);
            startActivity(intent);
            finish();
        }
        manager.getUserData("name", new OnDataRetrievedListener() {
            @Override
            public void onDataRetrieved(String data) {
                name.setText(data);
            }
        });


    }
}