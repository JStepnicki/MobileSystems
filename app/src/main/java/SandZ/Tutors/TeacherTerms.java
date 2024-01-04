package SandZ.Tutors;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TeacherTerms extends AppCompatActivity {
    private FirebaseManager manager;
    private EditText dateEditText, timeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_terms);
        manager = new FirebaseManager(this);
        CalendarView calendarView = findViewById(R.id.calendarView);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        Button addButton = findViewById(R.id.addTermButton);
        calendarView.setOnDateChangeListener((view, year, month, day) -> {
            String selectedDate = year + "-" + (month + 1) + "-" + day;
            Toast.makeText(TeacherTerms.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TeacherTerms.this, TermsView.class);
            intent.putExtra("user", manager.getCurrentUser());
            intent.putExtra("year", year);
            intent.putExtra("month", month);
            intent.putExtra("day", day);
            startActivity(intent);
            finish();
        });

        addButton.setOnClickListener(v -> {
            String selectedDate = dateEditText.getText().toString();
            String selectedTime = timeEditText.getText().toString();

            Timestamp timestamp = prepareTimestamp(selectedDate, selectedTime);

            if (timestamp != null) {
                manager.addTermToFirebase(manager.getCurrentUser().getUid(), timestamp,false);
            } else {
                Toast.makeText(TeacherTerms.this, "Invalid date or time format", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Timestamp prepareTimestamp(String date, String time) {
        try {
            String dateTimeStr = date + " " + time;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            // Utwórz obiekt Calendar
            Calendar calendar = Calendar.getInstance();
            // Parsuj datę i godzinę
            Date parsedDate = sdf.parse(dateTimeStr);
            // Ustaw datę i godzinę w obiekcie Calendar
            if (parsedDate != null) {
                calendar.setTime(parsedDate);
                // Odjęcie godziny
                calendar.add(Calendar.HOUR_OF_DAY, -1);
                // Pobranie zaktualizowanej daty i godziny
                parsedDate = calendar.getTime();
            }
            return new Timestamp(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
