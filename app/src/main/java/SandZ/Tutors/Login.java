package SandZ.Tutors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    private Button btnLogin;
    private FirebaseManager manager;
    private TextInputEditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private TextView switchToRegister;

private void getUserTypeFromFirebase(String userId) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    db.collection("users")
            .document(userId)
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Assuming "userType" is the field in Firestore containing the user type
                            String userType = document.getString("userType");

                            if (userType != null) {
                                // Redirect based on user type
                                if ("student".equals(userType)) {
                                    Intent intent = new Intent(getApplicationContext(), Student.class);
                                    startActivity(intent);
                                } else if ("teacher".equals(userType)) {
                                    Intent intent = new Intent(getApplicationContext(), Teacher.class);
                                    startActivity(intent);
                                } else {
                                    // Handle other user types or show an error message
                                    Toast.makeText(Login.this, "Unknown user type", Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            } else {
                                // Handle the case where "userType" is not present in Firestore
                                Toast.makeText(Login.this, "User type not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle the case where the document does not exist
                            Toast.makeText(Login.this, "User document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle exceptions while fetching data from Firestore
                        Toast.makeText(Login.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
}
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            getUserTypeFromFirebase(currentUser.getUid());
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseFirestore.getInstance(); niepotrzebne na razie
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        switchToRegister = findViewById(R.id.go_register);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        getUserTypeFromFirebase(user.getUid());
                                        finish();
                                    } else {
                                        Toast.makeText(Login.this, "Unable to retrieve user information", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });




        switchToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });



    }
}