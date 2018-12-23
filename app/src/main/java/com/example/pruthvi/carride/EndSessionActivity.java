package com.example.pruthvi.carride;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.ui.IconGenerator;

import java.util.HashMap;

public class EndSessionActivity extends FragmentActivity implements OnMapReadyCallback {

    private String TAG=EndSessionActivity.class.getSimpleName();
    private HashMap<String, Marker> mMarkers = new HashMap<String, Marker>();
    private GoogleMap mMap;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private Button mEndSession;


    IconGenerator iconFactory;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_session);

        mEndSession=findViewById(R.id.endSessionButton);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mAuth=FirebaseAuth.getInstance();
        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        iconFactory = new IconGenerator(this);
        Log.d(TAG, "onCreate: done");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mEndSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                startActivity(new Intent(EndSessionActivity.this,LoginActivity.class));
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }

    /**
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setMaxZoomPreference(16);
        subscribeToUpdates();
    }

    /**
     *
     */
    private void subscribeToUpdates() {
        String path="BusTracking";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                String currBusNo=dataSnapshot.getKey();
//                Log.d(TAG, "onChildChanged: "+currBusNo);
//                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
//                double lat = Double.parseDouble(value.get("latitude").toString());
//                double lng = Double.parseDouble(value.get("longitude").toString());
//                LatLng location = new LatLng(lat, lng);
//                addIcon(iconFactory, currBusNo, location);
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                String currBusNo=dataSnapshot.getKey();
//                Log.d(TAG, "onChildChanged: "+currBusNo);
//                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
//                double lat = Double.parseDouble(value.get("latitude").toString());
//                double lng = Double.parseDouble(value.get("longitude").toString());
//                LatLng location = new LatLng(lat, lng);
//                addIcon(iconFactory, currBusNo, location);
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot data : dataSnapshot.getChildren()){
//                    String currBusNo=data.getKey();
//                    Log.d(TAG, "onDataChange: "+currBusNo);
////                    HashMap<String, Object> value = (HashMap<String, Object>) data.getValue();
////                    double lat = Double.parseDouble(value.get("latitude").toString());
////                    double lng = Double.parseDouble(value.get("longitude").toString());
////                    LatLng location = new LatLng(lat, lng);
////                    addIcon(iconFactory, currBusNo, location);
//                    setMarker(data);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }


    /**
     *
     * @param dataSnapshot
     */
    private void setMarker(DataSnapshot dataSnapshot) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
        String key = dataSnapshot.getKey();
        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
        double lat = Double.parseDouble(value.get("latitude").toString());
        double lng = Double.parseDouble(value.get("longitude").toString());
        LatLng location = new LatLng(lat, lng);
        if (!mMarkers.containsKey(key)) {
            Marker currMarket=mMap.addMarker(new MarkerOptions().title(key).position(location));
            currMarket.showInfoWindow();
            mMarkers.put(key,currMarket);
        } else {
            mMarkers.get(key).setPosition(location);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkers.values()) {
            builder.include(marker.getPosition());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));

    }

    /**
     *
     * @param iconFactory
     * @param text
     * @param position
     */
    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        Log.d(TAG, "addIcon: done");
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        mMap.addMarker(markerOptions);
    }

    /**
     *
     */
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EndSessionActivity.this, "Ended Session.", Toast.LENGTH_LONG).show();

                    }
                });
    }
}
