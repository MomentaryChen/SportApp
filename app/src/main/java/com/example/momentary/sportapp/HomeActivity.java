package com.example.momentary.sportapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static android.content.Context.MODE_PRIVATE;

public class HomeActivity extends android.support.v4.app.Fragment {
    public Spinner spnFood;
    private final static String createTable = "CREATE TABLE tableFodder(_id interger not null,name text, count int, price int)";
    private final static String createWalletTable = "CREATE TABLE tableWallet(_id interger not null,money int)";
    private final static String createEXPTable = "CREATE TABLE tableEXP(_id interger not null,exp int)";
    String[] Food=new String[] {"拉雞飼料","可以飼料","很棒飼料","終極飼料"};
    View v;
    int screenWide;
    int screenHeight;
    int getFodderId=1;
    String getFodderName;
    private SQLiteDatabase db=null;
    Button feed;
    TextView nowExp;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_home, container, false);
        // 拿到螢幕大小
        setBoard();
        nowExp =v.findViewById(R.id.nowExp);
        ImageButton imageButton=(ImageButton)v.findViewById(R.id.imgFood); //R.id.imageButton 取得元件變數的id
        imageButton.setOnClickListener(onClick); //onclick  執行方法名稱 此為設定要偵聽的動作

        spnFood=(Spinner) v.findViewById(R.id.spnFood);
        ArrayAdapter <String> adapterFood=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,Food);
        adapterFood.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnFood.setAdapter(adapterFood);

        spnFood.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(v.getContext(), "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                getFodderId=position+1;
                getFodderName=adapterView.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(v.getContext(), "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });
        db = getActivity().openOrCreateDatabase("SportApp.db", MODE_PRIVATE, null);
        try{
            db.execSQL(createTable);
            db.execSQL("INSERT INTO tableFodder(_id,name,count,price) values (1,'拉雞飼料',0,10)");
            db.execSQL("INSERT INTO tableFodder(_id,name,count,price) values (2,'可以飼料',0,30)");
            db.execSQL("INSERT INTO tableFodder(_id,name,count,price) values (3,'很棒飼料',0,50)");
            db.execSQL("INSERT INTO tableFodder(_id,name,count,price) values (4,'終極飼料',0,90)");
        } catch (Exception e){

        }
        try{
            db.execSQL(createWalletTable);
            db.execSQL("INSERT INTO tableWallet(_id,money) values (0,10000)");
        }catch (Exception e){
        }

        try{
            db.execSQL(createEXPTable);                                                            // 新增經驗值資料庫
            db.execSQL("INSERT INTO tableEXP(_id,exp) values (0,0)");
        }catch (Exception e){
            //getActivity().setTitle("already insert ");
        }
        Cursor cursor_exp=getAll("tableEXP");
        if(cursor_exp.getCount()>0) {
            cursor_exp.moveToFirst();
            int exp = cursor_exp.getInt(1);
            nowExp.setText(Integer.toString(exp));
        }
        return v;
    }
    private void setBoard(){
        DisplayMetrics monitorsize =new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(monitorsize);
        screenWide=monitorsize.widthPixels;
        screenHeight=monitorsize.heightPixels;

        ImageButton imagePet=(ImageButton)v.findViewById(R.id.imagePet);
        Glide.with(v.getContext())
                .load(R.drawable.bulbasaur_01)
                .asGif()
                .override(screenWide,screenHeight/4)
                .into(imagePet);
    };

    private Button.OnClickListener onClick=new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            Cursor cursor= getAll("tableFodder");
            if(cursor.getCount() > 0) {
                try{
                    cursor.move(getFodderId);
                    int count=cursor.getInt(2);
                    if(count<=0){
                        Toast.makeText(v.getContext(), "請購買" + getFodderName+"  剩餘"+Integer.toString(count), Toast.LENGTH_SHORT).show();
                    }else{
                        count--;
                        db.execSQL("update tableFodder set count= "+count+ " where _id="+getFodderId);
                        Cursor cursor_exp=getAll("tableEXP");
                        if(cursor_exp.getCount()>0){
                            cursor_exp.moveToFirst();
                            int exp = cursor_exp.getInt(1);
                            int get_exp=0;
                            switch (getFodderId){
                                case 1:{
                                    get_exp=10;
                                    exp+=get_exp;
                                    db.execSQL("update tableEXP set exp= "+exp+" where _id= 0");
                                    break;
                                }case 2:{
                                    get_exp=20;
                                    exp+=get_exp;
                                    db.execSQL("update tableEXP set exp= "+exp+" where _id= 0");
                                    break;
                                }case 3:{
                                    get_exp=40;
                                    exp+=get_exp;
                                    db.execSQL("update tableEXP set exp= "+exp+" where _id= 0");
                                    break;
                                }case 4:{
                                    get_exp=80;
                                    exp+=get_exp;
                                    db.execSQL("update tableEXP set exp= "+exp+" where _id=0 ");
                                    break;
                                }
                            }
                            Toast.makeText(v.getContext(),getFodderName+"  剩餘"+Integer.toString(count)+"  經驗值上升 "+get_exp, Toast.LENGTH_SHORT).show();
                            nowExp.setText(Integer.toString(exp));

                        }else{
                        }
                    }
                } catch (Exception e){
                    Toast.makeText(v.getContext(), "餵食Error", Toast.LENGTH_SHORT).show();
                }
            }
        }

    };

    void Destroy(){
        db.close();
    }

    public Cursor getAll(String tableName) {
        Cursor cursor = db.rawQuery("SELECT * FROM "+tableName, null);
        return cursor;
    }

}
