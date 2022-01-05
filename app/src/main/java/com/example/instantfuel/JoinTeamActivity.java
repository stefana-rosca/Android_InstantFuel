package com.example.instantfuel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class JoinTeamActivity extends AppCompatActivity {
    TextInputEditText email;
    TextInputEditText name;
    TextInputEditText phone;
    TextInputEditText city;
    MaterialButton btnApply;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);

        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone_nr);
        city = findViewById(R.id.city);
        btnApply = findViewById(R.id.btnApply);
        database = FirebaseFirestore.getInstance();

        btnApply.setOnClickListener(view -> {
            saveNewJoinApplication();
        });
    }

    private void saveNewJoinApplication() {
        CollectionReference dbCollection = database.collection("NewTeamMember");
        NewTeamMember newTeamMember = new NewTeamMember(email.getText().toString(), name.getText().toString(), phone.getText().toString(), city.getText().toString());

        dbCollection.add(newTeamMember).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(JoinTeamActivity.this, "Your application has been sent. Thank you for the interest!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(JoinTeamActivity.this, "Fail to send application \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}