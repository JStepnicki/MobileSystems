package SandZ.Tutors.activites;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;

import SandZ.Tutors.R;
import SandZ.Tutors.database_handlers.FirebaseManager;



public class StudentMainView extends AppCompatActivity {
    Button btnLogout, btnMeetings, btnSearch;

    ImageView profilePic;
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
        profilePic = findViewById(R.id.ProfilePicStudent);


        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginView.class);
            startActivity(intent);
            finish();
        }

        profilePic.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SelectProfilePictureView.class);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(v -> {
            manager.signOut();
            Intent intent = new Intent(getApplicationContext(), LoginView.class);
            startActivity(intent);
            finish();
        });

        btnMeetings.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MeetingsView.class);
            startActivity(intent);
        });
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), TutorBrowserView.class);
            startActivity(intent);
        });}
    @Override
    protected void onStart() {
        super.onStart();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginView.class);
            startActivity(intent);
            finish();
        }
        manager.getUserData("name", data -> name.setText(data));

        manager.getUserData("surname", data -> name.setText(name.getText() + " " + data));

        manager.getImage(manager.getCurrentUser().getUid(),
                picture -> {
                    if (picture == 0) {
                        profilePic.setImageResource(R.mipmap.annonym);
                    } else {
                        profilePic.setImageResource(picture);

                    }
                },
                e -> {
                });


    }
}