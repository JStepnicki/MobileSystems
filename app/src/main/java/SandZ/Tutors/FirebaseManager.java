package SandZ.Tutors;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseManager {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Context context;

    private FirebaseUser user;

    public FirebaseManager(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
    }



    public void signOut() {
        mAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return user;
    }


    public void getUserData(String fieldName, OnDataRetrievedListener listener) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            DocumentReference userRef = db.collection("users").document(user.getUid());

            // Fetch the document
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String data = document.getString(fieldName);
                            listener.onDataRetrieved(data != null ? data : "");
                        } else {
                            listener.onDataRetrieved(""); // Document does not exist
                        }
                    } else {
                        listener.onDataRetrieved(""); // Failed to fetch data
                    }
                }
            });
        } else {
            listener.onDataRetrieved(""); // User not logged in
        }
    }
    public void registerUser(String email, String password, String name, String surname, boolean isTeacher) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Create a user object with additional details
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("email", email);
                                userData.put("name", name);
                                userData.put("surname", surname);
                                userData.put("userType", isTeacher ? "teacher" : "student");

                                // Store the user in Firestore
                                db.collection("users").document(user.getUid())
                                        .set(userData, SetOptions.merge())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if (isTeacher) {
                                                        // Additional fields for teachers
                                                        Map<String, Object> teacherData = new HashMap<>();
                                                        teacherData.put("link", ""); // Initialize with an empty link
                                                        teacherData.put("subjects", new ArrayList<>());
                                                        teacherData.put("grades", new ArrayList<>());

                                                        // Update the teacher information in Firestore
                                                        db.collection("users").document(user.getUid())
                                                                .set(teacherData, SetOptions.merge())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(context, "Account created and user information added to Firestore.", Toast.LENGTH_SHORT).show();
                                                                            Intent intent = new Intent(context, Login.class);
                                                                            context.startActivity(intent);
                                                                            ((Register) context).finish();
                                                                        } else {
                                                                            Toast.makeText(context, "Account created, but failed to add teacher information to Firestore.", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        // Student registration
                                                        Toast.makeText(context, "Account created and user information added to Firestore.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(context, Login.class);
                                                        context.startActivity(intent);
                                                        ((Register) context).finish();
                                                    }
                                                } else {
                                                    Toast.makeText(context, "Account created, but failed to add user information to Firestore.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(context, "User is null.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getMeetingsForCurrentUser(OnSuccessListener<ArrayList<Meeting>> successListener, OnFailureListener failureListener) {
        ArrayList<Meeting> meetings_objects = new ArrayList<>();

        db.collection("users").document(user.getUid()).collection("meetings")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Date startDate = document.getDate("date");
                            Date endDate = document.getDate("endDate");
                            String link = document.getString("link");
                            String student = document.getString("student");
                            String teacher = document.getString("teacher");
                            Meeting spotkanie = new Meeting(startDate, endDate, link, student, teacher);
                            meetings_objects.add(spotkanie);
                        }
                        successListener.onSuccess(meetings_objects);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failureListener.onFailure(e);
                    }
                });
    }
    public void getTermsForTeacher(String userID, OnSuccessListener<ArrayList<Term>> successListener, OnFailureListener failureListener) {
        ArrayList<Term> terms_objects = new ArrayList<>();

        db.collection("users").document(userID).collection("terms")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            com.google.firebase.Timestamp timestamp = document.getTimestamp("date");
                            boolean isBooked = document.getBoolean("isBooked");
                            Term term = new Term(timestamp, isBooked);
                            terms_objects.add(term);
                        }
                        successListener.onSuccess(terms_objects);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Błąd: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addTermToFirebase(String userID, Timestamp timestamp, boolean isBooked) {
        Map<String, Object> termData = new HashMap<>();
        termData.put("date", timestamp);
        termData.put("isBooked", isBooked);

        db.collection("users").document(userID).collection("terms")
                .add(termData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "Dodano termin", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Błąd: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

