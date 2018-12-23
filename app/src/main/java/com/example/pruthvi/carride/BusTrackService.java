package com.example.pruthvi.carride;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BusTrackService extends Service {

    private static final String TAG = BusTrackService.class.getSimpleName();

    private static final int PERMISSIONS_REQUEST = 1;

    private String busNumber;
    private String callingActivity;
    private String user;

    /**
     *
     * @param intent
     * @param flags
     * @param startId
     * @return int
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callingActivity=intent.getStringExtra("activityname");
        if(callingActivity.equals("StartSessionActivity")) {
            busNumber = intent.getStringExtra("busNumber");
            Log.d(TAG, "onStartCommand: " + busNumber);
        }else{
            busNumber="MyLocation";
            user=intent.getStringExtra("user");
        }
        requestLocationUpdates();
        return super.onStartCommand(intent, flags, startId);
    }

    public BusTrackService() {
    }


    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();


        Log.d(TAG, "onCreate: done");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            buildNotification();
        else
            startForeground(1, new Notification());
        //loginToFirebase();

        Log.d(TAG, "onCreate: happening");

    }


    /**
     *
     * @param intent
     * @return IBinder
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     *
     */
    private void buildNotification(){
        String NOTIFICATION_CHANNEL_ID = "com.example.pruthvi.carride";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.tracking_enabled)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    //Initiate the request to track the device's location//

    /**
     *
     */
    private void requestLocationUpdates() {
        Toast.makeText(BusTrackService.this,"requestLocation",Toast.LENGTH_SHORT).show();
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        final String path;
        Log.d(TAG, "requestLocationUpdates: "+busNumber);
        if(busNumber.equals("MyLocation")){
            path="MyLocation/"+user;
        }else{
            path="BusTracking" + "/" + busNumber;
        }
        Toast.makeText(BusTrackService.this,"Tracking Started!",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "requestLocationUpdates: "+path);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Log.d(TAG, "location update " + location);
                        ref.setValue(location);
                    }
                    Log.d(TAG, "onLocationResult: "+location.toString());
                }
            }, null);
        }
    }
}
