package SandZ.Tutors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private TextInputEditText editTextEmail, editTextPassword;
    private Switch userTypeSwitch;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private TextView switchToLogin;
    private FirebaseFirestore mDatabase;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        userTypeSwitch = findViewById(R.id.userTypeSwitch);

        btnRegister = findViewById(R.id.btn_register);
        switchToLogin = findViewById(R.id.go_login);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
//
//                                    Toast.makeText(Register.this, "Account created.",
//                                            Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(getApplicationContext(), Login.class);
//                                    startActivity(intent);
//                                    finish();

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        // Create a user object with additional details
                                        Map<String, Object> userData = new HashMap<>();
                                        userData.put("email", email);
                                        userData.put("userType", userTypeSwitch.isChecked() ? "teacher" : "student");

                                        // Store the user in Firestore
                                        mDatabase.collection("users").document(user.getUid())
                                                .set(userData, SetOptions.merge())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Register.this, "Account created and user information added to Firestore.", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getApplicationContext(), Login.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(Register.this, "Account created, but failed to add user information to Firestore.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(Register.this, "User is null.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        switchToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

    }
}