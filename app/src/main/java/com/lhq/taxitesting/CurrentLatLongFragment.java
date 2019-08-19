package com.lhq.taxitesting;


import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentLatLongFragment extends Fragment implements View.OnClickListener, LocationListener {

    final String TAG = "GPS";
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    LocationManager locationManager;
    Location loc;
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;
    private String lat;
    private String log;
    TextView tvLatitude, tvLongitude, tvTime;
    EditText losdetail_edt_img1, losdetail_edt_img2, losdetail_edt_img3, losdetail_edt_img4, losdetail_edt_img5;
    ImageView losdetail_iv1, losdetail_iv2, losdetail_iv3, losdetail_iv4, losdetail_iv5;
    ImageButton losdetail_ib1, losdetail_ib2, losdetail_ib3, losdetail_ib4, losdetail_ib5;
    private String time = "NA";
    private Handler handler;

    private String img1 = "";
    private String img2 = "";
    private String img3 = "";
    private String img4 = "";
    private String img5 = "";

    ImageView img_share1;
    ImageView img_share2;
    ImageView img_share3;
    ImageView img_share4;
    ImageView img_share5;

    EditText edtRemark;
    Button send;


    public CurrentLatLongFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_current_lat_long, container, false);
        tvLatitude = (TextView) v.findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) v.findViewById(R.id.tvLongitude);
        tvTime = (TextView) v.findViewById(R.id.tvTime);
        edtRemark = (EditText) v.findViewById(R.id.tvRemark);


        losdetail_edt_img1 = (EditText) v.findViewById(R.id.losdetail_edt_img1);
        losdetail_edt_img2 = (EditText) v.findViewById(R.id.losdetail_edt_img2);
        losdetail_edt_img3 = (EditText) v.findViewById(R.id.losdetail_edt_img3);
        losdetail_edt_img4 = (EditText) v.findViewById(R.id.losdetail_edt_img4);
        losdetail_edt_img5 = (EditText) v.findViewById(R.id.losdetail_edt_img5);
        losdetail_iv1 = (ImageView) v.findViewById(R.id.losdetail_iv1);
        losdetail_iv2 = (ImageView) v.findViewById(R.id.losdetail_iv2);
        losdetail_iv3 = (ImageView) v.findViewById(R.id.losdetail_iv3);
        losdetail_iv4 = (ImageView) v.findViewById(R.id.losdetail_iv4);
        losdetail_iv5 = (ImageView) v.findViewById(R.id.losdetail_iv5);
        losdetail_ib1 = (ImageButton) v.findViewById(R.id.losdetail_ib1);
        losdetail_ib2 = (ImageButton) v.findViewById(R.id.losdetail_ib2);
        losdetail_ib3 = (ImageButton) v.findViewById(R.id.losdetail_ib3);
        losdetail_ib4 = (ImageButton) v.findViewById(R.id.losdetail_ib4);
        losdetail_ib5 = (ImageButton) v.findViewById(R.id.losdetail_ib5);

        img_share1 = (ImageView) v.findViewById(R.id.img_share1);
        img_share2 = (ImageView) v.findViewById(R.id.img_share2);
        img_share3 = (ImageView) v.findViewById(R.id.img_share3);
        img_share4 = (ImageView) v.findViewById(R.id.img_share4);
        img_share5 = (ImageView) v.findViewById(R.id.img_share5);


        img_share5.setOnClickListener(this);
        img_share1.setOnClickListener(this);
        img_share2.setOnClickListener(this);
        img_share3.setOnClickListener(this);
        img_share4.setOnClickListener(this);

        send = (Button) v.findViewById(R.id.send);
        send.setOnClickListener(this);


        if (!GoogleGPSService.isRunning) {
            getActivity().startService(new Intent(getActivity(), GoogleGPSService.class));
        }

        locationManager = (LocationManager) getActivity().getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        getLocation();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time = android.text.format.DateFormat.format("dd-MM-yyyy h:mm:ss:aa", System.currentTimeMillis()).toString();

                handler.postDelayed(this, 1000);
            }

        }, 1000);

        losdetail_ib1.setOnClickListener(this);
        losdetail_ib2.setOnClickListener(this);
        losdetail_ib3.setOnClickListener(this);
        losdetail_ib4.setOnClickListener(this);
        losdetail_ib5.setOnClickListener(this);


        return v;
    }

    private void shareImage(String path) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Uri uri1 = Uri.parse("file://" + path);
        Uri uri2 = Uri.parse("file://" + path);
        Uri uri3 = Uri.parse("file://" + path);

        ArrayList<Uri> imageUriArray = new ArrayList<Uri>();
        imageUriArray.add(uri1);
        imageUriArray.add(uri2);
        imageUriArray.add(uri3);

        ArrayList<String> imageUriArrayPath = new ArrayList<String>();
        imageUriArrayPath.add("img1");
        imageUriArrayPath.add("img2");
        imageUriArrayPath.add("img3");

        File file = new File(path);
        Toast.makeText(getActivity(), file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        Uri imgUri = Uri.parse("file://" + path);
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, file.getName());
        // whatsappIntent.putStringArrayListExtra(Intent.EXTRA_TEXT, imageUriArrayPath);
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        // whatsappIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
        whatsappIntent.setType("image/jpeg");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            getActivity().startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            // ToastHelper.MakeShortText("Whatsapp have not been installed.");
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

      /*  Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Text caption message!!");
        intent.setType("text/plain");
        intent.setType("image/jpeg");
        intent.setPackage("com.whatsapp");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
        startActivity(intent);*/

    }


    @Override
    public void onResume() {
        super.onResume();

        if (!losdetail_edt_img1.getText().toString().equalsIgnoreCase("")) {
            losdetail_iv1.setImageBitmap(BitmapFactory.decodeFile(losdetail_edt_img1.getText().toString()));
            img_share1.setVisibility(View.VISIBLE);
        }
        if (!losdetail_edt_img2.getText().toString().equalsIgnoreCase("")) {
            losdetail_iv2.setImageBitmap(BitmapFactory.decodeFile(losdetail_edt_img2.getText().toString()));
            img_share2.setVisibility(View.VISIBLE);
        }
        if (!losdetail_edt_img3.getText().toString().equalsIgnoreCase("")) {
            losdetail_iv3.setImageBitmap(BitmapFactory.decodeFile(losdetail_edt_img3.getText().toString()));
            img_share3.setVisibility(View.VISIBLE);
        }
        if (!losdetail_edt_img4.getText().toString().equalsIgnoreCase("")) {
            losdetail_iv4.setImageBitmap(BitmapFactory.decodeFile(losdetail_edt_img4.getText().toString()));
            img_share4.setVisibility(View.VISIBLE);
        }
        if (!losdetail_edt_img5.getText().toString().equalsIgnoreCase("")) {
            losdetail_iv5.setImageBitmap(BitmapFactory.decodeFile(losdetail_edt_img5.getText().toString()));
            img_share5.setVisibility(View.VISIBLE);
        }

        //  GPSTracker.BUS.register(this);
        //     getActivity().registerReceiver(broadcastReceiver, new IntentFilter(GoogleGPSService.BROADCAST_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        //      getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        updateUI(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String s) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    private void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS on");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else {
                    loc.setLatitude(0);
                    loc.setLongitude(0);
                    updateUI(loc);
                }
            } else {
                Log.d(TAG, "Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");
        lat = Double.toString(loc.getLatitude());
        log = Double.toString(loc.getLongitude());
        tvLatitude.setText(Double.toString(loc.getLatitude()));
        tvLongitude.setText(Double.toString(loc.getLongitude()));
        tvTime.setText(DateFormat.getTimeInstance().format(loc.getTime()));
    }

    private void getLastLocation() {
        try {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, provider);
            Log.d(TAG, location == null ? "NO LastLocation" : location.toString());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // mContact = (Contact)getIntent().getExtras().getSerializable(EXTRA_CONTACT);
            lat = intent.getStringExtra("LAT");
            log = intent.getStringExtra("LOG");
            String time = intent.getStringExtra("TIME");
            tvLatitude.setText(lat);
            tvLongitude.setText(log);
            tvTime.setText(time);

            //      lat_log.setText(lat + "," + log);
            //       Toast.makeText(getActivity(), "Lat : "+lat+","+ "Long : "+ log, Toast.LENGTH_LONG).show();

        }


    };

    @Override
    public void onClick(View v) {
        if (v == losdetail_ib1) {
            selectImage("1");
        }
        if (v == losdetail_ib2) {
            selectImage("2");
        }
        if (v == losdetail_ib3) {
            selectImage("3");
        }
        if (v == losdetail_ib4) {
            selectImage("4");
        }
        if (v == losdetail_ib5) {
            selectImage("5");
        }
        if (v == img_share1) {
            shareImage(losdetail_edt_img1.getText().toString());
        }
        if (v == img_share2) {
            shareImage(losdetail_edt_img2.getText().toString());
        }
        if (v == img_share3) {
            shareImage(losdetail_edt_img3.getText().toString());
        }
        if (v == img_share4) {
            shareImage(losdetail_edt_img4.getText().toString());
        }
        if (v == img_share5) {
            shareImage(losdetail_edt_img5.getText().toString());
        }
        if (v == send) {
            toSendDataRFSectorDetail1();
        }

    }

    private void selectImage(String Value) {

        if (Value.equals("1")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);
        }
        if (Value.equals("2")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 2);
        }
        if (Value.equals("3")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 3);
        }
        if (Value.equals("4")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 4);
        }
        if (Value.equals("5")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 5);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 1) {
                onCaptureImageResult(data, "1");
            }

            if (requestCode == 2) {
                onCaptureImageResult(data, "2");
            }
            if (requestCode == 3) {
                onCaptureImageResult(data, "3");
            }
            if (requestCode == 4) {
                onCaptureImageResult(data, "4");
            }
            if (requestCode == 5) {
                onCaptureImageResult(data, "5");
            }
        }

    }

    private void onCaptureImageResult(Intent data, String name) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        if (name.equals("1")) {
            String capturepath = "";
            String destinationpath = Environment.getExternalStorageDirectory().toString();
            String ctime = android.text.format.DateFormat.format("dd-MM-yyyy h:mm:ss:aa", System.currentTimeMillis()).toString();

            String totalString = ctime + "\nLat :" + lat + "\nLong :" + log + "\n" + name;
            // Bitmap setTextwithImage =    ProcessingBitmap(thumbnail,totalString);
            Bitmap setTextwithImage = DrawBitmapAll.drawTextToBitmap(getActivity(), thumbnail, totalString);
            //     ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //   setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            File destination = new File(destinationpath + "/LiveLoction/");
            if (!destination.exists()) {
                destination.mkdirs();
            }
            capturepath = destinationpath + "/LiveLoction/" + ctime + "1.jpg";
            try {
                FileOutputStream out = new FileOutputStream(capturepath);
                setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            String getPicturePath = capturepath;
            File file = new File(getPicturePath);
            losdetail_edt_img1.setText(getPicturePath);
            Bitmap bitmap = BitmapFactory.decodeFile(getPicturePath);
            losdetail_iv1.setImageBitmap(bitmap);
            //  img_SiteID = encodeToBase64(BitmapFactory.decodeFile(thumbnail), Bitmap.CompressFormat.JPEG, 100);
            img1 = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
            img_share1.setVisibility(View.VISIBLE);
        }
        if (name.equals("2")) {

            String capturepath = "";
            String destinationpath = Environment.getExternalStorageDirectory().toString();
            String ctime = android.text.format.DateFormat.format("dd-MM-yyyy h:mm:ss:aa", System.currentTimeMillis()).toString();

            String totalString = ctime + "\nLat :" + lat + "\nLong :" + log + "\n" + name;
            // Bitmap setTextwithImage =    ProcessingBitmap(thumbnail,totalString);
            Bitmap setTextwithImage = DrawBitmapAll.drawTextToBitmap(getActivity(), thumbnail, totalString);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //  setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            File destination = new File(destinationpath + "/LiveLoction/");
            if (!destination.exists()) {
                destination.mkdirs();
            }
            capturepath = destinationpath + "/LiveLoction/" + ctime + "2.jpg";
            try {
                FileOutputStream out = new FileOutputStream(capturepath);
                setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            String getPicturePath = capturepath;
            File file = new File(getPicturePath);
            losdetail_edt_img2.setText(getPicturePath);
            Bitmap bitmap = BitmapFactory.decodeFile(getPicturePath);
            losdetail_iv2.setImageBitmap(bitmap);
            //  img_SiteID = encodeToBase64(BitmapFactory.decodeFile(thumbnail), Bitmap.CompressFormat.JPEG, 100);
            img2 = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
            img_share2.setVisibility(View.VISIBLE);
        }
        if (name.equals("3")) {

            String capturepath = "";
            String destinationpath = Environment.getExternalStorageDirectory().toString();
            String ctime = android.text.format.DateFormat.format("dd-MM-yyyy h:mm:ss:aa", System.currentTimeMillis()).toString();

            String totalString = ctime + "\nLat :" + lat + "\nLong :" + log + "\n" + name;
            // Bitmap setTextwithImage =    ProcessingBitmap(thumbnail,totalString);
            Bitmap setTextwithImage = DrawBitmapAll.drawTextToBitmap(getActivity(), thumbnail, totalString);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //   setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            File destination = new File(destinationpath + "/LiveLoction/");
            if (!destination.exists()) {
                destination.mkdirs();
            }
            capturepath = destinationpath + "/LiveLoction/" + ctime + "3.jpg";
            try {
                FileOutputStream out = new FileOutputStream(capturepath);
                setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            String getPicturePath = capturepath;
            File file = new File(getPicturePath);
            losdetail_edt_img3.setText(getPicturePath);
            Bitmap bitmap = BitmapFactory.decodeFile(getPicturePath);
            losdetail_iv3.setImageBitmap(bitmap);
            //  img_SiteID = encodeToBase64(BitmapFactory.decodeFile(thumbnail), Bitmap.CompressFormat.JPEG, 100);
            img3 = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
            img_share3.setVisibility(View.VISIBLE);
        }
        if (name.equals("4")) {

            String capturepath = "";
            String destinationpath = Environment.getExternalStorageDirectory().toString();
            String ctime = android.text.format.DateFormat.format("dd-MM-yyyy h:mm:ss:aa", System.currentTimeMillis()).toString();

            String totalString = ctime + "\nLat :" + lat + "\nLong :" + log + "\n" + name;
            // Bitmap setTextwithImage =    ProcessingBitmap(thumbnail,totalString);
            Bitmap setTextwithImage = DrawBitmapAll.drawTextToBitmap(getActivity(), thumbnail, totalString);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //    setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            File destination = new File(destinationpath + "/LiveLoction/");
            if (!destination.exists()) {
                destination.mkdirs();
            }
            capturepath = destinationpath + "/LiveLoction/" + ctime + "4.jpg";
            try {
                FileOutputStream out = new FileOutputStream(capturepath);
                setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            String getPicturePath = capturepath;
            File file = new File(getPicturePath);
            losdetail_edt_img4.setText(file.getName());
            Bitmap bitmap = BitmapFactory.decodeFile(getPicturePath);
            losdetail_iv4.setImageBitmap(bitmap);
            //  img_SiteID = encodeToBase64(BitmapFactory.decodeFile(thumbnail), Bitmap.CompressFormat.JPEG, 100);
            img4 = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
            img_share4.setVisibility(View.VISIBLE);
        }
        if (name.equals("5")) {
            String capturepath = "";
            String destinationpath = Environment.getExternalStorageDirectory().toString();
            String ctime = android.text.format.DateFormat.format("dd-MM-yyyy h:mm:ss:aa", System.currentTimeMillis()).toString();

            String totalString = ctime + "\nLat :" + lat + "\nLong :" + log + "\n" + name;
            // Bitmap setTextwithImage =    ProcessingBitmap(thumbnail,totalString);
            Bitmap setTextwithImage = DrawBitmapAll.drawTextToBitmap(getActivity(), thumbnail, totalString);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //      setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            File destination = new File(destinationpath + "/LiveLoction/");
            if (!destination.exists()) {
                destination.mkdirs();
            }
            capturepath = destinationpath + "/LiveLoction/" + ctime + "5.jpg";
            try {
                FileOutputStream out = new FileOutputStream(capturepath);
                setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            String getPicturePath = capturepath;
            File file = new File(getPicturePath);
            losdetail_edt_img5.setText(getPicturePath);
            Bitmap bitmap = BitmapFactory.decodeFile(getPicturePath);
            losdetail_iv5.setImageBitmap(bitmap);
            //  img_SiteID = encodeToBase64(BitmapFactory.decodeFile(thumbnail), Bitmap.CompressFormat.JPEG, 100);
            img5 = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
            img_share5.setVisibility(View.VISIBLE);
        }

    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void toSendDataRFSectorDetail1() {
        //  +"?Loginid="+empId+"&password="+empPassword+"&imeno="+"1234567890"
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                "http://13.126.69.214/lq/api/Login/Live_Location", jsonData(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        parseSettingResponse(response.toString());
                        Log.v("response", response.toString());
                        pDialog.hide();

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("error ", error.toString());
                pDialog.hide();
            }

        });
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq, null);
    }

    private JSONObject jsonData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceId","" );
            jsonObject.put("name","" );
            jsonObject.put("lat",lat );
            jsonObject.put("log",log );
            jsonObject.put("time", time);
            jsonObject.put("remark",edtRemark.getText().toString() );
            jsonObject.put("image1",img1 );
            jsonObject.put("image2",img2 );
            jsonObject.put("image3",img3 );
            jsonObject.put("image4",img4 );
            jsonObject.put("image5",img5 );
            jsonObject.put("status","" );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("json",jsonObject.toString());
        return jsonObject;
    }


  private void   parseData(){

          String sss="{\"grno\":\"GRN1819000002\",\"empid\":\"571\",\"whcode\":\"WHSHA01\",\"projectcode\":\"DICWHR1800000\",\"putawaydata\":\"[{\\\"status\\\":[{\\\"itemcode\\\":\\\"FDSFSDFSDFDS\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"FDSFSDFSDFDS\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"FDSFSDFSDFDS\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"FDSFSDFSDFDS\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"POIUYRTYY\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"POIUYRTYY\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"POIUYRTYY\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"POIUYRTYY\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"}],\\\"Productlocation\\\":\\\"HV0014\\\"}]\",\"empcode\":\"OPS1103\",\"\":\"\"}";

          try {

              JSONObject jsonObject1 = new JSONObject(sss);
              String status = jsonObject1.getString("grno");
              Toast.makeText(getActivity(), status + " ", Toast.LENGTH_LONG).show();
              JSONArray jsonArray1 = jsonObject1.getJSONArray("putawaydata");
              JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
              JSONArray jsonArray2 = jsonObject2.getJSONArray("status");
              JSONObject jsonObject3 =jsonArray2.getJSONObject(0);
              String s1 = jsonObject3.getString("itemcode");
              String s2 = jsonObject3.getString("barcode");
              Log.v("json",s1+","+ s2);
              //
          } catch (Exception e) {
              Toast.makeText(getActivity(), e.getMessage() + " ", Toast.LENGTH_LONG).show();
              Log.v("json",e.getMessage());
              e.printStackTrace();
          }

  }

    private void parseSettingResponse(String s) {

        String sss="{\"grno\":\"GRN1819000002\",\"empid\":\"571\",\"whcode\":\"WHSHA01\",\"projectcode\":\"DICWHR1800000\",\"putawaydata\":\"[{\\\"status\\\":[{\\\"itemcode\\\":\\\"FDSFSDFSDFDS\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"FDSFSDFSDFDS\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"FDSFSDFSDFDS\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"FDSFSDFSDFDS\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"POIUYRTYY\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"POIUYRTYY\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"POIUYRTYY\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"},{\\\"itemcode\\\":\\\"POIUYRTYY\\\",\\\"barcode\\\":\\\"GRN120000004462POIUYRTYY20190304\\\"}],\\\"Productlocation\\\":\\\"HV0014\\\"}]\",\"empcode\":\"OPS1103\",\"\":\"\"}";

        try {

            JSONObject jsonObject1 = new JSONObject(sss);
            JSONArray jsonArray = new JSONArray(s);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String status = jsonObject.getString("Status");
            Toast.makeText(getActivity(), status + " ", Toast.LENGTH_LONG).show();

            // String password = jsonObject.getString("password");
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage() + " ", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}
