package com.example.momentary.sportapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class NotebookActivity extends android.support.v4.app.Fragment {

    private final static String table_name ="tableFile";
    private final static String createTable = "CREATE TABLE tableFile(name text not null unique, content text)";
    ListView list ;
    private SQLiteDatabase db=null;
    Cursor cursor=null;
    ArrayList<String> txtName;
    View v ;
    private Button btnadd;

    @SuppressLint("MissingPermission")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_notebook, container, false);

        btnadd = (Button)v.findViewById(R.id.Add);
        btnadd.setOnClickListener(rec_add);
        list =v.findViewById(R.id.notebook_list);
        getActivity().setTitle("筆記本");
        TextView emp_list_show=(TextView)v.findViewById(R.id.emp_list_show);      //把Text 設成看不見   要當listview 的空的註解
        list.setEmptyView(emp_list_show);
        txtName = new ArrayList<String>();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(getActivity());
        db = dbOpenHelper.getWritableDatabase();
        //db.execSQL("drop table " + table_name);                   //刪除table

        try{
            db.execSQL(createTable);
            db.execSQL("Insert into "+table_name+"(name,content) values ('心得111','好吃好吃')");
            db.execSQL("Insert into "+table_name+"(name,content) values ('心得j嗚嗚 ','好吃好吃')");
        }catch (SQLException e){
            e.printStackTrace();
        }
        Cursor cursor = getAll();
        cursor.moveToFirst();
        txtName.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            txtName.add(cursor.getString(0));
            cursor.moveToNext();
        }
        ArrayAdapter adapter = new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, txtName);
        if(!adapter.isEmpty()){
            list.setAdapter(adapter);
            list.setOnItemClickListener(listener);
        }

        return v;
    }

    private View.OnClickListener rec_add = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass((WeightButtonActivity)getActivity(),Notebook.class );
            intent.putExtra("action_key",0);
            startActivity(intent);
        }
    };

    public ListView.OnItemClickListener listener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            intent.setClass((WeightButtonActivity)getActivity(),Notebook.class );
            intent.putExtra("action_key",1);                    //放入String name   要讓開啟的Activity的知道已經有了這個資料夾並取出資料
            intent.putExtra("name",txtName.get(position));
            //intent.putExtra("name",name);                              //傳送名稱, 內容
            // intent.putExtra("cotent",content);
            startActivity(intent);

        }
    };

    public Cursor getAll(){
        Cursor c = db.rawQuery("select * from "+ table_name,null);
        return c;
    }

}
