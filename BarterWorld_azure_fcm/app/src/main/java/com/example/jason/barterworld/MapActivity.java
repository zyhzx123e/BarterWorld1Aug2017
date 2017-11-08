package com.example.jason.barterworld;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
//************

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.Manifest;

public class MapActivity extends FragmentActivity implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{




    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng latLng;
    private Marker marker;
    Geocoder geocoder;
    double lat=0;

    String attvalue="";

    double lng=0;

    View mapView = null;

    //************
    public GoogleApiClient googleApiClient;
    private static final LatLngBounds myBounds = new LatLngBounds(
            new LatLng(-0,0),new LatLng(0,0));


    private EditText myATView;
    private RecyclerView myRecyclerView;
    private LinearLayoutManager myLinearLayoutManager;
   // private AT_Adapter mATAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        setContentView(R.layout.activity_map);

        setUpMapIfNeeded();

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        //*******



        myATView = (EditText)findViewById(R.id.TFaddress);


       // myRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);

      //  myRecyclerView.setHasFixedSize(true);
        // myRecyclerView.setLayoutManager(new LinearLayoutManager(this));



/*
        myATView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(!s.toString().equals("") && googleApiClient.isConnected()){
                    mATAdapter.getFilter().filter(s.toString());
                }else if(!googleApiClient.isConnected()){
                    Toast.makeText(getApplicationContext(),"Google API Client did not connect!",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       myRecyclerView.addOnItemTouchListener(
                new Recycler_Listener(this, new Recycler_Listener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final AT_Adapter.PlaceAutocomplete item = mATAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        Log.i("TAG", "Autocomplete item selected: " + item.description);

                         //    Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.


                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(googleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if(places.getCount()==1){
                                    //Do the things here on Click.....
                                    Toast.makeText(getApplicationContext(),String.valueOf(places.get(0).getLatLng()),Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),Constants.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                    }
                })
        );

        */

        //*******

        mapView = (View)findViewById(R.id.map);


        if(mMap != null) {


            mMap.setOnMapClickListener(this);
            mMap.setOnMapLongClickListener(this);

       }
    }




    @Override
    public void onStart() {
        super.onStart();
     //   mATAdapter = new AT_Adapter(getApplicationContext(),R.layout.search_row, googleApiClient,myBounds,null);
     //   myRecyclerView.setAdapter(mATAdapter);

    }
    public synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("Google API Callback", "Connection Done");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("Google API Callback","Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(this,"API Not yet connected",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        setUpMap();

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapClick(LatLng point) {
        //save current location
        latLng = point;
        lat=point.latitude;
        lng=point.longitude;

        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        android.location.Address address = addresses.get(0);

        if (address != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i) + "\n");
            }
            attvalue = sb.toString();
            Toast.makeText(MapActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
        }

        //remove previously placed Marker
        if (marker != null) {
            marker.remove();
        }

        //place marker where user just clicked
        marker = mMap.addMarker(new MarkerOptions().position(point).title(address.getAddressLine(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

    }

    @Override
    public void onMapLongClick(LatLng latLng) {  Log.v("long clicked ","+1");
        Toast.makeText(MapActivity.this, "Yo", Toast.LENGTH_LONG).show();

            new EditMap().execute("", String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));


            double latitude=latLng.latitude;
            double longitude=latLng.longitude;
            Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
            List<Address> addresses=null;
            try {

                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5



            }catch(IOException e){e.printStackTrace();}




            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            new EditMap().execute(address, String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));



            Toast.makeText(MapActivity.this, "Your Barter Location ==> "+address+" "+city+" "+state+" "+country+" "+postalCode+" "+knownName, Toast.LENGTH_LONG).show();


    }

    class EditMap extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * getting Albums JSON
         * */
        public String doInBackground(String... args) {
            String address = args[0];
            double latitude = Double.parseDouble(args[1]);
            double longitude = Double.parseDouble(args[2]);

            return editMap(address, latitude, longitude);
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        public void onPostExecute(String result) {
            if(!result.equals(""))
                Toast.makeText(MapActivity.this,result,Toast.LENGTH_LONG).show();
            else {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(attvalue));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 17));


                double latitude=lat;
                double longitude=lng;
                Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                List<Address> addresses=null;
                try {

                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5



                }catch(IOException e){e.printStackTrace();}




                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                Toast.makeText(MapActivity.this, "Your Barter Location ==> "+address+" "+city+" "+state+" "+country+" "+postalCode+" "+knownName, Toast.LENGTH_LONG).show();



            }

        }
    }





    //****************
    private String editMap(String address, double latitude, double longitude ) {
        String keyword = null;

        try {
            Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
            if(!address.equals("")){
                keyword = address;
                java.util.List<android.location.Address> result = geocoder
                        .getFromLocationName(keyword, 1);
                if (result.size() > 0) {
                    lat = (double) result.get(0).getLatitude();
                    lng = (double) result.get(0).getLongitude();
                    attvalue = address;
                } else {
                    return "Record not found";
                }



            } else {

                String sUrl = "http://google.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&sensor=true";

                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(sUrl);
                HttpResponse r = client.execute(get);
                int status = r.getStatusLine().getStatusCode();
                if(status == 200){
                    HttpEntity e = r.getEntity();
                    String data = EntityUtils.toString(e);

                    try{
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray results = jsonObject.getJSONArray("results");
                        JSONObject addressObject = results.getJSONObject(0);
                        JSONArray addressComp = addressObject.getJSONArray("address_components");

                        String city = "", state = "";
                        for(int i=0; i < addressComp.length(); i++){
                            JSONArray types = addressComp.getJSONObject(i).getJSONArray("types");
                            if(city.equals("") && types.getString(0).equals("locality"))
                                city = addressComp.getJSONObject(i).getString("long_name");
                            if(state.equals("") && types.getString(0).equals("administrative_area_level_1"))
                                state = addressComp.getJSONObject(i).getString("long_name");

                            if(!city.equals("") && !state.equals(""))
                                break;
                        }
                        attvalue = city + ", " + state;
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    lat = latitude;
                    lng = longitude;
                }else{
                    return "Location Not Found";
                }
            }




        } catch (IOException io) {
            return "Connection Error";
        }
        return "";
    }
    //************************************8









    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        if(!googleApiClient.isConnected() && !googleApiClient.isConnecting()){
            Log.v("Google API","Connecting google API..");
            googleApiClient.connect();
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        if(googleApiClient.isConnected()){
            Log.v("Google API","Dis-Connecting");
            googleApiClient.disconnect();
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    public void onSearch(View view)
    {
        try {
            EditText location_tf = (EditText) findViewById(R.id.TFaddress);
            String location = location_tf.getText().toString();
            List<Address> addressList = null;
            if (location != null || !location.equals("")) {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location, 1);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(address.getLatitude(), address.getLongitude()), 20));
                String address_ = addressList.get(0).getAddressLine(0); //0 to obtain first possible address
                String city = addressList.get(0).getLocality();
                String state = addressList.get(0).getAdminArea();
                String country = addressList.get(0).getCountryName();
                String postalCode = addressList.get(0).getPostalCode();
                String full_address = address_ + " " + city + " " + state + " " + country + " " + postalCode;
            /*
            *  addresses = geocoder.getFromLocation(marker.getPosition().latitude(), marker.getPosition().longitude(), 1); //1 num of possible location returned
            String address = addresses.get(0).getAddressLine(0); //0 to obtain first possible address
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            //create your custom title
            String title = address +"-"+city+"-"+state;
            marker.setTitle(title);
            *
            *
            * */

                Toast.makeText(MapActivity.this, full_address, Toast.LENGTH_LONG).show();

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(address_));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                //  mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            } else {
                Toast.makeText(MapActivity.this, "Please enter an address before search!", Toast.LENGTH_LONG).show();


            }
        }catch (Exception e){

            Toast.makeText(MapActivity.this, "Not recognized..."+ e.getMessage()+" "+e.getLocalizedMessage() , Toast.LENGTH_LONG).show();

        }
    }

    public void onZoom(View view)
    {
        if(view.getId() == R.id.btn_register_barter)
        {
            Toast.makeText(MapActivity.this, "You can now pin your barter location..." , Toast.LENGTH_LONG).show();

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    new EditMap().execute("", String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));


                    double latitude=latLng.latitude;
                    double longitude=latLng.longitude;
                    Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                    List<Address> addresses=null;
                    try {

                        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5



                    }catch(IOException e){e.printStackTrace();}




                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    new EditMap().execute(address, String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));

                    mMap.addMarker(new MarkerOptions().position(latLng).title(address));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                    Toast.makeText(MapActivity.this, "Your Selected Barter Location ==> "+address+" "+city+" "+state+" "+country+" "+postalCode+" "+knownName, Toast.LENGTH_LONG).show();

                    latitude =Double.parseDouble(new DecimalFormat("##.######").format(latitude));
                    longitude =Double.parseDouble(new DecimalFormat("##.######").format(longitude));

                    Intent intent = new Intent(MapActivity.this, com.example.jason.barterworld.PostActivity.class);
                    intent.putExtra("barter_latitude", String.valueOf(latitude));
                    intent.putExtra("barter_longitude", String.valueOf(longitude));
                    startActivity(intent);


                }
            });
        }
        if(view.getId() == R.id.btn_back)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void changeType(View view)
    {
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

            /*MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);
            */
            SupportMapFragment mFragment = (SupportMapFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mFragment.getMapAsync(this);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng barterApp = new LatLng(3.056476, 101.700364);//apu campus
       // LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
         String barter_title="";
        String barter_latitude="";
        String barter_longitude="";

        //loop through each barter item and pass the latitude and longitude and display the title

        FirebaseDatabase.getInstance().getReference().child("Barter_Posts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);
                            String barter_title=post.getTitle();
                            String barter_latitude=post.getLatitude();
                            String barter_longitude=post.getLongitude();

                            double double_latitude = Double.parseDouble(barter_latitude);
                            double double_longitude = Double.parseDouble(barter_longitude);
                            LatLng barter_loc = new LatLng(double_latitude, double_longitude);//apu campus
                            mMap.addMarker(new MarkerOptions().position(barter_loc).title(barter_title));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });





       // mMap.addMarker(new MarkerOptions().position(barterApp).title("Barter World App Origin"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(barterApp));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(3.056476, 101.700364), 12));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            //Not in api-23, no need to prompt
            mMap.setMyLocationEnabled(true);
        }


         if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(MapActivity.this, "You have to accept to enjoy all app's services!", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }
}
