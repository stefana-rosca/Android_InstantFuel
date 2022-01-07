package com.example.instantfuel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText etName, etPhone, etEmail, etPassword;
    Button update;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    static List<User> userList = new ArrayList<>();
    String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        mAuth = FirebaseAuth.getInstance();

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        update = findViewById(R.id.btnUpdate);

        currentUserId = user.getUid();

        db.collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<QueryDocumentSnapshot> list = new ArrayList<>();
                    String s;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        s = String.valueOf(document.getData().values());
                        if (s.substring(s.lastIndexOf(" ")+1, s.length()-1).equals(currentUserId))
                            docId = document.getId();
                    }
                    Log.d("listtt", list.toString());
                } else {
                    Log.d("error", "Error getting documents: ", task.getException());
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        db.collection("User").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("userList", "onSuccess: LIST EMPTY");
                        } else {
                            List<User> users = documentSnapshots.toObjects(User.class);
                            userList.addAll(users);
                            for (User user : userList) {
                                if (user.getUserUID().equals(mAuth.getCurrentUser().getUid())){

                                    String emailRes = user.getEmail();
                                    String nameRes = user.getName();
                                    String pwRes = user.getPassword();
                                    String phoneRes = user.getPhone();

                                    etName.setText(nameRes);
                                    etPhone.setText(phoneRes);
                                    etEmail.setText(emailRes);
                                    etPassword.setText(pwRes);
                            }
                        }
                    }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void updateProfile() {
        String emailRes = etEmail.getText().toString();
        String nameRes = etName.getText().toString();
        String pwRes = etPassword.getText().toString();
        String phoneRes = etPhone.getText().toString();

        final DocumentReference sDoc = db.collection("User").document(docId);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {

                transaction.update(sDoc, "email", emailRes);
                transaction.update(sDoc, "name", nameRes);
                transaction.update(sDoc, "password", pwRes);
                transaction.update(sDoc, "phone", phoneRes);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateProfileActivity.this, "updated", Toast.LENGTH_SHORT).show();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfileActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}