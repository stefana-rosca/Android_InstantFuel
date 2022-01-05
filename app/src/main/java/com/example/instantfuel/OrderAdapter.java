package com.example.instantfuel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView dateTextView;
        public TextView locationTextView;
        public TextView typeTextView;
        public TextView quantityTextView;
        public TextView priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = (TextView) itemView.findViewById(R.id.date);
            locationTextView = (TextView) itemView.findViewById(R.id.location);
            typeTextView = (TextView) itemView.findViewById(R.id.type);
            quantityTextView = (TextView) itemView.findViewById(R.id.quantity);
            priceTextView = (TextView) itemView.findViewById(R.id.price);
        }
    }

    // Store a member variable for the contacts
    static List<Order> orderList;

    // Pass in the contact array into the constructor
    public OrderAdapter(List<Order> orders) {
        orderList = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.activity_single_order, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        Order order = orderList.get(position);

        // Set item views based on your views and data model
        TextView dateTextView = holder.dateTextView;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
        String dateString = format.format( order.getDate());
        dateTextView.setText(dateString);
        TextView locatonTextView = holder.locationTextView;
        locatonTextView.setText(order.getAdress());
        TextView typeTextView = holder.typeTextView;
        typeTextView.setText(order.getFuelType());
        TextView quantityTextView = holder.quantityTextView;
        quantityTextView.setText(String.valueOf(order.getQuantity()) + " l");
        TextView priceTextView = holder.priceTextView;
        priceTextView.setText("LEI " + order.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


}
