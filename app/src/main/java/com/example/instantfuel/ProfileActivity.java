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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DatabaseReference db = FirebaseDatabase.getInstance().getReference("User");

    static List<User> userList = new ArrayList<>();
    String loggedUserName = "";
    String loggedUserPhone = "";
    String loggedUserEmail = "";
    String loggedUserPassword = "";

    public TextView emailTV;
    public TextView nameTV;
    public TextView phoneTV;
    public TextView passwordTV;
    public TextView adress;

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
        adress = findViewById(R.id.tv_address);

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
                            adress.setText(MapActivity.locationString);
                            Log.d("Tell", "Telefon profile: " + loggedUserPhone);
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