package SandZ.Tutors.database_handlers;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import SandZ.Tutors.activites.LoginView;
import SandZ.Tutors.activites.RegisterView;
import SandZ.Tutors.data.classes.Meeting;
import SandZ.Tutors.data.classes.TeacherClass;
import SandZ.Tutors.data.classes.Term;

public class FirebaseManager {

    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;
    private final Context context;

    private final FirebaseUser user;

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
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String data = document.getString(fieldName);
                            listener.onDataRetrieved(data != null ? data : "");
                        } else {
                            listener.onDataRetrieved("");
                        }
                    } else {
                        listener.onDataRetrieved("");
                    }
                }
            });
        } else {
            listener.onDataRetrieved("");
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
                                userData.put("picture", 0);

                                // Store the user in Firestore
                                db.collection("users").document(user.getUid())
                                        .set(userData, SetOptions.merge())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if (isTeacher) {
                                                        Map<String, Object> teacherData = new HashMap<>();
                                                        teacherData.put("subjects", new ArrayList<>());
                                                        teacherData.put("price", 0);
                                                        db.collection("users").document(user.getUid())
                                                                .set(teacherData, SetOptions.merge())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(context, "Account created and user information added to Firestore.", Toast.LENGTH_SHORT).show();
                                                                            Intent intent = new Intent(context, LoginView.class);
                                                                            context.startActivity(intent);
                                                                            ((RegisterView) context).finish();
                                                                        } else {
                                                                            Toast.makeText(context, "Account created, but failed to add teacher information to Firestore.", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        // Student registration
                                                        Toast.makeText(context, "Account created and user information added to Firestore.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(context, LoginView.class);
                                                        context.startActivity(intent);
                                                        ((RegisterView) context).finish();
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
                            String link = document.getString("link");
                            String student = document.getString("student");
                            String teacher = document.getString("teacher");
                            Meeting spotkanie = new Meeting(startDate, link, student, teacher);
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
                            String link = document.getString("link");
                            Term term = new Term(timestamp, isBooked,link);
                            terms_objects.add(term);
                        }
                        successListener.onSuccess(terms_objects);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addTermToFirebase(String userID, Timestamp timestamp, boolean isBooked, String link) {
        Map<String, Object> termData = new HashMap<>();
        termData.put("date", timestamp);
        termData.put("isBooked", isBooked);
        termData.put("link", link);

        db.collection("users").document(userID).collection("terms")
                .add(termData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "Term added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getSubjectList(final SubjectListCallback callback) {
        ArrayList<String> subjects = new ArrayList<>();
        db.collection("subjects")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots){
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                            String subject = document.getId();
                            subjects.add(subject);
                        }
                        callback.onSubjectListReceived(subjects);
                    }
                });
    }

    public void getTeacherList(final TeacherListCallback callback) {
        ArrayList<TeacherClass> teachers = new ArrayList<>();
        db.collection("users")
                .whereEqualTo("userType", "teacher")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String id = document.getId();
                        String email = document.getString("email");
                        String name = document.getString("name");
                        String surname = document.getString("surname");
                        List<String> subjects = (List<String>) document.get("subjects");

                        // Retrieve the "rates" field as a map of strings to numbers
                        Map<String, Object> ratesMap = (Map<String, Object>) document.get("ratings");
                        List<Integer> rates = new ArrayList<>();
                        if (ratesMap != null) {
                            for (Object rate : ratesMap.values()) {
                                // Assuming rates are stored as numbers
                                if (rate instanceof Number) {
                                    rates.add(((Number) rate).intValue());
                                }
                            }
                        }

                        int price = document.getLong("price").intValue();
                        int picture = document.getLong("picture").intValue();
                        TeacherClass teacher = new TeacherClass(id, email, name, surname, subjects, rates, price, picture);
                        teachers.add(teacher);
                    }
                    callback.onTeacherListReceived(teachers);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    // Handle failure
                });
    }


    public void updateSubjects(String userID, List<String> newSubjects, Context context) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("subjects", newSubjects);

        db.collection("users").document(userID).set(updateMap, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Przedmioty zaktualizowane pomyślnie", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(context, "Błąd podczas aktualizacji przedmiotów", Toast.LENGTH_SHORT).show();
                });
    }

    public void getTeacherSubjects(String teacherID, OnSuccessListener<ArrayList<String>> successListener, OnFailureListener failureListener) {
        db.collection("users").document(teacherID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> teacherSubjects = new ArrayList<>();
                        if (documentSnapshot.exists()) {
                            teacherSubjects = (ArrayList<String>) documentSnapshot.get("subjects");
                        }
                        successListener.onSuccess(teacherSubjects);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failureListener.onFailure(e);
                    }
                });
    }

    public void addMeetingForTeacherAndStudent(String teacherID, String studentID, Timestamp date, String link, String teacher, String student) {
        Map<String, Object> meetingData = new HashMap<>();
        meetingData.put("date", date);
        meetingData.put("link", link);
        meetingData.put("teacher", teacher);
        meetingData.put("student", student);

        addMeetingToCollection(teacherID, meetingData, "meetings");
        addMeetingToCollection(studentID, meetingData, "meetings");

        updateTermsStatus(teacherID, date);
    }

    private void addMeetingToCollection(String userID, Map<String, Object> meetingData, String collectionName) {
        db.collection("users").document(userID).collection(collectionName)
                .add(meetingData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(context, "Meeting added", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateTermsStatus(String userID, Timestamp date) {
        db.collection("users").document(userID).collection("terms")
                .whereEqualTo("date", date)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().update("isBooked", true);
                    }
                });
    }

    public void updatePrice(String teacherId, int price){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("price", price);

        db.collection("users").document(teacherId).set(updateMap, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Cena zaktualizowana pomyślnie", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(context, "Błąd podczas aktualizacji ceny", Toast.LENGTH_SHORT).show();
                });
    }
    public void addRate(String teacherId, String studentId, int rate) {
        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Construct a map to represent the new rating
        Map<String, Object> newRating = new HashMap<>();
        newRating.put("studentId", studentId);
        newRating.put("value", rate);

        // Update the document with the new rating
        db.collection("users").document(teacherId)
                .update("ratings." + studentId, rate)  // Use studentId as the key
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Ocena dodana pomyślnie", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(context, "Błąd podczas aktualizacji oceny", Toast.LENGTH_SHORT).show();
                });
    }



    public void addImage(String teacherId, int image){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("picture", image);

        db.collection("users").document(teacherId).set(updateMap, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Zdjęcie zaktualizowane pomyślnie", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(context, "Błąd podczas aktualizacji zdjęcia", Toast.LENGTH_SHORT).show();
                });
    }

}

