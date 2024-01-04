package SandZ.Tutors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class TermsView extends AppCompatActivity {
    private FirebaseManager manager;
    private ArrayList<Term> user_terms;
    private TextView date;
    private FirebaseUser user;
    private ArrayList<String> terms_hours;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_view);
        manager = new FirebaseManager(this);

        Intent receivedIntent = getIntent();
        user = (FirebaseUser) receivedIntent.getExtras().get("user");
        year = (int) receivedIntent.getExtras().get("year");
        month = (int) receivedIntent.getExtras().get("month");
        day = (int) receivedIntent.getExtras().get("day");

        user_terms = new ArrayList<>();
        terms_hours = new ArrayList<>();

        date = findViewById(R.id.data_display);
        date.setText("Your terms at " + year + "-" + (month + 1) + "-" + day);

        manager.getTermsForTeacher(user.getUid(), successListener, failureListener);
    }

    private OnSuccessListener<ArrayList<Term>> successListener = new OnSuccessListener<ArrayList<Term>>() {
        @Override
        public void onSuccess(ArrayList<Term> terms) {
            if (terms.isEmpty()) {
                terms_hours.add("No terms");
            } else {
                for (Term term : terms) {
                    if (term.checkDate(year, month, day)) {
                        user_terms.add(term);
                        terms_hours.add(term.getTimeAsString());
                    }
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(TermsView.this, android.R.layout.simple_list_item_1, terms_hours);
                ListView listView = findViewById(R.id.listView);
                listView.setAdapter(arrayAdapter);
            }
        }
    };

    private OnFailureListener failureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(TermsView.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    };
}
