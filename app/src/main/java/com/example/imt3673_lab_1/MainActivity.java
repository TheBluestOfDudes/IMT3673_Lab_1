package com.example.imt3673_lab_1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //Class variables
    private TextView lblBalance;
    private Button btnTransactions;
    private Button btnTransfer;
    private Integer balance;
    private Random r;
    private final int maxBalance = 110;
    private final int minBalance = 90;
    public static final int RETURN_CODE_Transfer = 0;
    public static final String PARAM_INT_NAME = "MainInt";
    public static final String PARAM_STRING_NAME = "MainString";
    public static final String PARAM_DATE_NAME = "MainDate";
    public static List<TransactionsData> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing variables
        this.lblBalance = findViewById(R.id.lbl_balance);
        this.btnTransactions = findViewById(R.id.btn_transactions);
        this.btnTransfer = findViewById(R.id.btn_transfer);
        this.dataList = new ArrayList<TransactionsData>();
        this.r = new Random();
        //Generating random balance
        this.balance = (r.nextInt(this.maxBalance - this.minBalance) + (this.minBalance + 1));
        this.lblBalance.setText(this.balance.toString() + '€');
        //Makes the first transaction
        firstTransaction();
        //Goes to transaction activity
        registerTransaction();
        //Goes to transfer activity
        registerTransfer();
    }

    /*
    *   The purpose of this function is to generate the initial
    *   transaction and then putting it into the global list.
    * */

    private void firstTransaction(){
        //Gets time of creation
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = dateFormat.format(date);
        //Generates initial transaction and adds to the list
        this.dataList.add(new TransactionsData("Owner", formattedDate, this.balance, this.balance));
    }

    /*
    *   The purpose of this function is to set up an onClickListener to the
    *   transaction button, and set up the button's behavior.
    * */

    private  void registerTransaction(){
        //Creates listener
        this.btnTransactions.setOnClickListener(v->{
            //Creates intent and starts activity
            final Intent toTransaction = new Intent(MainActivity.this, TransactionActivity.class);
            startActivity(toTransaction);
        });
    }

    /*
    *   The purpose of this function is to set up an onClickListener to the
    *   transfer button, and set up the button's behavior
    * */

    private void registerTransfer(){
        //Creates listener
        this.btnTransfer.setOnClickListener(v->{
            //Creates intent and passes account balance
            final Intent toTransfer = new Intent(MainActivity.this, TransferActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(MainActivity.PARAM_INT_NAME, this.balance);
            toTransfer.replaceExtras(bundle);
            //Starts activity
            startActivityForResult(toTransfer, RETURN_CODE_Transfer);
        });
    }

    /*
    *   The purpose of thus function is to use the data returned from TransferActivity
    *   to update the current balance, and register the new transaction.
    * */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != MainActivity.RETURN_CODE_Transfer) return;
        if (resultCode != Activity.RESULT_OK) return;
        if(requestCode == MainActivity.RETURN_CODE_Transfer){
            //Gets all the data sent from TransferActivity
            String recipient = data.getStringExtra(MainActivity.PARAM_STRING_NAME);
            String date = data.getStringExtra(MainActivity.PARAM_DATE_NAME);
            Integer newBalance = data.getIntExtra(MainActivity.PARAM_INT_NAME, 0);
            //Adds new transaction to the list
            this.dataList.add(new TransactionsData(recipient, date, this.balance, newBalance));
            //Updates balance
            this.lblBalance.setText(newBalance.toString() + '€');
            this.balance = newBalance;
        }
    }
}
