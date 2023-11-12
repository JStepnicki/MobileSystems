package SandZ.Tutors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btnLogout;
    TextView email, userType;
    FirebaseUser user;
    private FirebaseFirestore mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        btnLogout = findViewById(R.id.btn_logout);
        email = findViewById(R.id.user_email);
        userType = findViewById(R.id.user_type);
        user = mAuth.getCurrentUser();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DocumentReference userRef = mDatabase.collection("users").document(userId);
            // Fetch the document
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Retrieve email and userType from the document
                            String userEmail = document.getString("email");
                            String userUserType = document.getString("userType");

                            // Display the data in your TextViews or wherever you need it
                            email.setText(userEmail);
                            userType.setText(userUserType);
                        } else {
                            Toast.makeText(MainActivity.this, "No such document.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            email.setText(user.getEmail());
            userType.setText(user.getDisplayName());
        }

    }
}