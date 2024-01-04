package SandZ.Tutors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;



public class TeacherTerms extends AppCompatActivity {
    private FirebaseManager manager;
    private CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_terms);
        manager = new FirebaseManager(this);
        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                String selectedDate = year + "-" + (month + 1) + "-" + day;
                Toast.makeText(TeacherTerms.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TeacherTerms.this, TermsView.class);
                intent.putExtra("user", manager.getCurrentUser());
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                startActivity(intent);
                finish();
            }
        });
    }

}
