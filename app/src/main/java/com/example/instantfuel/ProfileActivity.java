package com.example.instantfuel;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
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
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    static List<User> userList = new ArrayList<>();
    String loggedUserName = "";
    String loggedUserPhone = "";
    String loggedUserEmail = "";
    String loggedUserPassword = "";

    public TextView emailTV;
    public TextView nameTV;
    public TextView phoneTV;
    public TextView passwordTV;

    ImageButton settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTV = findViewById(R.id.tv_name);
        phoneTV = findViewById(R.id.phone);
        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);
        settings = findViewById(R.id.tv_settings);

        getUsersInfoFromDb();
        mAuth = FirebaseAuth.getInstance();

        settings.setOnClickListener(view ->{
            startActivity(new Intent(ProfileActivity.this, UpdateProfileActivity.class));
        });
    }

    public void getUsersInfoFromDb() {
        firebaseFirestore.collection("User").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("userList", "onSuccess: LIST EMPTY");
                        } else {
                            List<User> users = documentSnapshots.toObjects(User.class);
                            userList.addAll(users);
                            for (User user : userList)
                            {
                                if(user.getUserUID().equals(mAuth.getCurrentUser().getUid())) {
                                    loggedUserName = user.getName();
                                    loggedUserEmail = user.getEmail();
                                    loggedUserPassword = user.getPassword();
                                    loggedUserPhone = user.getPhone();
                                }
                            }
                            nameTV.setText(loggedUserName);
                            phoneTV.setText(loggedUserPhone);
                            emailTV.setText(loggedUserEmail);
                            passwordTV.setText(loggedUserPassword);
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
}