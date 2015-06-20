package com.{package};


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;


public class MainActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register beacon broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(BeaconFoundBroadcast, new IntentFilter(".BEACON_FOUND"));

        // Start service
        startService(new Intent(this, MyBeaconService.class));
    }


    // Beacon found broadcast receiver
    private BroadcastReceiver BeaconFoundBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // ...
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister receiver if set
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(BeaconFoundBroadcast);
        } catch(IllegalArgumentException e) {
            System.out.println(e);
        }
    }
}
