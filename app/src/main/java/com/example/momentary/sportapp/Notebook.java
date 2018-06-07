package com.example.momentary.sportapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Notebook extends AppCompatActivity {
    String name="";
    String content="";
    EditText edtName,edtContent;
    Button bntDelete;
    private final static String table_name ="tableFile";
    private final static String createTable = "CREATE TABLE tableFile(name text not null unique, content text)";
    private SQLiteDatabase db=null;
    Cursor cursor=null;
    String firstName;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notebook);
        edtName = findViewById(R.id.name);
        edtContent = findViewById(R.id.content);
        bntDelete = findViewById(R.id.bntdelete);
        intent = getIntent();

        bntDelete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(firstName,content,2);
                startMain();
            }
        });

        DBOpenHelper dbOpenHelper = new DBOpenHelper(this);
        db = dbOpenHelper.getWritableDatabase();
        //setTitle(Integer.toString(action)+name);

        int action = intent.getIntExtra("action_key", 0);  //show the name and content
        if (action == 1) {
            firstName = intent.getStringExtra("name");
            edtName.setText(firstName);
            setTitle(firstName);
            cursor = getAll();
            cursor.moveToFirst();

            for(int i=0;i<cursor.getCount();i++){
                if(cursor.getString(0).equals(firstName)){
                    content=cursor.getString(1);
                    //Toast.makeText(this, firstName+"  "+content,Toast.LENGTH_SHORT).show();
                    edtContent.setText(content);
                    break;
                }
                cursor.moveToNext();
            }
        }
    }

    public void update(String name,String content,int act){                                         //0.新建 1.修改 2.刪除
        try{
            if(name.equals("")) {
                Toast.makeText(this,"名稱沒有輸入",Toast.LENGTH_SHORT).show();
                return;
            }
            if(act==0){
                db.execSQL("Insert into "+table_name+"(name,content) values ('"+name+"' , '"+ content+ "') ");
                Toast.makeText(this,"儲存成功",Toast.LENGTH_SHORT).show();
            }else if(act==1){
                db.execSQL("update "+table_name+" set name ='"+name+"' , content = '"+content +"' where name = '"+firstName+"'");
                Toast.makeText(this,"儲存成功",Toast.LENGTH_SHORT).show();
            }else if(act==2){
                db.execSQL("delete from "+table_name+" where  name='"+firstName+"'");
                Toast.makeText(this,"刪除成功",Toast.LENGTH_SHORT).show();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String name ,content ;
        int action = intent.getIntExtra("action_key",0);

        name=edtName.getText().toString();
        content=edtContent.getText().toString();
        if(action==0){
            update(name,content,0);
        }else if (action==1){
            update(name,content,1);
        }
        startMain();
    }
    public void startMain(){
        Intent intent = new Intent();
        intent.setClass(Notebook.this,WeightButtonActivity.class);
        startActivity(intent);
        finish();
    }

    public Cursor getAll(){
        Cursor c = db.rawQuery("select * from "+ table_name,null);
        return c;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
