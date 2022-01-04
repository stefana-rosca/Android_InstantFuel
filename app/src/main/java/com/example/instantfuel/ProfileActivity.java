package com.example.instantfuel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView loggedInUserMsg;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    static List<User> userList = new ArrayList<>();
    String loggedUserName = "";
    String loggedUserPhone = "";
    String loggedUserEmail = "";
    String loggedUserPassword = "";

    // creating a variable for
    // our Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    private TextView retrieveEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //retrieveEmail = findViewById(R.id.email);
        //getUsersInfoFromDb();

    }

 /*   public void getUsersInfoFromDb() {
        firebaseFirestore.collection("User").get()
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
                                if(user.getUserUID().equals(mAuth.getCurrentUser().getUid())) {
                                    loggedUserName = user.getName();
                                    loggedUserEmail = user.getEmail();
                                }
                            }
                            retrieveEmail.setText(loggedUserEmail);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });

    }*/
}