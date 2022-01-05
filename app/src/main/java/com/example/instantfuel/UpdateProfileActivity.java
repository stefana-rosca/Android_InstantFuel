package com.example.instantfuel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText etName, etPhone, etEmail, etPassword;
    Button update;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;// = db.document("User/"+currentUserId);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

    FirebaseAuth mAuth;
    static List<User> userList = new ArrayList<>();
    String idUser;


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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }

    private void writeNewPost(String userId, String name, String phone, String email, String password) {
        String key = reference.child("User").push().getKey();
        System.out.println(key+" key");
        User user = new User(userId, name, email , password, phone);
        Map<String, Object> postValues = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/User/" + userId + "/" + key, postValues);

        reference.updateChildren(childUpdates);
    }
    @Override
    protected void onStart(){
        super.onStart();

/*        db.collection("User").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("userList", "onSuccess: LIST EMPTY");
                        } else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<User> users = documentSnapshots.toObjects(User.class);
                            userList.addAll(users);
                            for (User user : userList)
                            {
                                if(user.getUserUID().equals(mAuth.getCurrentUser().getUid()))
                                {
                                    idUser = mAuth.getCurrentUser().getUid();
                                    String emailRes = user.getEmail();
                                    String nameRes = user.getName();
                                    String pwRes = user.getPassword();
                                    String phoneRes = user.getPhone();

                                    etName.setText(nameRes);
                                    etPhone.setText(phoneRes);
                                    etEmail.setText(emailRes);
                                    etPassword.setText(pwRes);
                                }
                                else{
                                    Toast.makeText(UpdateProfileActivity.this, "No profile", Toast.LENGTH_SHORT).show();
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
                });*/

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        documentReference.collection("User").document(currentUserId);

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.getResult().exists()) {
                            String emailRes = task.getResult().getString("email");
                            String nameRes = task.getResult().getString("name");
                            String pwRes = task.getResult().getString("password");
                            String phoneRes = task.getResult().getString("phone");

                            etName.setText(nameRes);
                            etPhone.setText(phoneRes);
                            etEmail.setText(emailRes);
                            etPassword.setText(pwRes);
                        }
                        else{
                            Toast.makeText(UpdateProfileActivity.this, "No profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateProfile() {
        String emailRes = etEmail.getText().toString();
        String nameRes = etName.getText().toString();
        String pwRes = etPassword.getText().toString();
        String phoneRes = etPhone.getText().toString();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String currentUserId = user.getUid();

        System.out.println(idUser+" aaaaaa");
        //writeNewPost(idUser, nameRes, phoneRes, emailRes, pwRes);
        final DocumentReference sDoc = db.collection("User").document(currentUserId);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {

                System.out.println(nameRes+phoneRes+emailRes+pwRes+" bbbb");
               // transaction.update(sDoc,"userUID", currentUserId);
                transaction.update(sDoc, "email", emailRes);
                transaction.update(sDoc, "name", nameRes);
                transaction.update(sDoc, "password", pwRes);
                transaction.update(sDoc, "phone", phoneRes);

                // Success
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