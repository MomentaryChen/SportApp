package com.example.momentary.sportapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class StoreActivity extends  android.support.v4.app.Fragment {
    private final static String createTable = "CREATE TABLE tableFodder(_id interger not null,name text, count int, price int)";
    private final static String createWalletTable = "CREATE TABLE tableWallet(_id interger not null,money int)";
    private SQLiteDatabase db=null;
    ListView shopping_list;
    TextView txtPrice;
    Cursor cursor;
    View v ;
    int wallet;
    int fodderCount[]=new int [4];

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_store, container, false);
        shopping_list=(ListView) v.findViewById(R.id.shopping_list);
        TextView emp_list_show=(TextView)v.findViewById(R.id.emp_list_show);      //把Text 設成看不見   要當listview 的空的註解
        shopping_list.setEmptyView(emp_list_show);
        txtPrice=(TextView) v.findViewById(R.id.fodder_price);
        getActivity().setTitle("飼料商店");
        db = getActivity().openOrCreateDatabase("SportApp.db", MODE_PRIVATE, null);
        try{
            db.execSQL(createTable);
            db.execSQL("INSERT INTO tableFodder(_id,name,count,price) values (1,'拉雞飼料',0,10)");
            db.execSQL("INSERT INTO tableFodder(_id,name,count,price) values (2,'可以飼料',0,30)");
            db.execSQL("INSERT INTO tableFodder(_id,name,count,price) values (3,'很棒飼料',0,50)");
            db.execSQL("INSERT INTO tableFodder(_id,name,count,price) values (4,'終極飼料',0,90)");
        }catch (Exception e){
            //getActivity().setTitle("already insert ");
        }

        upDateFodderCount();
        shopping_list.setOnItemClickListener(listviewDoListener);
        //拿取錢包的資料庫
        try{
            db.execSQL(createWalletTable);
            db.execSQL("INSERT INTO tableWallet(_id,money) values (0,10000)");
        }catch (Exception e){
        }

        /*try{
            db.execSQL("UPDATE tableWallet SET money=10000 where _id=0");
        }catch (Exception e){

        }*/
       upDateWallet(); //顯示還有多少錢
        //db.execSQL("drop table tableWallet");
        //db.execSQL("drop table tableFodder");
        db.close();
        return v;
    }
    void Destroy(){
        db.close();
    }

    void upDateFodderCount(){
        Cursor cursor=getAll("tableFodder");
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            for(int i=0;i<cursor.getCount()-1;i++){
                fodderCount[i]=cursor.getInt(2);
                cursor.moveToNext();
            }
        }

        cursor=getAll("tableFodder");
        if (cursor != null && cursor.getCount() >= 0) {                             //資料庫載入Listview
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(v.getContext(), R.layout.display_shpping_list, cursor, new String[]{"_id","name", "count","price"
            }, new int[]{R.id._id,R.id.txtshowname, R.id.txtshowhavecount,R.id.txtshowprice}, 0);
            shopping_list.setAdapter(adapter);
        }
    }

    void upDateWallet(){
        Cursor cursor=getAll("tableWallet");
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            wallet = cursor.getInt(1);
            String money=Integer.toString(wallet);
            txtPrice.setText(money);
        }
    }

    private ListView.OnItemClickListener listviewDoListener=new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            int cost=0;
            db = getActivity().openOrCreateDatabase("SportApp.db", MODE_PRIVATE, null);
            switch (i){
                case 0:{
                    cost=10;
                    if(wallet<cost){
                        Toast.makeText(getActivity(),"不夠10元",Toast.LENGTH_SHORT).show();
                    }else{
                        db.execSQL("update tableWallet set money="+(wallet-cost)+ "  where _id=0");
                        db.execSQL("update tableFodder set count="+(++fodderCount[0])+" where _id=1");
                        upDateWallet();
                        upDateFodderCount();
                        db.close();
                    }
                    break;
                }
                case 1:{
                    cost=30;
                    if(wallet<cost){
                        Toast.makeText(getActivity(),"不夠30元",Toast.LENGTH_SHORT).show();
                    }else{
                        db.execSQL("update tableWallet set money="+(wallet-cost)+ "  where _id=0");
                        db.execSQL("update tableFodder set count="+(++fodderCount[1])+" where _id=2");
                        upDateWallet();
                        upDateFodderCount();
                        db.close();
                    }
                    break;
                }
                case 2:{
                    cost=50;
                    if(wallet<cost){
                        Toast.makeText(getActivity(),"不夠50元",Toast.LENGTH_SHORT).show();
                    }else{
                        db.execSQL("update tableWallet set money="+(wallet-cost)+ "  where _id=0");
                        db.execSQL("update tableFodder set count="+(++fodderCount[2])+" where _id=3");
                        upDateWallet();
                        upDateFodderCount();
                        db.close();
                    }
                    break;
                }
                case 3:{
                    cost=90;
                    if(wallet<cost){
                        Toast.makeText(getActivity(),"不夠90元",Toast.LENGTH_SHORT).show();
                    }else{
                        db.execSQL("update tableWallet set money="+(wallet-cost)+ "  where _id=0");
                        db.execSQL("update tableFodder set count="+(++fodderCount[3])+" where _id=4");
                        upDateWallet();
                        upDateFodderCount();
                        db.close();
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public Cursor getAll(String tableName) {
        Cursor cursor = db.rawQuery("SELECT * FROM "+tableName, null);
        return cursor;
    }

}
