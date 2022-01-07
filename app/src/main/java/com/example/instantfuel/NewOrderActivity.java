package com.example.instantfuel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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


public class NewOrderActivity extends AppCompatActivity {

    Spinner spinnerType;
    Spinner spinnerQuantity;
    TextView textViewPrice;
    TextView textViewTotalPrice;
    TextView textViewLocation;
    Double totalPrice = 1.0;
    Double selectedFuelPrice=1.0;
    public static Map<String,Double> fuelMap = new HashMap();
    public List<String> fuelList;
    public List<Integer> quantities = new ArrayList<>(Arrays.asList(0,1,2,3,4));
    Button btnMap;
    MaterialButton btnOrder;

    Boolean checkSelectedSpinner1 = false;
    Boolean checkSelectedSpinner2 = false;
    String locationString = "";

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
        fuelList.add(0, "None");

        btnMap = findViewById(R.id.btnMap);


        spinnerType = findViewById(R.id.spinnerType);
            spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        textViewTotalPrice.setText("Total price: 0");
                        checkSelectedSpinner1 = false;
                    } else {
                        checkSelectedSpinner1 = true;
                        selectedFuelType = fuelList.get(position);
                        selectedFuelPrice = fuelMap.get(fuelList.get(position));
                        textViewPrice.setText("Price: " + selectedFuelPrice.toString());
                        textViewPrice.setTextColor(Color.GREEN);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    checkSelectedSpinner1 = false;
                    Toast.makeText(NewOrderActivity.this, "Order has failed. You must choose from both fuel type and quantity! \n", Toast.LENGTH_SHORT).show();
                }
            });
            textViewPrice = findViewById(R.id.textViewPrice);
            textViewTotalPrice = findViewById(R.id.textViewTotalPrice);

            loadDataInSpinnerType();

            spinnerQuantity = findViewById(R.id.spinnerQuantity);
            spinnerQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    checkSelectedSpinner2 = true;
                    totalPrice = selectedFuelPrice * quantities.get(position);
                    selectedQuantity = quantities.get(position);
                    if (position != 0) {
                        String totalPriceRounded = String.format("%.2f", totalPrice);
                        textViewTotalPrice.setText("Total price: " + totalPriceRounded);
                    } else {
                        textViewTotalPrice.setText("Total price: 0");
                        checkSelectedSpinner2 = false;
                    }
                    textViewTotalPrice.setTextColor(Color.GREEN);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    checkSelectedSpinner2 = false;
                    Toast.makeText(NewOrderActivity.this, "Order has failed. You must choose from both fuel type and quantity! \n", Toast.LENGTH_SHORT).show();
                }
            });

            loadDataInSpinnerQuantity();

        textViewLocation = findViewById(R.id.textViewLocation);
        textViewLocation.setText("");
        btnMap.setOnClickListener(view -> {
            startActivityForResult(new Intent(NewOrderActivity.this, MapActivity.class),1);
        });

        btnOrder = findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(view -> {
            saveOrder();
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                locationString = data.getStringExtra("locationString");
                textViewLocation.setText(locationString);
            }
        }
    }

    private void loadDataInSpinnerType(){
        ArrayAdapter fuelArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fuelList);
        fuelArrayAdapter.setDropDownViewResource(android .R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(fuelArrayAdapter);
    }

    private void loadDataInSpinnerQuantity(){
        ArrayAdapter quantityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, quantities);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuantity.setAdapter(quantityAdapter);
    }

    public String selectedFuelType;
    public Integer selectedQuantity;

    FirebaseFirestore database;
    public void saveOrder() {
        if (checkSelectedSpinner1 && checkSelectedSpinner2 && !locationString.equals("")) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Order order = new Order(user.getUid(), selectedFuelType, selectedQuantity, totalPrice, new Timestamp(System.currentTimeMillis()), locationString);
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
        else if (!checkSelectedSpinner1)
                Toast.makeText(NewOrderActivity.this, "Please select the fuel type \n", Toast.LENGTH_SHORT).show();
        else if (!checkSelectedSpinner2)
            Toast.makeText(NewOrderActivity.this, "Please select the fuel quantity \n", Toast.LENGTH_SHORT).show();

        if (locationString.equals(""))
            Toast.makeText(NewOrderActivity.this, "Please check your location \n", Toast.LENGTH_SHORT).show();

    }
}