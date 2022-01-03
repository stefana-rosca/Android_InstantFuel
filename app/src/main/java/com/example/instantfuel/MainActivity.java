package com.example.instantfuel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnLogOut;
    Button btnMap;
    Button btnNewOrder;
    FirebaseAuth mAuth;
    TextView loggedInUserMsg;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    static List<User> userList = new ArrayList<>();
    String loggedUserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogOut = findViewById(R.id.btnLogout);
        btnMap = findViewById(R.id.btnMap);
        btnNewOrder = findViewById(R.id.btnNewOrder);
        loggedInUserMsg = findViewById(R.id.textView);
        mAuth = FirebaseAuth.getInstance();

        if (LoginActivity.loggedIn)
            getUsersFromDbAndUpdateMainMsg();

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        btnMap.setOnClickListener(view ->{

            startActivity(new Intent(MainActivity.this, MapActivity.class));
        });

        btnNewOrder.setOnClickListener(view ->{

            startActivity(new Intent(MainActivity.this, NewOrderActivity.class));
        });

    }

    public void getUsersFromDbAndUpdateMainMsg() {
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
                                if(user.getUserUID().equals(mAuth.getCurrentUser().getUid()))
                                    loggedUserName = user.getName();
                            }
                            loggedInUserMsg.setText("You are logged in as " + loggedUserName.toUpperCase());
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}