package com.example.pruthvi.carride;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RideRequestDetailsActivity extends AppCompatActivity implements
        View.OnClickListener {

    private Spinner locationSpinner;
    private ArrayAdapter<String> locationAdapter;

    private Spinner destinationSpinner;
    private ArrayAdapter<String> destinationAdapter;

    private Spinner hoursSpinner;
    private ArrayAdapter<String> hoursAdapter;

    private Spinner minutesSpinner;
    private ArrayAdapter<String> minutesAdapter;

    private Button mSend;
    private String mLocationSelect;
    private String mDestinationSelect;
    private String mHoursSelect;
    private String mMinutesSelect;
    private EditText nameP;
    private String rideTime="";
    private String rideDate="";

    private RadioButton am;
    private RadioButton pm;

    private DatabaseReference mDatabase;
    private DatabaseReference mReference;

    FirebaseAuth mAuth;
    FirebaseUser user;
    private String photoUrl;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private TextView timeView;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button pickTimeButton;
    private Button pickDateButton;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_request_details);

        mSend=findViewById(R.id.sendButton);

        pickTimeButton=findViewById(R.id.pickTime);
        pickDateButton=findViewById(R.id.pickDate);
        dateView=findViewById(R.id.pickDateText);
        timeView=findViewById(R.id.pickUpTimeText);
        timeView.setText("Time :");
        pickTimeButton.setOnClickListener(this);
        pickDateButton.setOnClickListener(this);

        calendar = Calendar.getInstance();


        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        photoUrl=user.getPhotoUrl().toString();
        Log.d("RideRequestDetailsActivity", "onCreate: "+photoUrl);

        mReference=FirebaseDatabase.getInstance().getReference();

        Log.d("RideRequestDetails", "onClick: happening");
        List<String> locationList;

        locationSpinner=findViewById(R.id.locationSpinner);
        locationList=new ArrayList<>();
        locationList.add("Panjim");
        locationList.add("Vasco Da Gama");
        locationList.add("Margao");
        locationList.add("Verna");

        locationAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,locationList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        //TODO notfy changes in list for adapter.

        List<String> destinationList;

        destinationSpinner=findViewById(R.id.destinationSpinner);
        destinationList=new ArrayList<>();
        destinationList.add("Panjim");
        destinationList.add("Vasco Da Gama");
        destinationList.add("Margao");
        destinationList.add("Verna");

        destinationAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,destinationList);
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(destinationAdapter);

//        List<String> hoursList;
//
//        hoursSpinner=findViewById(R.id.hoursSpinner);
//        hoursList=new ArrayList<>();
//        hoursList.add("00");
//        hoursList.add("01");
//        hoursList.add("02");
//        hoursList.add("03");
//        hoursList.add("04");
//        hoursList.add("05");
//        hoursList.add("06");
//        hoursList.add("07");
//        hoursList.add("08");
//        hoursList.add("09");
//        hoursList.add("10");
//        hoursList.add("11");
//        hoursList.add("12");

//
//
//        hoursAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,hoursList);
//        hoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        hoursSpinner.setAdapter(hoursAdapter);

//        List<String> minutesList;
//
//        minutesSpinner=findViewById(R.id.minutesSpinner);
//        minutesList=new ArrayList<>();
//        minutesList.add("00");
//        minutesList.add("10");
//        minutesList.add("20");
//        minutesList.add("30");
//        minutesList.add("40");
//        minutesList.add("50");
//
//        minutesAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,minutesList);
//        minutesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        minutesSpinner.setAdapter(minutesAdapter);
//

        mDatabase = FirebaseDatabase.getInstance().getReference();


        Log.d("RideRequestDetails", "onClick: happening");


        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String ampm="";
//
//                if(am.isChecked()){
//                    ampm="AM";
//                }else if (pm.isChecked()){
//                    ampm="PM";
//                }

                mLocationSelect=locationSpinner.getSelectedItem().toString();
                mDestinationSelect=destinationSpinner.getSelectedItem().toString();


                if(TextUtils.isEmpty(rideDate) || TextUtils.isEmpty(rideTime)){
                    Toast.makeText(RideRequestDetailsActivity.this,"Fields empty!",Toast.LENGTH_SHORT).show();
                }else {
                    requestRide(mLocationSelect, mDestinationSelect, rideTime, rideDate);
                    Log.d("RideRequestDetails", "startactivity: happening");

                    Intent rideIntent = new Intent(RideRequestDetailsActivity.this, ResponseListActivity.class);
                    rideIntent.putExtra("location", mLocationSelect);
                    startActivity(rideIntent);
                }

            }
        });

    }

    /**
     *
     * @param location
     * @param destination
     * @param pickUpTime
     * @param pickDate
     */
    public void requestRide(String location,String destination,String pickUpTime,String pickDate) {
        Passenger passenger=new Passenger(destination,pickUpTime,user.getDisplayName(),pickDate);


        mDatabase.child("location").child(location).child(user.getDisplayName()).setValue(passenger);
    }

    /**
     *
     * @param view
     */
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Hello",
                Toast.LENGTH_SHORT)
                .show();
    }


    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        if (v == pickDateButton) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            rideDate=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            dateView.setText("Date :"+rideDate);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == pickTimeButton) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            String AM_PM ;
                            if(hourOfDay < 12) {
                                AM_PM = "AM";
                            } else {
                                AM_PM = "PM";
                            }
                            rideTime=hourOfDay + ":" + minute+":"+AM_PM;
                            timeView.setText("Time :"+rideTime);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }


}
