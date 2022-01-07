package com.example.instantfuel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity{
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    static List<Order> orderList;
    TextView noOrdersMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        orderList = new ArrayList<>();
        getOrderListFromDb();
        noOrdersMessage = (TextView) findViewById(R.id.noOrdersMessage);

    }

    public void getOrderListFromDb() {
        firebaseFirestore.collection("Order").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("orderList", "onSuccess: LIST EMPTY");
                        } else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<Order> orders = documentSnapshots.toObjects(Order.class);
                            for (Order order : orders)
                            {
                                if(order.getUserUID().equals(SaveSharedPreference.getUserUID(getApplicationContext()))) {
                                    orderList.add(order);
                                }
                            }
                        }
                        Log.d("Order", "size:  " +orderList.size());

                        if (orderList.size()!=0) {
                            // Lookup the recyclerview in activity layout
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvOrders);
                            // Initialize contacts
                            // Create adapter passing in the sample user data
                            OrderAdapter adapter = new OrderAdapter(orderList);
                            // Attach the adapter to the recyclerview to populate items
                            recyclerView.setAdapter(adapter);
                            // Set layout manager to position the items
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        }
                        else
                            noOrdersMessage.setText("No previous orders.");


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