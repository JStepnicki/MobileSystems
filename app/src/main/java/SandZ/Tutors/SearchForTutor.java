package SandZ.Tutors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchForTutor extends AppCompatActivity {
    private FirebaseManager manager;
    private Button subjectButton;

    private ListView tutorListView;
    private TutorListAdapter adapter;
    private Context context;
    private List<TeacherClass> teachers;
    private List<String> subjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_tutor);
        manager = new FirebaseManager(this);
        subjects = new ArrayList<>();
        subjectButton = findViewById(R.id.subject_button);
        tutorListView = findViewById(R.id.tutorListView);
        context = this;
        teachers = new ArrayList<TeacherClass>();

        subjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });

        manager.getSubjectList(new SubjectListCallback() {
            @Override
            public void onSubjectListReceived(List<String> receivedSubjects) {
                subjects = receivedSubjects;
            }
        });
        manager.getTeacherList(new TeacherListCallback() {
            @Override
            public void onTeacherListReceived(List<TeacherClass> receivedTeachers) {
                adapter = new TutorListAdapter(context, receivedTeachers);
                tutorListView.setAdapter(adapter);
            }
        });
    }

    private void showFilterDialog() {
        if(subjects == null)
            return;
        final List<String> filterOptions = subjects; // Zakładam, że masz metodę do pobierania dynamicznej listy opcji

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose subject")
                .setItems(filterOptions.toArray(new CharSequence[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle the click on individual items if needed
                        String selectedOption = filterOptions.get(i);
                        subjectButton.setText(selectedOption);

                    }
                });

        builder.create().show();
    }


}
