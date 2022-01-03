package com.example.instantfuel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Intent;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button btnLogOut;
    Button btnMap;
    Button btnNewOrder;
    FirebaseAuth mAuth;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                if (id == R.id.nav_map) {
                    System.out.println(id + " ACOLO " + R.id.nav_map);
                    startActivity(new Intent(MainActivity.this, MapActivity.class));
//                    return false;
                }
                if (id == R.id.nav_logout) {
                    mAuth.signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                    return false;
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

        btnLogOut = findViewById(R.id.btnLogout);
        btnMap = findViewById(R.id.btnMap);
        btnNewOrder = findViewById(R.id.btnNewOrder);
        mAuth = FirebaseAuth.getInstance();

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

        System.out.println(mAuth.getUid());
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.navigation_menu, menu);
//        return true;
//    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

        /*boolean check = false;
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            check = true;

            int id = item.getItemId();
            if (id == R.id.nav_map) {
                System.out.println("AM INTRATTTTT" + id);
                Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(myIntent);
                return false;
            }
            return super.onOptionsItemSelected(item);
        }
        return check;*/
        /*if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.nav_logout:
                // User chose the "Settings" item, show the app settings UI...
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return false;

            case R.id.nav_settings:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }*/
    }
}