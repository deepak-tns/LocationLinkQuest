package com.lhq.taxitesting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class CurrentLocationActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {


    TextView tv_first;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;

    LocationManager locationManager;
    Location location;
    Marker mCurrLocationMarker;
    private GoogleMap mMap;

    double latitude = 0;
    double longitude = 0;
    private int PROXIMITY_RADIUS = 500;
    Button btn_share;
    boolean zoomEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_maps);

        /*// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tv_first = (TextView) findViewById(R.id.tv_first);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_share = (Button) findViewById(R.id.btn_share);

        setSupportActionBar(toolbar);
        if (checkPermissions()) {
        }
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        fn_getlocation();

        //  Add..............
         *//*   ImageView iv_back =(ImageView)findViewById(R.id.iv_backss);
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });*//*

        final EditText editText = (EditText) findViewById(R.id.edt_search_loc);
        Button btn_find = (Button) findViewById(R.id.btn_find);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String line = editText.getText().toString().toLowerCase();
                String type = line.replaceAll("\\s+", "");

                StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                googlePlacesUrl.append("location=" + latitude + "," + longitude);
                googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlacesUrl.append("&types=" + type);
                googlePlacesUrl.append("&sensor=true");
                googlePlacesUrl.append("&key=" + getString(R.string.google_maps_key));

                GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
                Object[] toPass = new Object[2];
                toPass[0] = mMap;
                toPass[1] = googlePlacesUrl.toString();
                googlePlacesReadTask.execute(toPass);
                tv_first.setVisibility(View.GONE);
            }
        });
        tv_first.setVisibility(View.VISIBLE);

        */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag, new TabFragment()).commit();
        }

    }

 /*   @Override
    protected void onStart() {
        super.onStart();

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendMessae();
                } catch (FileNotFoundException e) {

                    Log.v("Current Location", e.getMessage().toString());
                    e.printStackTrace();
                }
            }
        });


    }*/

    public void sendMessae() throws FileNotFoundException {

/*
            String uriString = "geo:23.1097,-82.4094";
            Intent waIntent = new Intent();
            Uri uri = Uri.parse(uriString);
            waIntent.setData(uri);
            Toast.makeText(this, "D" + "Starting " + uriString, Toast.LENGTH_SHORT).show();
            try {
                this.startActivity(Intent.createChooser(waIntent,"Choose app to show location"));
            } catch (Exception e) {
                Toast.makeText(this, "D" + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

*/
        Uri uri = Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude);

        String uriST = uri.toString();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, uriST);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        // intent.setType("image/jpeg");
        intent.setPackage("com.whatsapp");
        startActivity(intent);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Enable / Disable my location button
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Enable / Disable Compass icon
        mMap.getUiSettings().setCompassEnabled(true);

        // Enable / Disable Rotate gesture
        mMap.getUiSettings().setRotateGesturesEnabled(true);

        // Enable / Disable zooming functionality
        mMap.getUiSettings().setZoomGesturesEnabled(true);


    }


        private void fn_getlocation(){
            locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnable && !isNetworkEnable){

            }else {

                if (isNetworkEnable){
                    location = null;
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
                    if (locationManager!=null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location!=null){

                            Log.e("latitude",location.getLatitude()+"");
                            Log.e("longitude",location.getLongitude()+"");

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        //    fn_update(location);
                        }
                    }

                }


                if (isGPSEnable){
                    location = null;
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
                    if (locationManager!=null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location!=null){
                            Log.e("latitude",location.getLatitude()+"");
                            Log.e("longitude",location.getLongitude()+"");
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                 //           fn_update(location);
                        }
                    }
                }


            }

        }







    @Override
    public void onLocationChanged(Location loc) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        location = loc;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.v("getdata", location.getLatitude() + "," + location.getLongitude() + "," + location.getAccuracy() + "," + location.distanceTo(location));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        if (!zoomEnabled) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        }

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder(" Your Address:");
                //  for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(returnedAddress.getAddressLine(0)).append("\n");
                //  }
                Log.v("getLocation", strReturnedAddress.toString());
                tv_first.setText(strReturnedAddress.toString());
                zoomEnabled = true;
            } else {
                tv_first.setText("No Address returned!");
                zoomEnabled = false;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // tv_first.setText("Canont get Address!");
        }
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




    class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
        String googlePlacesData = null;
        GoogleMap googleMap;
        @Override
        protected String doInBackground(Object... inputObj) {
            try {
                googleMap = (GoogleMap) inputObj[0];
                String googlePlacesUrl = (String) inputObj[1];
                Http http = new Http();
                googlePlacesData = http.read(googlePlacesUrl);
            } catch (Exception e) {
                Log.d("Google Place Read Task", e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
            PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
            Object[] toPass = new Object[2];
            toPass[0] = googleMap;
            toPass[1] = result;
            Log.v("GetAddress1", result);
            placesDisplayTask.execute(toPass);
        }
    }

    class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {
        JSONObject googlePlacesJson;
        GoogleMap googleMap;
        @Override
        protected List<HashMap<String, String>> doInBackground(Object... inputObj) {
            List<HashMap<String, String>> googlePlacesList = null;
            Places placeJsonParser = new Places();
            try {
                googleMap = (GoogleMap) inputObj[0];
                googlePlacesJson = new JSONObject((String) inputObj[1]);
                googlePlacesList = placeJsonParser.parse(googlePlacesJson);
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return googlePlacesList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            googleMap.clear();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    HashMap<String, String> googlePlace = list.get(i);
                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));
                    String placeName = googlePlace.get("place_name");
                    String vicinity = googlePlace.get("vicinity");
                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);
                    Log.v("GetAddress", placeName + " : " + vicinity);
                    googleMap.addMarker(markerOptions);
                }
            }
        }
        public class Places {
            public List<HashMap<String, String>> parse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return getPlaces(jsonArray);
            }
            private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
                int placesCount = jsonArray.length();
                List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> placeMap = null;
                for (int i = 0; i < placesCount; i++) {
                    try {
                        placeMap = getPlace((JSONObject) jsonArray.get(i));
                        placesList.add(placeMap);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return placesList;
            }
            private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
                HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
                String placeName = "-NA-";
                String vicinity = "-NA-";
                String latitude = "";
                String longitude = "";
                String reference = "";
                try {
                    if (!googlePlaceJson.isNull("name")) {
                        placeName = googlePlaceJson.getString("name");
                    }
                    if (!googlePlaceJson.isNull("vicinity")) {
                        vicinity = googlePlaceJson.getString("vicinity");
                    }
                    latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                    longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
                    reference = googlePlaceJson.getString("reference");
                    googlePlaceMap.put("place_name", placeName);
                    googlePlaceMap.put("vicinity", vicinity);
                    googlePlaceMap.put("lat", latitude);
                    googlePlaceMap.put("lng", longitude);
                    googlePlaceMap.put("reference", reference);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return googlePlaceMap;
            }
        }
    }

    class Http {
        public String read(String httpUrl) throws IOException {
            String httpData = "";
            InputStream inputStream = null;
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(httpUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                httpData = stringBuffer.toString();
                bufferedReader.close();
            } catch (Exception e) {
                Log.d("Exception-read Http url", e.toString());
            } finally {
                inputStream.close();
                httpURLConnection.disconnect();
            }
            return httpData;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}



    /*private static final String TAG = "LocationActivity";

    Location mCurrentLocation;
    String mLastUpdateTime;
    GoogleMap googleMap;
    double latitude = 0;
    double longitude = 0;
    private int PROXIMITY_RADIUS = 5000;

    private ArrayList<LatLng> arrayPoints = null;

    private TextView   tv_first;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_maps);

        arrayPoints = new ArrayList<LatLng>();
        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available


        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
           fm.getMapAsync(this);

  *//*      View plusMinusButton = fm.getView().findViewById(1);
        View locationButton = fm.getView().findViewById(2);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams)     locationButton.getLayoutParams();
        RelativeLayout.LayoutParams llp = (RelativeLayout.LayoutParams)     plusMinusButton.getLayoutParams();
// position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_LEFT,0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        llp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        llp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);


        ImageView iv_back =(ImageView)findViewById(R.id.iv_backss);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*//*

        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider,1000,0, (android.location.LocationListener) this);

        final EditText editText =(EditText)findViewById(R.id.edt_search_loc);
        Button btn_find =(Button)findViewById(R.id.btn_find);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = editText.getText().toString().toLowerCase();
                StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                googlePlacesUrl.append("location=" + latitude + "," + longitude);
                googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
                googlePlacesUrl.append("&types=" + type);
                googlePlacesUrl.append("&sensor=true");
                googlePlacesUrl.append("&key=" + getString(R.string.google_maps_key));

                GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
                Object[] toPass = new Object[2];
                toPass[0] = googleMap;
                toPass[1] = googlePlacesUrl.toString();
                googlePlacesReadTask.execute(toPass);
            }
        });

        tv_first =(TextView)findViewById(R.id.tv_first);


    }


    @Override
    public void onMapReady(GoogleMap googleMaps) {
        googleMap = googleMaps;
        // Add a marker in Sydney and move the camera
        //LatLng TutorialsPoint = new LatLng(21, 57);
       // googleMap.addMarker(new MarkerOptions().position(TutorialsPoint).title("Tutorialspoint.com"));
        // googleMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
    }


    @Override
    public void onLocationChanged(final Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        mCurrentLocation.getSpeed();
        mCurrentLocation.distanceTo(location);
        addMarker();
    }


    private void addMarker() {
        MarkerOptions options = new MarkerOptions();

      *//*  IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
        options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime)));
        options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());*//*


        options.draggable(true);
        options.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));


        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        options.position(currentLatLng);

         googleMap.addMarker(options);
      //  Marker mapMarker = googleMap.addMarker(options);

        long atTime = mCurrentLocation.getTime();
       // mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
     //   mapMarker.setTitle(mLastUpdateTime);
        Log.d(TAG, "Marker added.............................");

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(10);
        arrayPoints.add(currentLatLng);
        polylineOptions.addAll(arrayPoints);
        googleMap.addPolyline(polylineOptions);


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));

      //  googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        Log.d(TAG, "Zoom done.............................");


        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);

            if(addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                tv_first.setText(strReturnedAddress.toString());

            }
            else{
                tv_first.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // tv_first.setText("Canont get Address!");
        }
    }


    class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
        String googlePlacesData = null;
        GoogleMap googleMap;

        @Override
        protected String doInBackground(Object... inputObj) {
            try {
                googleMap = (GoogleMap) inputObj[0];
                String googlePlacesUrl = (String) inputObj[1];
                Http http = new Http();
                googlePlacesData = http.read(googlePlacesUrl);
            } catch (Exception e) {
                Log.d("Google Place Read Task", e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
            PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
            Object[] toPass = new Object[2];
            toPass[0] = googleMap;
            toPass[1] = result;
            placesDisplayTask.execute(toPass);
        }
    }

    class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

        JSONObject googlePlacesJson;
        GoogleMap googleMap;

        @Override
        protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

            List<HashMap<String, String>> googlePlacesList = null;
            Places placeJsonParser = new Places();

            try {
                googleMap = (GoogleMap) inputObj[0];
                googlePlacesJson = new JSONObject((String) inputObj[1]);
                googlePlacesList = placeJsonParser.parse(googlePlacesJson);
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return googlePlacesList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            googleMap.clear();
            if(list != null) {
                for (int i = 0; i < list.size(); i++) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    HashMap<String, String> googlePlace = list.get(i);
                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));
                    String placeName = googlePlace.get("place_name");
                    String vicinity = googlePlace.get("vicinity");
                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);
                    googleMap.addMarker(markerOptions);
                }
            }
        }
    }

    public class Places {

        public List<HashMap<String, String>> parse(JSONObject jsonObject) {
            JSONArray jsonArray = null;
            try {
                jsonArray = jsonObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return getPlaces(jsonArray);
        }

        private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
            int placesCount = jsonArray.length();
            List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> placeMap = null;

            for (int i = 0; i < placesCount; i++) {
                try {
                    placeMap = getPlace((JSONObject) jsonArray.get(i));
                    placesList.add(placeMap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return placesList;
        }

        private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
            HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
            String placeName = "-NA-";
            String vicinity = "-NA-";
            String latitude = "";
            String longitude = "";
            String reference = "";

            try {
                if (!googlePlaceJson.isNull("name")) {
                    placeName = googlePlaceJson.getString("name");
                }
                if (!googlePlaceJson.isNull("vicinity")) {
                    vicinity = googlePlaceJson.getString("vicinity");
                }
                latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = googlePlaceJson.getString("reference");
                googlePlaceMap.put("place_name", placeName);
                googlePlaceMap.put("vicinity", vicinity);
                googlePlaceMap.put("lat", latitude);
                googlePlaceMap.put("lng", longitude);
                googlePlaceMap.put("reference", reference);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return googlePlaceMap;
        }
    }

     class Http {

        public String read(String httpUrl) throws IOException {
            String httpData = "";
            InputStream inputStream = null;
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(httpUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                httpData = stringBuffer.toString();
                bufferedReader.close();
            } catch (Exception e) {
                Log.d("Exception - reading Http url", e.toString());
            } finally {
                inputStream.close();
                httpURLConnection.disconnect();
            }
            return httpData;
        }
    }

}



*/