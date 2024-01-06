package SandZ.Tutors;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;


public class Teacher extends AppCompatActivity {

    Button btnLogout, btnTeacherMeetings, btnTerms, btnTeacherSubjects;
    TextView email, userType;
    FirebaseUser user;
    private FirebaseManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        manager = new FirebaseManager(this);
        btnLogout = findViewById(R.id.btn_logout);
        btnTeacherMeetings = findViewById(R.id.btn_teacher_meetings);
        btnTerms = findViewById(R.id.teacher_terms);
        btnTeacherSubjects = findViewById(R.id.teacher_subjects);
        email = findViewById(R.id.user_email);
        userType = findViewById(R.id.user_type);
        user = manager.getCurrentUser();
        btnLogout.setOnClickListener(v -> {
            manager.signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });
        btnTeacherMeetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Meetings.class);
                startActivity(intent);
            }
        });
        btnTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TeacherTerms.class);
                startActivity(intent);
            }
        });
        btnTeacherSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SubjectView.class);
                startActivity(intent);
            }
        });
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            manager.getUserData("email", data -> email.setText(data));
            manager.getUserData("userType", data -> userType.setText(data));
        }
    }
}