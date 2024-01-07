package SandZ.Tutors.activites;


import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import SandZ.Tutors.R;
import SandZ.Tutors.data.classes.TeacherClass;
import SandZ.Tutors.database_handlers.FirebaseManager;
import SandZ.Tutors.database_handlers.OnDataRetrievedListener;
import SandZ.Tutors.database_handlers.TeacherCallback;


public class TeacherMainView extends AppCompatActivity {

    Button btnLogout, btnTeacherMeetings, btnTerms, btnTeacherSubjects, btnPrice;
    TextView credentials;
    FirebaseUser user;
    private TextView subjectsTextView;
    private RatingBar ratingBar;
    private ImageView profilePictureView;
    private TeacherClass teacher_object;
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
        btnPrice = findViewById(R.id.set_price);
        credentials = findViewById(R.id.credentials);
        ratingBar = findViewById(R.id.ratingBar2);
        subjectsTextView = findViewById(R.id.subjectList);
        user = manager.getCurrentUser();
        profilePictureView = findViewById(R.id.profilePictureTeacher);

        profilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectProfilePictureView.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(v -> {
            manager.signOut();
            Intent intent = new Intent(getApplicationContext(), LoginView.class);
            startActivity(intent);
            finish();
        });
        btnTeacherMeetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MeetingsView.class);
                startActivity(intent);
            }
        });
        btnTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TeacherTermsView.class);
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
        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TeacherMainView.this);
                builder.setTitle("Enter Price");

                final EditText input = new EditText(TeacherMainView.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String enteredValue = input.getText().toString();
                        try {
                            int priceValue = Integer.parseInt(enteredValue);
                            manager.updatePrice(manager.getCurrentUser().getUid(), priceValue);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });


        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginView.class);
            startActivity(intent);
            finish();
        }
        manager.getTeacherById(manager.getCurrentUser().getUid(), new TeacherCallback() {
            @Override
            public void onTeacherReceived(TeacherClass teacher) {
                if (teacher != null) {
                    teacher_object = teacher;
                    credentials.setText(teacher.getName() + " " + teacher.getSurname());
                    ratingBar.setRating(teacher.getRate());

                    List<String> subjects = teacher.getSubjects();
                    subjectsTextView.setText("My subjects:\n");
                    for (int i = 0; i < subjects.size(); i++) {
                        subjectsTextView.append(subjects.get(i));
                        if (i < subjects.size() - 1) {
                            subjectsTextView.append(", ");
                        }
                        if ((i + 1) % 3 == 0 && i < subjects.size() - 1) {
                            subjectsTextView.append("\n");
                        }
                    }

                    if(teacher.getPicture() == 0) {
                        profilePictureView.setImageResource(R.mipmap.avatar);
                    }
                    else{
                        profilePictureView.setImageResource(teacher.getPicture());
                    }

                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginView.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }
}