package com.atelierdusaintex.atelierdigital;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;


public class MyBeaconService extends Service {

    public static final long scanPe = 1000;
    public static final long waitPe = 1000;
    public static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    public static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);
    public BeaconManager beaconManager = new BeaconManager(this);


    // Empty constructor
    public MyBeaconService() {}


    @Override
    public IBinder onBind(Intent intent) { throw new UnsupportedOperationException("Not yet implemented"); }


    @Override
    public void onCreate() {

        // Set beaconManager
        beaconManager.setForegroundScanPeriod(scanPe, waitPe);
        //beaconManager.setBackgroundScanPeriod(scanPe, waitPe);

        // Monitoring beacons
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {

            @Override
            public void onEnteredRegion(Region arg0, List<Beacon> arg1) {

                // Send broadcast to main thread
                BeaconFoundBroadcast();
            }

            @Override
            public void onExitedRegion(Region region) {

                // Goodbye message ?
            }
        });
    }


    // Start monitoring beacons
    public int onStartCommand(Intent intent, int flags, int startId) {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {

            @Override
            public void onServiceReady() {
                try {

                    // Start monitoring beacons
                    beaconManager.startMonitoring(ALL_ESTIMOTE_BEACONS);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        return START_STICKY;
    }


    // Update rank broadcast
    public void BeaconFoundBroadcast() {
        Intent intent = new Intent("com.atelierdusaintex.atelierdigital.BEACON_FOUND");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    // Kill beacon service
    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            beaconManager.stopMonitoring(ALL_ESTIMOTE_BEACONS);
            beaconManager.disconnect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
