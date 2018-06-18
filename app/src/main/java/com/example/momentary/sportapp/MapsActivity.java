package com.example.momentary.sportapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class MapsActivity extends android.support.v4.app.Fragment implements OnMapReadyCallback {
    SupportMapFragment mMapView;
    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    private MapView mapView;
    private LocationManager locMgr;
    private final static String createGPSTable = "CREATE TABLE tableGPS(_id integer not null,loc_x real,loc_y real,distance real)";
    private SQLiteDatabase db = null;
    private SQLiteDatabase dbStore = null;
    double distance = 0;
    private int wallet=0;
    int id = 0;
    String bestProv;
    Location location;
    View v;
    private Handler handler = new Handler();

    @SuppressLint("MissingPermission")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_maps, container, false);
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();// needed to get the map to display immediately
        mapView.getMapAsync(this);
        db = getActivity().openOrCreateDatabase("SportApp.db", MODE_PRIVATE, null);
        locMgr = (LocationManager) v.getContext().getSystemService(LOCATION_SERVICE);
       // location = locMgr.getLastKnownLocation(bestProv);
        handler.removeCallbacks(updateTimer);
        //設定Delay的時間
        handler.postDelayed(updateTimer, 5000);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getActivity().setTitle("地圖");
        if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getActivity().setTitle("No location");
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);                                         //地圖的初始型態
        mMap.getUiSettings().setCompassEnabled(true);                                              //指南針顯示設定
        mMap.getUiSettings().setAllGesturesEnabled(true);//設定所有手勢控制
        //db.execSQL("drop table tableGPS");
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        bestProv = locMgr.getBestProvider(criteria, false);
        location = locMgr.getLastKnownLocation(bestProv);

        if(location==null){
            Toast.makeText(v.getContext(), "USE NETWORK LOCATION ", Toast.LENGTH_SHORT).show();
            location = locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        try{
            LatLng currentLoc = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc,17));
        }catch (Exception e){
        }
        try{
            db.execSQL(createGPSTable);
            db.execSQL("INSERT INTO tableGPS(_id,loc_x,loc_y,distance) values ("+id+","+location.getLatitude()+","+location.getLongitude()+","+0+")");
            Cursor cursor=getAll("tableGPS");
            if(cursor==null || cursor.getCount()==1){
                distance=0;
                id=1;
            }else{
                cursor.moveToLast();
                distance=cursor.getDouble(3);
                id=cursor.getInt(0);
            }
        }catch (Exception e){
        }
        //db.execSQL("drop table tableGPS");
        paintLine();

    }
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        if (locMgr!= null) {
            locMgr.removeUpdates(gLListenr);
        }

        distance=0;
        id=0;
        if(db!=null) db.close();
        if(dbStore!=null) dbStore.close();

    }

    private Runnable updateTimer = new Runnable() {     //Timer
        @SuppressLint("MissingPermission")
        public void run() {
            if(locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,gLListenr);
            }else if(locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,gLListenr);
            }
            handler.postDelayed(this, 3000);
        }
    };

    public LocationListener gLListenr = new LocationListener(){
        @Override
        public void onLocationChanged(Location location) {
            try{
                double Latitude=location.getLatitude();
                double Longitude=location.getLongitude();
                LatLng currentLoc = new LatLng(Latitude,Longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc,17));
                Cursor cursor=getAll("tableGPS");
                cursor.moveToLast();
                double passLatitude=cursor.getDouble(1);
                double passLongtitude=cursor.getDouble(2);
                if(abs(Latitude-passLatitude)>=0.00005 || abs(Longitude-passLongtitude)>=0.00005) {
                    distance += (abs(Latitude - passLatitude)) + abs(Longitude - passLongtitude)/2  * 110;
                    Log.v("distance",Double.toString(distance));
                    Log.v("Latitude",Double.toString(abs(Latitude-passLatitude)));
                    Log.v("Longitude",Double.toString(abs(Longitude-passLongtitude)));
                    String tContent = String.format("緯差 : %s\n經度差 : %s距離",abs(Latitude),abs(Longitude),distance);
                    id++;
                    db.execSQL("INSERT INTO tableGPS(_id,loc_x,loc_y,distance) values (" + id + "," + Latitude+ "," + Longitude+ "," + distance + ")");
                    Toast.makeText(v.getContext(), tContent+" \n+5寵物幣", Toast.LENGTH_SHORT).show();
                    paintLine();
                    addMomey();

                }else{
                    Toast.makeText(v.getContext(), "沒有移動 ", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public void addMomey(){
        dbStore  = getActivity().openOrCreateDatabase("SportApp.db", MODE_PRIVATE, null);
        Cursor cursor = dbStore.rawQuery("SELECT * FROM tableWallet", null);
        cursor.moveToFirst();
        wallet = cursor.getInt(1);
        wallet += 5;
        dbStore.execSQL("update tableWallet set money= "+ wallet + " where _id=0");
        //db.execSQL("");
    }
    public void paintLine() {
        Cursor cursor = getAll("tableGPS");
        PolylineOptions rectOptions = new PolylineOptions();
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                rectOptions.add(new LatLng(cursor.getDouble(1), cursor.getDouble(2)));
                cursor.moveToNext();
            }
        }
        Polyline polyline = mMap.addPolyline(rectOptions);

    }

    void upDateWallet(){
        Cursor cursor=getAll("tableWallet");
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            wallet = cursor.getInt(1);


        }
    }

    public Cursor getAll(String tableName) {
        Cursor cursor = db.rawQuery("SELECT * FROM "+tableName, null);
        return cursor;
    }

}
