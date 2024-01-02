package SandZ.Tutors;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class Meetings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);

        // Inicjalizacja ListView
        ListView meetingView = findViewById(R.id.meetingView);

        // Tworzenie testowej listy spotkań
        ArrayList<String> meetingList = new ArrayList<String>();
        meetingList.add("test1");
        meetingList.add("test2");
        meetingList.add("test3");
        meetingList.add("test4");

        // Utworzenie ArrayAdapter i przypisanie go do ListView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, meetingList);
        meetingView.setAdapter(arrayAdapter);
    }
}
