package com.taxiapp.thetaxicompany;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TaxiDriverActivity extends AppCompatActivity {

    private BroadcastReceiver onReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_driver);

        onReceive = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //TODO : check if this user is within the given range and can receive a request

                String title = intent.getStringExtra("title");
                String message = intent.getStringExtra("message");
                String from = intent.getStringExtra("from");
               // String destination = intent.getStringExtra("destination");



                AlertDialog.Builder diBuilder = new AlertDialog.Builder(TaxiDriverActivity.this);
                diBuilder.setMessage(message);
                diBuilder.setTitle(title);
                diBuilder.setPositiveButton("accept", new OnAcceptListener(from));
                diBuilder.setNegativeButton("decline", null);
                diBuilder.show();
            }

            /**
             * When accepting the request, the view is changed into a route activity
             */
             class OnAcceptListener implements DialogInterface.OnClickListener {

                private String from;

                public OnAcceptListener(String from){
                    this.from = from;
                }

                 @Override
                 public void onClick(DialogInterface dialog, int which) {

                     //TODO : first check all accepted requests and choose the nearest one to the user
                     //
                     //TODO : then calculate the time the driver needs to reach the customer and
                     // send a notification to the customer with the calculated time
                     //
                     //TODO : redirect the chosen driver to a route activitiy
                     startActivity(new Intent(TaxiDriverActivity.this, RouteActivity.class));
                 }
             }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(onReceive, new IntentFilter("Message"));





    }
}
