package com.example.momentary.sportapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class NoteEditor extends NotebookActivity {
    FileProcess fp;
    EditText et_title, et_body;

    ArrayList<String> titlelist;


    boolean bSDCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        et_title = (EditText) findViewById(R.id.et_title);
        et_body = (EditText) findViewById(R.id.et_body);



         Intent intent = getIntent();
        bSDCard = intent.getBooleanExtra("SDCARD", false);
         int notepos = intent.getIntExtra("NOTEPOS", -1);

        fp = new FileProcess(this, bSDCard);
        titlelist = fp.getNoteList();


        if (notepos != -1) {
            String title = titlelist.get(notepos);
            et_title.setText(title);
            title = title + ".txt";
          et_body.setText(fp.readFile(title));
        } else {
            et_title.setText("");
            et_body.setText("");
        }




    }

    @Override
    public void onPause() {
        super.onPause();
        String title = et_title.getText().toString();
        if (title.length() == 0) {
            Toast.makeText(this, "標題不可空白，便條無儲存",
                    Toast.LENGTH_LONG).show();
        } else {
            fp.addNote(et_title.getText().toString(),
                    et_body.getText().toString(),
                    !isTitleExist(title));
        }
    }

    boolean isTitleExist(String title) {
        for (int i = 0; i < titlelist.size(); i++)
            if (title.equalsIgnoreCase(titlelist.get(i)))
                return true;
        return false;
    }

}



