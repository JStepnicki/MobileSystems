package SandZ.Tutors.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import SandZ.Tutors.R;
import SandZ.Tutors.data.classes.TeacherClass;
import SandZ.Tutors.data.classes.Term;
import SandZ.Tutors.database_handlers.FirebaseManager;
import SandZ.Tutors.database_handlers.OnDataRetrievedListener;

public class ReserveTermView extends AppCompatActivity {
    private FirebaseManager manager;
    private List<Term> termList;
    private ArrayList<String> terms_hours;
    private TeacherClass teacher;
    private String studentName;
    private int SelectedYear, SelectedMonth, SelectedDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_term_view);

        Intent receivedIntent = getIntent();
        teacher = (TeacherClass) receivedIntent.getExtras().get("teacher");

        manager = new FirebaseManager(this);
        terms_hours = new ArrayList<>();

        CalendarView calendarView = findViewById(R.id.calendarView2);
        Calendar today = Calendar.getInstance();
        SelectedYear = today.get(Calendar.YEAR);
        SelectedMonth = today.get(Calendar.MONTH);
        SelectedDay = today.get(Calendar.DAY_OF_MONTH);

        calendarView.setOnDateChangeListener((view, year, month, day) -> {
            SelectedYear = year;
            SelectedMonth = month;
            SelectedDay = day;
            manager.getTermsForTeacher(teacher.getId(), successListener);
        });
        termList = new ArrayList<>();
        manager.getUserData("name", new OnDataRetrievedListener() {
            @Override
            public void onDataRetrieved(String data) {
                studentName = data;
            }
        });


    }
    private final OnSuccessListener<ArrayList<Term>> successListener = new OnSuccessListener<ArrayList<Term>>() {
        @Override
        public void onSuccess(ArrayList<Term> terms) {
            termList.clear();
            terms_hours.clear();

            for (Term term : terms) {
                if (term.checkDate(SelectedYear, SelectedMonth, SelectedDay) && !term.isBooked()) {
                    termList.add(term);
                    terms_hours.add(term.getTimeAsString());
                }
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ReserveTermView.this, android.R.layout.simple_list_item_1, terms_hours);
            ListView listView = findViewById(R.id.availableTermsListView);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                Term selectedTerm = termList.get(position);
                manager.addMeetingForTeacherAndStudent(teacher.getId(), manager.getCurrentUser().getUid(), selectedTerm.getTimestamp(),selectedTerm.getLink(),teacher.getName(),studentName);
                Toast.makeText(ReserveTermView.this, "Selected Term: " + selectedTerm.getTimeAsString(), Toast.LENGTH_SHORT).show();
                terms_hours.remove(position);
                termList.remove(position);
                arrayAdapter.notifyDataSetChanged();
                finish();
            });
        }
    };
    private final OnFailureListener failureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(ReserveTermView.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    };
}