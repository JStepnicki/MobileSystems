package SandZ.Tutors.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import SandZ.Tutors.R;
import SandZ.Tutors.data_adapters.SubjectAdapter;
import SandZ.Tutors.database_handlers.FirebaseManager;
import SandZ.Tutors.database_handlers.SubjectListCallback;

public class SubjectView extends AppCompatActivity {

    private ListView listView;
    private Button updateButton;

    private FirebaseManager manager;

    private SubjectAdapter adapter;
    
    private boolean subjectsReceived, teacherSubjectsReceived;

    private List<String> subjects, teacherSubjects, selectedSubjects;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_view);
        manager = new FirebaseManager(this);

        subjects = new ArrayList<>();
        teacherSubjects = new ArrayList<>();
        subjectsReceived = false;
        teacherSubjectsReceived = false;
        
        listView = findViewById(R.id.subjectListView);
        updateButton = findViewById(R.id.button);


        context = this;
        manager.getSubjectList(new SubjectListCallback() {
            @Override
            public void onSubjectListReceived(List<String> receivedSubjects) {
                subjects = receivedSubjects;
                subjectsReceived = true;
                if(teacherSubjectsReceived)
                    initializeAdapter();
            }
        });

        manager.getTeacherSubjects(manager.getCurrentUser().getUid(),
                new OnSuccessListener<ArrayList<String>>() {
                    @Override
                    public void onSuccess(ArrayList<String> receivedSubjects) {
                        teacherSubjects = receivedSubjects;
                        teacherSubjectsReceived = true;
                        if(subjectsReceived)
                            initializeAdapter();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedSubjects = adapter.getSelectedSubjects();
                manager.updateSubjects(manager.getCurrentUser().getUid(), selectedSubjects,context);
                }
        });
    }


    private void initializeAdapter() {
            adapter = new SubjectAdapter(this, subjects, teacherSubjects);
            listView.setAdapter(adapter);
        }
    }

