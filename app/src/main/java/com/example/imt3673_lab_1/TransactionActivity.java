package com.example.imt3673_lab_1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        //Fill list
        populateList();
        //Detects long clicks on list items
        registerLongClicks();
    }

    /*
    *   The purpose of this function is to fill the ListView with the
    *   transactions stored in the dataList.
    * */

    private void populateList(){
        //Gets the list
        TransactionsData[] listData = (TransactionsData[]) MainActivity.dataList.toArray(new TransactionsData[MainActivity.dataList.size()]);
        String[] items = new String[listData.length];
        //Fills the list items into an array
        for(int i = 0; i < listData.length; i++){
            items[i] = listData[i].getDataString();
        }
        //Sets up adapter and populates the list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_items, items);
        ListView list = findViewById(R.id.transaction_list);
        list.setAdapter(adapter);
    }

    /*
    *   The purpose of this function is to register a long click
    *   on the list items and then generate a toast that shows the
    *   recipient name and the amount spent on the tranaction.
    * */

    private void registerLongClicks(){
        //Gets the list view
        ListView list = findViewById(R.id.transaction_list);
        //Sets up longClickListener
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Gets the view we clicked
                TextView t = (TextView) view;
                //Gets the date of that transaction
                String date = t.getText().toString().substring(0, 8);
                //Setting up variables
                int transferAmount = 0;
                int duration = Toast.LENGTH_SHORT;
                String recipient = "";
                String text = "";
                Context context = getApplicationContext();
                //TransactionsData[] listData = (TransactionsData[]) MainActivity.dataList.toArray(new TransactionsData[MainActivity.dataList.size()]);
                //Find out the recipient name.
                for(int i = 0; i < MainActivity.dataList.size(); i++){
                    if(date.equals(MainActivity.dataList.get(i).getDate())){
                        transferAmount = (MainActivity.dataList.get(i).getBalanceBefore() - MainActivity.dataList.get(i).getBalanceAfter());
                        recipient = MainActivity.dataList.get(i).getRecipient();
                    }
                }
                //Makes toast
                text = recipient + ", " + transferAmount;
                Toast toast = Toast.makeText(context,text, duration);
                //Shows toast
                toast.show();
                return false;
            }
        });
    }
}
