package com.example.brianwachira.hikerswatch.MainActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView lattextview,longtextview,acctextview,alttextview,addresstextview;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startlistening();
        }
    }

    public void startlistening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }}

    public void UpdateLocationInfo(Location location){

        lattextview.setText("Latitude: " + location.getLatitude());
        longtextview.setText("Longitude:" + location.getLongitude());
        alttextview.setText("Altitude: " + location.getAltitude());
        acctextview.setText("Accuracy:" + location.getAccuracy());
        Log.i("Location", location.toString());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
         String address = "could not find address";
        try {
            List<Address>listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if(listAddresses !=null && listAddresses.size() >0){

            address = "address\n";

            if(listAddresses.get(0).getSubThoroughfare()!=null){
                address+=listAddresses.get(0).getSubThoroughfare() + " ";
            }
            if(listAddresses.get(0).getThoroughfare()!=null){
                    address+=listAddresses.get(0).getThoroughfare() + "\n";
            }
            if(listAddresses.get(0).getLocality()!=null){
                address +=listAddresses.get(0).getLocality() + "\n";
                }
                if(listAddresses.get(0).getPostalCode()!=null){
                    address +=listAddresses.get(0).getPostalCode() + "\n";
                }
                if(listAddresses.get(0).getCountryName()!=null){
                address +=listAddresses.get(0).getCountryName() + "\n";
                }
            }
            addresstextview.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lattextview = findViewById(R.id.latttextView);
        longtextview = findViewById(R.id.longttextView);
        acctextview = findViewById(R.id.accttextView);
        alttextview = findViewById(R.id.alttextView);
        addresstextview = findViewById(R.id.addresstextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                UpdateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startlistening();
        }else{
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                //Ask for permission you dont have
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(location !=null){
                    UpdateLocationInfo(location);

                }
            }
        }
    }
}


