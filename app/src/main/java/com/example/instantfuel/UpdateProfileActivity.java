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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.sql.Timestamp;
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
    DocumentReference documentReference=db.collection("User").document();// = db.document("User/"+currentUserId);
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

    FirebaseAuth mAuth;
    static List<User> userList = new ArrayList<>();
    String idUser;
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
                        Log.d("pleaseee", s.toString());
                        Log.d("pleaseee", s.substring(s.lastIndexOf(" ")+1, s.length()-1));
                        Log.d("pleaseee", currentUserId);
                        if (s.substring(s.lastIndexOf(" ")+1, s.length()-1).equals(currentUserId))
                            docId = document.getId();
                    }
                    Log.d("listtt", list.toString());
                    String userUID;
                } else {
                    Log.d("error", "Error getting documents: ", task.getException());
                }
            }
        });



        Log.d("plss", documentReference.getId());

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
                            System.out.println(user.getUid()+" bye");
                            List<User> users = documentSnapshots.toObjects(User.class);
                            userList.addAll(users);
                            for (User user : userList) {
                                if (user.getUserUID().equals(mAuth.getCurrentUser().getUid())){
                                    System.out.println(user.getUserUID()+" hello "+mAuth.getCurrentUser().getUid());

                                    //idUser = mAuth.getCurrentUser().getUid();
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

        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();*/
       /* documentReference.collection("User").document(currentUserId);

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
                });*/
    }

    private void writeNewPost(String id, String name, String phone, String email, String password) {
        String key = reference.child("User").push().getKey();
        System.out.println(key+" key");
        User user = new User(id, name, email , password, phone);
        Map<String, Object> postValues = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/User/" + id + "/" + key, postValues);
        System.out.println(user.toString()+" user");
        System.out.println(id+"id din functie");
        reference.updateChildren(childUpdates);
    }

    FirebaseFirestore database2;
    private void updateProfile() {
        //String id = /*documentReference.getId();*/db.collection("User").document().getId();
        System.out.println(documentReference.getPath()+"maybe");
        //System.out.println("doc id "+id);
        String emailRes = etEmail.getText().toString();
        String nameRes = etName.getText().toString();
        String pwRes = etPassword.getText().toString();
        String phoneRes = etPhone.getText().toString();

        Log.d("docId", docId);

        writeNewPost(docId, nameRes, phoneRes,emailRes,pwRes);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String currentUserId = user.getUid();

        System.out.println(currentUserId+" aaaaaa");
        //writeNewPost(idUser, nameRes, phoneRes, emailRes, pwRes);
        String trypls="ZkWw51jwPvDSCZHUMRbJ";
        final DocumentReference sDoc = db.collection("User").document(trypls);

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

  /*      User updatedUser = new User(currentUserId,nameRes, emailRes, pwRes,phoneRes);
        CollectionReference dbOrder = database2.collection("User");

        dbOrder.add(updatedUser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(UpdateProfileActivity.this, "Your order has been sucessfully completed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(UpdateProfileActivity.this, "Fail to add order \n" + e, Toast.LENGTH_SHORT).show();
            }
        });*/


    }
}