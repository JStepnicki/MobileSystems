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

// Login.java
public class Login extends AppCompatActivity {
    private Button btnLogin;
    private FirebaseManager manager;
    private TextInputEditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private TextView switchToRegister;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            manager.getUserData("userType", new OnDataRetrievedListener() {
                @Override
                public void onDataRetrieved(String data) {
                    if (!TextUtils.isEmpty(data)) {
                        // Redirect based on user type
                        if ("student".equals(data)) {
                            Intent intent = new Intent(Login.this, Student.class);
                            startActivity(intent);
                        } else if ("teacher".equals(data)) {
                            Intent intent = new Intent(Login.this, Teacher.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Login.this, "Unknown user type", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    } else {
                        Toast.makeText(Login.this, "User type not found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        manager = new FirebaseManager(this);
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
                                    manager.getUserData("userType", new OnDataRetrievedListener() {
                                        @Override
                                        public void onDataRetrieved(String data) {
                                            if (!TextUtils.isEmpty(data)) {
                                                // Redirect based on user type
                                                if ("student".equals(data)) {
                                                    Intent intent = new Intent(Login.this, Student.class);
                                                    startActivity(intent);
                                                } else if ("teacher".equals(data)) {
                                                    Intent intent = new Intent(Login.this, Teacher.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(Login.this, "Unknown user type", Toast.LENGTH_SHORT).show();
                                                }
                                                finish();
                                            } else {
                                                Toast.makeText(Login.this, "User type not found", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
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
