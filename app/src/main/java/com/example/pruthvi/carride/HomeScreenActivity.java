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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HomeScreenActivity extends AppCompatActivity {

    private String TAG =HomeScreenActivity.class.getSimpleName();
    private Button carRideButton;
    private Button busRideButton;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private static final int PERMISSIONS_REQUEST = 1;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        busRideButton=findViewById(R.id.busRide);

        carRideButton=findViewById(R.id.carRide);

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


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }

        carRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreenActivity.this,RideRequestDetailsActivity.class));
            }
        });

        busRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(HomeScreenActivity.this,BusMapActivity.class)));
            }
        });

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

            startTrackerService();
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
        sendBusTracking.putExtra("activityname","HomeScreenActivity");
        sendBusTracking.putExtra("user",user.getDisplayName());
        startService(sendBusTracking);

        //Notify the user that tracking has been enabled//

        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();



    }
}
