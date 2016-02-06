package com.example.jlel.beacon;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Created by jlel on 24.01.16.
 */
public class MonitorActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    private ListView listView;
    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().
               setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
        listView = (ListView)findViewById(R.id.listView);
        onBeaconServiceConnect();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
//
//        beaconManager.setRangeNotifier(new RangeNotifier() {
//            @Override
//            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//                Log.i(TAG, "JOOOO");
//                if (beacons.size() > 0) {
//                    Log.i(TAG, "YAHOO!!!");
//                    Beacon firstBeacon = beacons.iterator().next();
//                    logToDisplay("The first beacon " + firstBeacon.toString() +
//                            " is about " + firstBeacon.getDistance() + " meters away.");
//                }
//            }
//
//        });
//
//        try {
//            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
//        } catch (RemoteException e) {   }
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

    private void logToDisplay(String s) {
        items.add(s);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }


    public void onClick(View view) {
        Throwable tr = new Throwable();
        String[] s = Log.getStackTraceString(tr).split("\n");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, s);
        listView.setAdapter(adapter);
    }

}

