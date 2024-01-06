package SandZ.Tutors.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import SandZ.Tutors.R;
import SandZ.Tutors.data.classes.TeacherClass;

public class TeacherAccountView extends AppCompatActivity {
    private TextView textView6;
    private ImageView profilePicture;
    private Button reserveTermButton;
    private TextView nameText;
    private TextView surnameText;
    private RatingBar ratingBar;
    private TextView subjectListView;
    private TeacherClass teacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_account_view);

        Intent receivedIntent = getIntent();
        teacher = (TeacherClass) receivedIntent.getExtras().get("teacher");

        profilePicture = findViewById(R.id.profile_picutre);
        reserveTermButton = findViewById(R.id.reserve_term);
        nameText = findViewById(R.id.name_text);
        surnameText = findViewById(R.id.surname_text);
        ratingBar = findViewById(R.id.ratingBar);
        subjectListView = findViewById(R.id.subject_list_view);

        nameText.setText(teacher.getName());
        surnameText.setText(teacher.getSurname());
        ratingBar.setRating(5);//tu trzeba jebnac obliczanie sredniej ocen

        List<String> subjects = teacher.getSubjects();

        for (int i = 0; i < subjects.size(); i++) {
            subjectListView.append(subjects.get(i));
            if (i < subjects.size() - 1) {
                subjectListView.append(", ");
            }
            if ((i + 1) % 3 == 0 && i < subjects.size() - 1) {
                subjectListView.append("\n");
            }
        }

        // Dodajemy obsługę zdarzeń
        reserveTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obsługa kliknięcia przycisku "Show terms calendar"
                // Tutaj możesz dodać odpowiednią logikę obsługi zdarzenia
            }
        });

        // Możesz dodać obsługę zdarzeń dla innych elementów, jeśli to potrzebne
    }
}
