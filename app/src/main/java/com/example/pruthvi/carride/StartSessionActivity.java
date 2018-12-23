package com.example.pruthvi.carride;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;


public class StartSessionActivity extends AppCompatActivity {


    private String TAG="StartSessionActivity";
    private static final int PERMISSIONS_REQUEST = 1;


    private Button scanQRButton;
    private Button startSessionButton;

    private IntentIntegrator qrScan;

    private String busNumber;

    boolean isPermissionGranted=false;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_session);

        scanQRButton=findViewById(R.id.scanQrButton);
        startSessionButton=findViewById(R.id.startSessionButton);

        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();



        scanQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan=new IntentIntegrator(StartSessionActivity.this);
                qrScan.initiateScan();
            }
        });

        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
        }


        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted=true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }

        startSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(busNumber == null){
                    Toast.makeText(StartSessionActivity.this,"Please scan the QR code.",Toast.LENGTH_SHORT).show();
                }else if(!isPermissionGranted){
                    Toast.makeText(StartSessionActivity.this,"Please grant location permission.",Toast.LENGTH_SHORT).show();
                }else {
                    startTrackerService();
                    startActivity(new Intent(StartSessionActivity.this,EndSessionActivity.class));
                }
            }
        });
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
     //Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: called");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                busNumber=result.getContents();
                Log.d(TAG, "onActivityResult: "+busNumber);
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");

        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            isPermissionGranted=true;
            //startTrackerService();
        } else {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
        }
    }

    //Start the TrackerService//

    /**
     *
     */
    private void startTrackerService() {


        Log.d(TAG, "startTrackerService: called");
        Intent sendBusTracking=new Intent(this, BusTrackService.class);
        sendBusTracking.putExtra("activityname","StartSessionActivity");
        sendBusTracking.putExtra("busNumber",busNumber);
        startService(sendBusTracking);

        //Notify the user that tracking has been enabled//

        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();




    }
}
