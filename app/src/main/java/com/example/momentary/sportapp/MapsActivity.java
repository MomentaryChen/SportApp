package com.example.momentary.sportapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.security.Provider;
import java.security.spec.ECField;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class MapsActivity extends android.support.v4.app.Fragment implements  OnMapReadyCallback {
    SupportMapFragment mMapView;
    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    private MapView mapView;
    private LocationManager locMgr;
    private final static String createGPSTable = "CREATE TABLE tableGPS(_id integer not null,loc_x real,loc_y real,distance int)";
    private SQLiteDatabase db = null;
    int distance=0;
    int id=0;
    String bestProv;
    View v;
    private Handler handler = new Handler();

    @SuppressLint("MissingPermission")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_maps, container, false);
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();// needed to get the map to display immediately
        mapView.getMapAsync(this);
        locMgr = (LocationManager) v.getContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        bestProv = locMgr.getBestProvider(criteria, true);
        db = getActivity().openOrCreateDatabase("SportApp.db", MODE_PRIVATE, null);
        handler.removeCallbacks(updateTimer);
        //設定Delay的時間
        handler.postDelayed(updateTimer, 5000);
        return v;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        getActivity().setTitle("地圖");
        Location location=locMgr.getLastKnownLocation(bestProv);
        LatLng currentLoc = new LatLng(location.getLatitude(),location.getLongitude());
        //db.execSQL("drop table tableGPS");
        try{
            db.execSQL(createGPSTable);
            db.execSQL("INSERT INTO tableGPS(_id,loc_x,loc_y,distance) values ("+id+","+location.getLatitude()+","+location.getLongitude()+","+0+")");
        }catch (Exception e){
        }
        Cursor cursor=getAll("tableGPS");
        if(cursor==null || cursor.getCount()==1){
            distance=0;
            id=1;
        }else{
            cursor.moveToLast();
            distance=cursor.getInt(3);
            id=cursor.getInt(0);
        }
        //db.execSQL("drop table tableGPS");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc,14));
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);                                         //地圖的初始型態
        mMap.getUiSettings().setCompassEnabled(true);                                              //指南針顯示設定
        mMap.getUiSettings().setAllGesturesEnabled(true);//設定所有手勢控制
        mMap.setMyLocationEnabled(true);

        paintLine();

    }

    private Runnable updateTimer = new Runnable() {     //Timer
        @SuppressLint("MissingPermission")
        public void run() {
            try{
                locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,gLListenr);
            }catch (Exception e){
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
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc,14));
                Cursor cursor=getAll("tableGPS");
                cursor.moveToLast();
                double passLatitude=cursor.getDouble(1);
                double passLongtitude=cursor.getDouble(2);
                String tContent = String.format("緯差 : %s\n經度差 : %s距離",abs(Latitude-passLatitude),abs(Longitude-passLongtitude),distance);
                if(abs(Latitude-passLatitude)>=0.000000001 || abs(Longitude-passLongtitude)>=0.000000001) {
                    distance += pow(Math.pow(abs(Latitude - passLatitude), 2) + Math.pow(abs(Longitude - passLongtitude), 2), 1 / 2) * 110;
                    //String tContent = String.format("緯 : %s\n經度 : %s\n距離 : %s",location.getLatitude(),location.getLongitude(),distance);
                    id++;
                    db.execSQL("INSERT INTO tableGPS(_id,loc_x,loc_y,distance) values (" + id + "," + Latitude+ "," + Longitude+ "," + distance + ")");
                    Toast.makeText(v.getContext(), tContent, Toast.LENGTH_SHORT).show();
                    paintLine();
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
        Toast.makeText(v.getContext(), "路線更新", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        db.close();
        distance=0;
        id=0;
    }

    public Cursor getAll(String tableName) {
        Cursor cursor = db.rawQuery("SELECT * FROM "+tableName, null);
        return cursor;
    }

}
