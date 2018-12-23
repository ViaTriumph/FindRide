package com.example.pruthvi.carride;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ResponseListActivity extends AppCompatActivity {


    //private ArrayAdapter<String> responseAdapter;
    ArrayList<Ride> rideList;
    private static RideAdapter responseAdapter;
    private ListView mList;
    private DatabaseReference mReference;
    private DatabaseReference mRootRef;

    FirebaseAuth mAuth;
    ValueEventListener requestListener;

    private String mLocation;
    private String driverName;
    private String phoneNo;
    private String photoUrl;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_list);

        mList=findViewById(R.id.list);

        rideList=new ArrayList<Ride>();

        responseAdapter=new RideAdapter(rideList,getApplicationContext());
        mList.setAdapter(responseAdapter);

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();


        mRootRef=FirebaseDatabase.getInstance().getReference();





        Log.d("ResponseListActivity", "onCreate: hello");

        //query.addChildEventListener();


        mLocation=getIntent().getExtras().getString("location");


        Query query=mRootRef.child("Users").child(user.getDisplayName());


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference mReference=mRootRef.child("farequery").child(user.getDisplayName());

                mReference.addValueEventListener(requestListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





//        que.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()) {
//                    DriverDetails currUser = dataSnapshot.getValue(DriverDetails.class);
//                    if (currUser != null) {
//                        mLocation = currUser.getLocation();
//                        Log.d("ResponseListActivity", "(query) " + currUser.toString());
//                        Log.d("ResponseListActivity", "location(query) " + mLocation);
//                        DatabaseReference mReference = mRootRef.child("location").child(mLocation);
//                    }
//                    Log.d("ResponseListActivity", "location(query) " + mLocation);
//
//                }
//                Log.d("ResponseListActivity", "after " + mLocation);
//                //mReference.addValueEventListener(requestListener);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        requestListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rideList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    String name = data.getKey();
                    FareDetails fareDetails = data.getValue(FareDetails.class);
                    driverName = fareDetails.getDriverName();
                    Log.d("ResponseListActivity", "onDataChange: "+driverName);
                    phoneNo = fareDetails.getPhoneNo();
                    photoUrl=fareDetails.getPhotoUrl();
                    String listadd = name+ "-"+phoneNo;
                    Log.d("ResponseListActivity", "onDataChange : "+listadd);
                    String date=fareDetails.getDate();
                    String time=fareDetails.getTime();
                    Ride newRide=new Ride(photoUrl,driverName,date,time,fareDetails.getFare(),phoneNo);
                    rideList.add(newRide);
                    responseAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ResponseListActivity", "loadPost:onCancelled", databaseError.toException());
            }
        };

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ride currRide=(Ride)mList.getItemAtPosition(i);

                Intent sendIntent=new Intent(ResponseListActivity.this,RideConformationActivity.class);
                sendIntent.putExtra("driverName",currRide.getDriverName());
                sendIntent.putExtra("phoneNo",currRide.getPhoneNo() );
                sendIntent.putExtra("photo",currRide.getPhoto());
                sendIntent.putExtra("fare",currRide.getFare());
                startActivity(sendIntent);
            }
        });



    }
}
