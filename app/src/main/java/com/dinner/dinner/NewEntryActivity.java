package com.dinner.dinner;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class NewEntryActivity extends AppCompatActivity {

    public static final String INSERT_URL = "http://furfuncom.epizy.com/mobile/db.php";

    Dinner dinner;

    CheckBox soupCB;
    CheckBox saladCB;
    CheckBox mainCB;

    RadioGroup deliveryRG;

    EditText priceET;

    Spinner paymentSpin;

    Button newEntryBtn;
    Button updateEntryBtn;
    Button deleteEntryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        // checking if it's new or existing entry
        Intent intent = getIntent();
        dinner = (Dinner) intent.getSerializableExtra(AdapterDinner.ENTRY);

        newEntryBtn = findViewById(R.id.btn_create);
        updateEntryBtn = findViewById(R.id.btn_update);
        deleteEntryBtn = findViewById(R.id.btn_delete);

        if (dinner == null) { // new entry- values by default
            setTitle(R.string.new_entry_meniu_title);

            dinner = new Dinner(
                    -1, // because it's not in db
                    getResources().getString(R.string.new_entry_dinner_type_main),
                    getResources().getString(R.string.new_entry_dinner_delivery_type_no),
                    0,
                    getResources().getStringArray(R.array.new_entry_dinner_payment_type).toString()
            );

            updateEntryBtn.setEnabled(false);
            deleteEntryBtn.setEnabled(false);
            newEntryBtn.setEnabled(true);
        } else { // existing entry- values by entry
            setTitle(R.string.existing_entry_meniu_title);

            //TODO: implement update, delete buttons
            updateEntryBtn.setEnabled(true);
            deleteEntryBtn.setEnabled(true);
            newEntryBtn.setEnabled(false);
        }

        soupCB = findViewById(R.id.checkSoup);
        saladCB = findViewById(R.id.checkSalad);
        mainCB = findViewById(R.id.checkMain);

        deliveryRG = findViewById(R.id.new_entry_dinner_delivery_group);

        priceET = findViewById(R.id.new_entry_dinner_price);

        paymentSpin = findViewById(R.id.payment);

        setDataFromEntry(dinner);

        newEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dinner dinnerFromForm = getDataFromForm();
                insertToDB(dinnerFromForm);
                /*Toast.makeText(NewEntryActivity.this,
                        "Dinner type: " + dinner.getDinnerType() + "\n" +
                                "Delivery type: " + dinner.getDelivery() + "\n" +
                                "Price: " + dinner.getPrice() + "\n" +
                                "Payment: " + dinner.getPayment() + "\n",
                        Toast.LENGTH_SHORT).show();*/
            }
        });


        updateEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewEntryActivity.this,
                        "Needs to be implemented",
                        Toast.LENGTH_SHORT).show();
            }
        });

        deleteEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewEntryActivity.this,
                        "Needs to be implemented",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setDataFromEntry(Dinner dinner) {
        boolean isChecked = false;
        if (dinner.getDinnerType().
                contains(getResources().getString(R.string.new_entry_dinner_type_soup))) {
            soupCB.setChecked(true);
            isChecked = true;
        }
        if (dinner.getDinnerType().
                contains(getResources().getString(R.string.new_entry_dinner_type_salad))) {
            saladCB.setChecked(true);
            isChecked = true;
        }
        if (dinner.getDinnerType().
                contains(getResources().getString(R.string.new_entry_dinner_type_main))) {
            mainCB.setChecked(true);
            isChecked = true;
        }
        if (!isChecked) { // was new entry - nothing to check
            mainCB.setChecked(true); //// lets say it will be default value for new entry
        }

        if(!dinner.getDelivery(). // delivery - no
                equalsIgnoreCase(getResources().getString(R.string.new_entry_dinner_delivery_type_no))){
            ((RadioButton)deliveryRG.getChildAt(0)).setChecked(true);
        } else { // delivery - yes
            ((RadioButton)deliveryRG.getChildAt(1)).setChecked(true);
        }

        priceET.setText(String.valueOf(dinner.getPrice()));

        //TODO: spinner

    }

    private Dinner getDataFromForm() {
        String dinnerTypes = "";
        if (soupCB.isChecked()) {
            dinnerTypes = dinnerTypes + soupCB.getText().toString() + " ";
        }
        if (saladCB.isChecked()) {
            dinnerTypes = dinnerTypes + saladCB.getText().toString() + " ";
        }
        if (mainCB.isChecked()) {
            dinnerTypes = dinnerTypes + mainCB.getText().toString() + " ";
        }

        int selectedDeliveryType = deliveryRG.getCheckedRadioButtonId();
        RadioButton deliveryType = findViewById(selectedDeliveryType);
        String selectedDeliveryTypeBtnName = deliveryType.getText().toString();

        double price = Double.parseDouble(priceET.getText().toString());

        String payment = String.valueOf(paymentSpin.getSelectedItem());

        Dinner dinner = new Dinner(dinnerTypes, selectedDeliveryTypeBtnName, price, payment);

        return dinner;
    }

    private void insertToDB (Dinner dinner){
        class NewEntry extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            DB db = new DB();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(NewEntryActivity.this,
                        getResources().getString(R.string.new_entry_database_info),
                        null, true, true);
            }

            @Override
            protected String doInBackground(String... strings) {
                // Pirmas string yra raktas, antras - reikšmė.
                HashMap<String, String> pietus = new HashMap<String, String>();
                pietus.put("dinner_type", strings[0]);
                pietus.put("delivery", strings[1]);
                pietus.put("price", strings[2]);
                pietus.put("payment", strings[3]);
                pietus.put("action", "insert");

                String result = db.sendPostRequest(INSERT_URL, pietus);

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(NewEntryActivity.this,
                        s,
                        Toast.LENGTH_SHORT).show();
                Intent eitiIPaieskosLanga = new Intent(NewEntryActivity.this,SearchActivity.class);
                startActivity(eitiIPaieskosLanga);
            }

        }
        NewEntry newEntry = new NewEntry();
        newEntry.execute(
               dinner.getDinnerType(),
                dinner.getDelivery(),
                Double.toString(dinner.getPrice()),
                dinner.getPayment()
        );

    }


}
