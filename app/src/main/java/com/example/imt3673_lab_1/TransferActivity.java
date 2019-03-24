package com.example.imt3673_lab_1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TransferActivity extends AppCompatActivity {
    //Views
    private Button btnPay;
    private EditText txtAmount;
    private TextView lblAmountCheck;
    //Names in list
    private String[] names;
    //The account balance
    private Integer balance;
    //The chosen item in the list
    private  String chosenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        //Initializing variables
        this.btnPay = this.findViewById(R.id.btn_pay);
        this.txtAmount = this.findViewById(R.id.txt_amount);
        this.lblAmountCheck = this.findViewById(R.id.lbl_amount_check);
        this.balance = getIntent().getIntExtra(MainActivity.PARAM_INT_NAME, 0);
        //Disabling button
        this.btnPay.setEnabled(false);
        //Filling ListView
        populateList();
        //Checks which list item was clicked
        registerListClicks();
        //Checks whether the EditText was edited.
        registerEditChange();
        //Checks if the pay button was pressed
        registerPayButton();
    }
    /*
    *   This function's purpose is to fill the list view with
    *   elements. I haven't been very creative with my names, but they
    *   suffice. I use an extra xml file so the adapter inserts a bunch of
    *   TextView elements into the ListView
    */
    private void populateList(){
        //Build name list
        this.names = new String[] {"Guy1","Guy2","Guy3","Guy4","Guy5","Guy6"};
        //Get adapter
        //ArrayAdapter<String>(Context, Layout to be used, Items to be placed)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_items, names);
        //Filling the list view
        ListView list = findViewById(R.id.transaction_list);
        list.setAdapter(adapter);
    }

    /*
    *   The purpose of this function is to detect which item
    *   in the list has been clicked by the user, and save
    *   the name of that item.
    *
    *   Every time a recipient is chosen, it will check if the button
    *   can be enabled.
    * */
    private void registerListClicks(){
        //Gets list element
        ListView list = findViewById(R.id.transaction_list);
        //Sets onclick listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Gets the relevant view
                TextView text = (TextView) view;
                //Saves the name of the recipient
                chosenName = (String) text.getText();
                //Checks if the button should be enabled
                checkIfEnable();
            }
        });
    }

    /*
    *   The purpose of this function is to check if the button
    *   should be enabled after the user edits the txt_amount field.
    *
    *   I made use of the text watcher class for this, however it auto generates
    *   a before and onchange function i do not use. I cannot remove them as far
    *   as I am aware, otherwise the text watcher stops working.
    * */
    private void registerEditChange(){
        this.txtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Checks if the button should be enabled
                checkIfEnable();
            }
        });
    }

    /*
    *   The purpose of this function is straight forward.
    *   It checks if the pay button has been pressed.
    *   If it has, then we call the pay function.
    * */
    private void registerPayButton(){
        this.btnPay.setOnClickListener(v -> {
            pay();
        });
    }

    /*
    *   The purpose of this function is to calculate the new balance after the
    *   transaction, and then passing the needed data back to MainActivity.
    *
    *   It calculates the new balance, gets the current time (ie, the time of the transaction)
    *   and it also gets the name of the recipient in question. Once it's gotten these values,
    *   it saves them into a bundle and sends them back to MainActivity upon finishing.
    * */
    private void pay(){
        //Calculating the new balance
        Integer value = Integer.parseInt(this.txtAmount.getText().toString());
        int result = this.balance - value;
        //The intent to be passed back.
        Intent data = new Intent(TransferActivity.this, MainActivity.class);
        //The bundle to be put into the intent.
        Bundle bundle = new Bundle();
        //Gets time of transaction
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = dateFormat.format(date);
        //Fills bundle the needed data
        bundle.putInt(MainActivity.PARAM_INT_NAME, result);
        bundle.putString(MainActivity.PARAM_STRING_NAME, this.chosenName);
        bundle.putString(MainActivity.PARAM_DATE_NAME, formattedDate);
        data.replaceExtras(bundle);
        //Sets result and ends activity
        setResult(Activity.RESULT_OK, data);
        finish();
    }
    /*
    *   The purpose of this function is to check whether or not the pay
    *   button should be enabled or not, and set it to enabled/disabled accordingly.
    *
    *   It first makes sure that one of the names in the list has been chosen.
    *   If we have a name selected it goes on to try to convert the value of
    *   txtAmount to an int. If we have a string, then it'll throw an exception and
    *   the pay button is disabled.
    *   If not we carry on.
    *
    *   If the value we inputted is greater than 0 and is not out of bounds,
    *   we enable the button and set lblAmountCheck to blank. If the former is not
    *   the case, we disable the button and have lblAmountCheck display a message.
    * */
    private void checkIfEnable(){
        //Control variable
        boolean ok = false;
        //Input from txtAmount
        Integer value = 0;
        String input = "";
        //Makes sure an item in the list has been clicked.
        for(int i = 0; i < this.names.length; i++){
            if(this.names[i] == this.chosenName){
                ok = true;
            }
        }
        if(ok){
            //Gets what is written in txtAmount
            input = this.txtAmount.getText().toString();
            try{
                value = Integer.parseInt(input);
                if((value > 0) && (value <= this.balance)){
                    this.btnPay.setEnabled(true);
                    this.lblAmountCheck.setText("");
                }
                else if ((value <= 0) || (value > this.balance)){
                    this.btnPay.setEnabled(false);
                    this.lblAmountCheck.setText("Value is invalid or out of bounds");
                }
            }
            catch(NumberFormatException e){
                this.btnPay.setEnabled(false);
                this.lblAmountCheck.setText("Value is invalid or out of bounds");
            }
        }
    }
}
