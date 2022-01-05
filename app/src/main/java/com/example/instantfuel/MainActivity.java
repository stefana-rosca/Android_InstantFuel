package com.example.instantfuel;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnNewOrder;
    FirebaseAuth mAuth;
    TextView loggedInUserMsg;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    static List<User> userList = new ArrayList<>();
    String loggedUserName = "";

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (SaveLoggedUser.getUserName(MainActivity.this).length() == 0)
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        else {

            drawerLayout = findViewById(R.id.my_drawer_layout);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

            NavigationView navigationView = findViewById(R.id.navigationView);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    if (id == R.id.nav_map) {
                        startActivity(new Intent(MainActivity.this, MapActivity.class));
                    }
                    if (id == R.id.nav_logout) {
                        mAuth.signOut();
                        clearUserName(getApplicationContext());
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                    if (id == R.id.nav_account) {
                        clearUserName(getApplicationContext());
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    }
                    return true;
                }
            });

            // pass the Open and Close toggle for the drawer layout listener
            // to toggle the button
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();

            // to make the Navigation drawer icon always appear on the action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            btnNewOrder = findViewById(R.id.btnNewOrder);
            loggedInUserMsg = findViewById(R.id.textView);
            mAuth = FirebaseAuth.getInstance();

//            if (LoginActivity.loggedIn)
            getUsersFromDbAndUpdateMainMsg();

            btnNewOrder.setOnClickListener(view -> {
                startActivity(new Intent(MainActivity.this, NewOrderActivity.class));
            });
        }
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
                            loggedInUserMsg.setText("Welcome, " + loggedUserName.toUpperCase() + "!");
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = SaveLoggedUser.getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}