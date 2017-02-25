package com.example.subhankar29.crimeawareness;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class LocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    static FirebaseDatabase database;
    static boolean isRunning=false;

    //Google API Client for getting last known location
    GoogleApiClient googleApiClient;

    private LocationManager locationManager;
    private final Criteria criteria = new Criteria();
    private final static int minUpdateTime = 30000;
    private final static int minUpdateDistance = 100;
    private static ULocation userLocation;
    private static Location oUserLocation;

    private static final String TAG = "LOCATION_SERVICE";


    private LocationListener bestProviderListener
            = new LocationListener() {

        public void onLocationChanged(Location location) {
            reactToLocationChange(location);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
            registerListener();
        }

        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {}
    };

    private LocationListener bestAvailableProviderListener =
            new LocationListener() {
                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                    registerListener();
                }

                public void onLocationChanged(Location location) {
                    reactToLocationChange(location);
                }

                public void onStatusChanged(String provider,
                                            int status, Bundle extras) {}
            };

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Get a reference to the Location Manager
        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(svcName);

        // Specify Location Provider criteria
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(true);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Set to running
        isRunning=true;

        //Get last known location of user
        googleApiClient.connect();

        //Start tracking user location
        try {
            registerListener();
        }catch (SecurityException e){
            Log.d("ERROR","/Not enough permission.");
        }

        return Service.START_REDELIVER_INTENT;
    }

    private void registerListener() throws SecurityException {
        unregisterAllListeners();
        String bestProvider =
                locationManager.getBestProvider(criteria, false);
        String bestAvailableProvider =
                locationManager.getBestProvider(criteria, true);

        Log.d(TAG, bestProvider + " / " + bestAvailableProvider);

        if (bestProvider == null)
            Log.d(TAG, "No Location Providers exist on device.");
        else if (bestProvider.equals(bestAvailableProvider))
            locationManager.requestLocationUpdates(bestAvailableProvider,
                    minUpdateTime, minUpdateDistance,
                    bestAvailableProviderListener);
        else {
            locationManager.requestLocationUpdates(bestProvider,
                    minUpdateTime, minUpdateDistance, bestProviderListener);

            if (bestAvailableProvider != null)
                locationManager.requestLocationUpdates(bestAvailableProvider,
                        minUpdateTime, minUpdateDistance,
                        bestAvailableProviderListener);
            else {
                List<String> allProviders = locationManager.getAllProviders();
                for (String provider : allProviders)
                    locationManager.requestLocationUpdates(provider, 0, 0,
                            bestProviderListener);
                Log.d(TAG, "No Location Providers currently available.");
            }
        }
    }

    private void unregisterAllListeners() throws SecurityException {
        locationManager.removeUpdates(bestProviderListener);
        locationManager.removeUpdates(bestAvailableProviderListener);
    }

    private void reactToLocationChange(Location location) {
        // TODO [ React to location change ]
        Log.d("LOCATIONSERVICE","My Location changed!");
        userLocation = new ULocation();
        userLocation.latitude=Double.toString(location.getLatitude());
        userLocation.longitude=Double.toString(location.getLongitude());
        userLocation.provider=location.getProvider();
        //database.getReference().getRoot().child("Users").child(auth.getCurrentUser().getUid()).child("location").setValue(userLocation);
        setUserLocation(location);

    }

    public static synchronized void setUserLocation(Location location){oUserLocation=location;}

    public static synchronized Location getUserLocation(){
        return oUserLocation;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)throws SecurityException{
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (mLastLocation != null) {
            setUserLocation(mLastLocation);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
