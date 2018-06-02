package com.example.momentary.sportapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;




public class NotebookActivity extends AppCompatActivity {

    FileProcess fp;
    boolean bSDCard = false; // true;
    private Button btn_addnote;
    private ListView lv_notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);

        fp = new FileProcess(this, bSDCard);

        btn_addnote = (Button) findViewById(R.id.btn_addnote);
        btn_addnote.setOnClickListener(addnote);

        lv_notes = (ListView) findViewById(R.id.lv_notes);
        lv_notes.setOnItemClickListener(iclick);
        lv_notes.setOnItemLongClickListener(ilclick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<String> notelist = fp.getNoteList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, notelist);
        lv_notes.setAdapter(adapter);
    }

    private View.OnClickListener addnote = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(NotebookActivity.this, NoteEditor.class);
            intent.putExtra("SDCARD", bSDCard);
            intent.putExtra("NOTEPOS", -1);
            startActivity(intent);
        }
    };

    AdapterView.OnItemClickListener iclick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> av, View v,
                                int position, long id) {

            Intent intent = new Intent();
            intent.setClass(NotebookActivity.this, NoteEditor.class);
            intent.putExtra("SDCARD", bSDCard);
            intent.putExtra("NOTEPOS", position);
            startActivity(intent);
        }
    };

    AdapterView.OnItemLongClickListener ilclick = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> av, View v,
                                       int position, long id) {

            fp.delNote(position);
            ArrayList<String> notelist = fp.getNoteList();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (NotebookActivity.this, android.R.layout.simple_list_item_1, notelist);
            lv_notes.setAdapter(adapter);
            return false;
        }
    };
}
