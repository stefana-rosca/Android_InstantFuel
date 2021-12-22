package com.example.instantfuel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class NewOrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    FirebaseDatabase db;
    Spinner spinnerType;
    Spinner spinnerQuantity;
    TextView textViewPrice;
    TextView textViewTotalPrice;
    Double totalPrice = 1.0;
    Double selectedFuelPrice=1.0;
    public static Map<String,Double> fuelMap = new HashMap();
    public List<String> fuelList;
    public List<Integer> quantities = new ArrayList<>(Arrays.asList(0,1,2,3,4));
    MaterialButton btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        database = FirebaseFirestore.getInstance();

        fuelMap.put("Motorina", 5.99);
        fuelMap.put("Motorina+", 6.33);
        fuelMap.put("Benzina95", 5.96);
        fuelMap.put("Benzina+", 6.45);

        Set<String> fuelSet = fuelMap.keySet();
        fuelList = new ArrayList<>(fuelSet);

        spinnerType = findViewById(R.id.spinnerType);
        spinnerType.setOnItemSelectedListener(this);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);

        loadDataInSpinnerType();

        spinnerQuantity = findViewById(R.id.spinnerQuantity);
        spinnerQuantity.setOnItemSelectedListener(this);

        loadDataInSpinnerQuantity();

        btnOrder = findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(view -> {
            saveOrder();
        });
    }

    private void loadDataInSpinnerType(){
        ArrayAdapter fuelArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fuelList);
        fuelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(fuelArrayAdapter);
    }

    private void loadDataInSpinnerQuantity(){
        ArrayAdapter quantityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, quantities);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuantity.setAdapter(quantityAdapter);
    }

    public String selectedFuelType;
    public Integer selectedQuantity;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerType: {
                selectedFuelType = fuelList.get(position);
                selectedFuelPrice = fuelMap.get(fuelList.get(position));
                textViewPrice.setText("Price: " + selectedFuelPrice.toString());
                if (position==0)
                    textViewTotalPrice.setText("Total price: 0");
                spinnerQuantity.setSelection(0);
            }
            case R.id.spinnerQuantity: {
                totalPrice = selectedFuelPrice * quantities.get(position);
                selectedQuantity = quantities.get(position);
                if (position!=0)
                    textViewTotalPrice.setText("Total price: " + totalPrice);
                else
                    textViewTotalPrice.setText("Total price: 0");
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    FirebaseFirestore database;
    public void saveOrder() {
        Order order = new Order(selectedFuelType, selectedQuantity, totalPrice, new Timestamp(System.currentTimeMillis()));
        CollectionReference dbOrder = database.collection("Order");

        dbOrder.add(order).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(NewOrderActivity.this, "Your order has been sucessfully completed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(NewOrderActivity.this, "Fail to add order \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}