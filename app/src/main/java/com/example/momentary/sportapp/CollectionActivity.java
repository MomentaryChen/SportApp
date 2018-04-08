package com.example.momentary.sportapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static android.content.Context.MODE_PRIVATE;


public class CollectionActivity extends android.support.v4.app.Fragment {
    private ImageButton imgbtn0;
    private ImageButton imgbtn1;
    private ImageButton imgbtn2;
    private ImageButton imgbtn3;
    private int distance;
    private final static String createGPSTable = "CREATE TABLE tableGPS(_id integer not null,loc_x decimal,loc_y decimal,distance int)";
    private SQLiteDatabase db=null;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_collection, container, false);
        imgbtn0=(ImageButton)v.findViewById(R.id.imageButton0);
        imgbtn1=(ImageButton)v.findViewById(R.id.imageButton1);
        imgbtn2=(ImageButton)v.findViewById(R.id.imageButton2);
        imgbtn3=(ImageButton)v.findViewById(R.id.imageButton3);
        Glide.with(v.getContext())
                .load(R.drawable.bulbasaur_mini_01)
                .asGif()
                .centerCrop()
                .override(200,200)
                .into(imgbtn0);
        Glide.with(v.getContext())
                .load(R.drawable.pikachu_mini_01)
                .asGif()
                .centerCrop()
                .override(200,200)
                .into(imgbtn1);
        Glide.with(v.getContext())
                .load(R.drawable.charmander_mini_01)
                .asGif()
                .centerCrop()
                .override(200,200)
                .into(imgbtn2);

        db = getActivity().openOrCreateDatabase("SportApp.db", MODE_PRIVATE, null);
        //db.execSQL("drop table tableGPS");
        try{
            db.execSQL(createGPSTable);
            //db.execSQL("INSERT INTO tableGPS(_id,loc_x,loc_y,distance) values (0,24,120,1000)");
        }catch (Exception e){
        }
        Cursor cursor=getAll("tableGPS");
        if(cursor==null ||cursor.getCount()<=0 ){
        }else{
            cursor.moveToLast();
            distance=cursor.getInt(3);
            if(distance>500){
                Toast.makeText(v.getContext(),"You get the new pet",Toast.LENGTH_LONG).show();
                Glide.with(v.getContext())
                        .load(R.drawable.oddish_mini_01)
                        .asGif()
                        .centerCrop()
                        .override(200,200)
                        .into(imgbtn3);
            }
        }
        cursor.moveToLast();
        distance=cursor.getInt(3);
        getActivity().setTitle("圖鑑    已完成距離"+Integer.toString(distance)+"KM");             //debug
        db.close();
        return v;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Cursor getAll(String tableName) {
        Cursor cursor = db.rawQuery("SELECT * FROM "+tableName, null);
        return cursor;
    }

}