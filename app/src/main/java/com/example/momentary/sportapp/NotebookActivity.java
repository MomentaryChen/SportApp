package com.example.momentary.sportapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class NotebookActivity extends android.support.v4.app.Fragment {

    private final static String createTitle = "CREATE TABLE tableFodder(_id interger not null,name text, count int, price int)";
    private final static String createContent= "CREATE TABLE tableWallet(_id interger not null,money int)";

    View v ;

    private TextView show;
    private EditText title;
    private EditText content;
    private Button btnadd;
    private Button btndel;
    private Button btnchange;
    private Button btnsearch;

    @SuppressLint("MissingPermission")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_notebook, container, false);

        Spinner spinner=(Spinner) v.findViewById(R.id.spinner);


        show =(TextView)v.findViewById(R.id.textView_show);
        title =(EditText)v.findViewById(R.id.editText_title);
        content =(EditText)v.findViewById(R.id.editText_content);
        btnadd = (Button)v.findViewById(R.id.Add);
        btnadd.setOnClickListener(rec_add);
        btndel = (Button)v.findViewById(R.id.Delete);
        btndel.setOnClickListener(rec_del);
        btnchange = (Button)v.findViewById(R.id.Change);
        btnchange.setOnClickListener(rec_change);
        btnsearch = (Button)v.findViewById(R.id.Search);
        btnsearch.setOnClickListener(rec_search);

        String[] lunch = {"雞腿飯", "魯肉飯", "排骨飯", "水餃", "陽春麵"};
        ArrayAdapter<String> lunchList = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                lunch);
        spinner.setAdapter(lunchList);
        return v;
    }

    private View.OnClickListener rec_add = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            String addtitle = title.getText().toString();
            String addcontent = content.getText().toString();


        }
    };

    private View.OnClickListener rec_del = new View.OnClickListener(){
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener rec_change = new View.OnClickListener(){
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener rec_search = new View.OnClickListener(){
        @Override
        public void onClick(View view) {

        }
    };

}
