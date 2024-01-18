package SandZ.Tutors.activites;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import SandZ.Tutors.R;
import SandZ.Tutors.data.classes.Meeting;
import SandZ.Tutors.data_adapters.MeetingAdapter;
import SandZ.Tutors.database_handlers.FirebaseManager;

public class MeetingsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);
        FirebaseManager manager = new FirebaseManager(this);
    }

    @Override
    protected void onStart() {
        FirebaseManager manager = new FirebaseManager(this);
        super.onStart();
        manager.getMeetingsForCurrentUser(successListener, failureListener);
    }

    private final OnSuccessListener<ArrayList<Meeting>> successListener = meetings -> {
        if (meetings.isEmpty()) {
            Toast.makeText(MeetingsView.this, "No meetings", Toast.LENGTH_SHORT).show();
        } else {
            MeetingAdapter meetingAdapter = new MeetingAdapter(MeetingsView.this, meetings);
            ListView meetingListView = findViewById(R.id.meetingView);
            meetingListView.setAdapter(meetingAdapter);

            // Add item click listener
            meetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Meeting selectedMeeting = meetings.get(position);
                    showLinkDialog(selectedMeeting.getLink());
                }
            });
        }
    };

    private final OnFailureListener failureListener = e -> {
        Toast.makeText(MeetingsView.this, "Błąd: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    };

    private void showLinkDialog(String link) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Meeting Link:")
                .setMessage(link)
                .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        copyToClipboard(link);
                    }
                });
        builder.create().show();
    }

    private void copyToClipboard(String textToCopy) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Meeting Link", textToCopy);
        clipboardManager.setPrimaryClip(clip);
        Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}
