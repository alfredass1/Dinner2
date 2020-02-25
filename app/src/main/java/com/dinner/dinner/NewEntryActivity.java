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

    public static final String DB_URL = "http://forfuncom.epizy.com/mobile/db.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        Button newEntryBtn = findViewById(R.id.btn_create);
        final CheckBox soupCB = findViewById(R.id.checkSoup);
        final CheckBox saladCB = findViewById(R.id.checkSalad);
        final CheckBox mainCB = findViewById(R.id.checkMain);
        final RadioGroup deliveryRG = findViewById(R.id.new_entry_dinner_delivery_group);
        final EditText priceET = findViewById(R.id.new_entry_dinner_price);
        final Spinner paymentSpin = findViewById(R.id.payment);

        newEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                insertToDB(dinner);
                /*Toast.makeText(NewEntryActivity.this,
                        "Dinner type: " + dinner.getDinnerType() + "\n" +
                                "Delivery type: " + dinner.getDelivery() + "\n" +
                                "Price: " + dinner.getPrice() + "\n" +
                                "Payment: " + dinner.getPayment() + "\n",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

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

                String result = db.sendPostRequest(DB_URL, pietus);

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
