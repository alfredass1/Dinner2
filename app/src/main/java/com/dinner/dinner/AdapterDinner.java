package com.dinner.dinner;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class AdapterDinner extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String ENTRY = "com.dinner.dinner.ENTRY";

    private Context context;
    private LayoutInflater inflater;
    List<Dinner> data = Collections.emptyList();
    Dinner current;
    int currentPos = 0;

    // create constructor to initialize context and data sent from MainActivity
    public AdapterDinner(Context context, List<Dinner> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        for (Dinner dinner : data) {
            Log.e("dinner", dinner.getDinnerType() + " " + dinner.getPayment());
        }
    }

    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.container_dinner, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in RecyclerView to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        Dinner current = data.get(position);
        myHolder.textDinnerType.setText("Dinner type: " + current.getDinnerType());
        myHolder.textDelivery.setText("Delivery: " + current.getDelivery());
        myHolder.textPrice.setText("Price: " + current.getPrice());
        myHolder.textPayment.setText("Payment: " + current.getPayment());
        Log.e("dinner", current.getDinnerType() + " " + current.getPayment());

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textDinnerType;
        TextView textDelivery;
        TextView textPrice;
        TextView textPayment;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textDinnerType = (TextView) itemView.findViewById(R.id.textDinnerType);
            textDelivery = (TextView) itemView.findViewById(R.id.textDelivery);
            textPrice = (TextView) itemView.findViewById(R.id.textPrice);
            textPayment = (TextView) itemView.findViewById(R.id.textPayment);
            itemView.setOnClickListener(this);
        }

        // Click event for item
        @Override
        public void onClick(View v) {
            int itemPosition = getAdapterPosition();

            Dinner dinner = data.get(itemPosition);

            Toast.makeText(context, dinner.getDinnerType() + "" + dinner.getPrice(), Toast.LENGTH_SHORT).show();
            //intent intent = new Intent(context, NewEntryActivity.class);
            //intent.putExtra(ENTRY, dinner);
            //context.startActivity(intent);
        }

    }

}