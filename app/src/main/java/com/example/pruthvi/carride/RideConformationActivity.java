package com.example.pruthvi.carride;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class RideConformationActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 1;

    private Button startRide;
    private TextView dName;
    private TextView dPhone;
    private ImageView photo;
    private TextView fareText;

    private String driverName;
    private String phoneNumber;
    private String photoUrl;
    private String fare;

    FirebaseAuth mAuth;

    ValueEventListener rideListner;
    ValueEventListener requestListner;

    private String location;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_conformation);

        startRide=findViewById(R.id.acceptButton);
        dName=findViewById(R.id.nameD);
        dPhone=findViewById(R.id.phoneD);
        photo=findViewById(R.id.photoD);
        fareText=findViewById(R.id.fareD);

        mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();


        boolean isPermissionGranted=false;

        driverName=getIntent().getExtras().getString("driverName");
        phoneNumber=getIntent().getExtras().getString("phoneNo");
        photoUrl=getIntent().getExtras().getString("photo");
        fare=getIntent().getExtras().getString("fare");

        dName.setText("Name : "+driverName);
        dPhone.setText("Phone No. : "+phoneNumber);
        Uri pic=Uri.parse(photoUrl);
        Picasso.with(RideConformationActivity.this).load(pic).transform(new CircleTransform()).into(photo);
        fareText.setText("Fare : "+fare);

        DatabaseReference mRoot=FirebaseDatabase.getInstance().getReference();
        Query que=mRoot.child("Users");
        final Query rideAccepted=mRoot.child("farequery");
        final Query removeRequest1=mRoot.child("location");

        que.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getKey().equals(user.getDisplayName())){
                        UserProfile currUser=data.getValue(UserProfile.class);
                        location=currUser.getLocation();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

            startRide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startTrackerService();
                    Log.d("RideConformationActivity", "onClick: happening");
                    rideAccepted.addListenerForSingleValueEvent(rideListner);
                    Query removeRequest=((DatabaseReference) removeRequest1).child(location);
                    removeRequest.addListenerForSingleValueEvent(requestListner);
                }
            });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }

        rideListner =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getKey().equals(user.getDisplayName())){
                        data.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        requestListner= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getKey().equals(user.getDisplayName())){
                        data.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

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

        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
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

//        Intent serviceIntent = new Intent(TrackingService.class.getName());
//        serviceIntent.putExtra("dName", driverName);
//        getApplicationContext().startService(serviceIntent);
        startService(new Intent(this, TrackingService.class));

        //Notify the user that tracking has been enabled//

        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();


    }
}
