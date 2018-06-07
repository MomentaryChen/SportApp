package com.example.momentary.sportapp;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class FileProcess extends NotebookActivity{
    final static String NOTELIST = "NoteList.txt";

    Context mCtx;
    boolean bSDCard;
    File sdfolder;

    FileProcess(Context mCtx, boolean bSDCard)  {
        this.mCtx =mCtx;
        this.bSDCard = bSDCard;

        if (bSDCard) initSDCard();
    }

    private  void initSDCard(){
        File sdroot = Environment.getExternalStorageDirectory();
        File newdir = new File(sdroot.getPath(),"Notepad");
        if(newdir.exists() == false){
            newdir.mkdir();
        }
        sdfolder = newdir;
    }

    ArrayList<String> getNoteList(){
        ArrayList<String> notelist = new ArrayList<String>();
        FileInputStream fis =null;
        try{
            if(bSDCard){
                File listfile = new File(sdfolder, NOTELIST);
                fis = new FileInputStream(listfile);
            } else{
                fis =mCtx.openFileInput(NOTELIST);
            }
            String str;
            do{
                str = readOneLine(fis);
                if (str.equalsIgnoreCase(""))break;
                notelist.add(str);
            } while (true);
            fis.close();
        } catch (Exception e){

        }
        return notelist;
    }
    public String readFile(String title){
        FileInputStream fis =null;
        String fname = title;
        String data="";
        try{
            if(bSDCard){
                File f = new File(sdfolder, fname);
                fis = new FileInputStream(f);
            } else{
                fis =mCtx.openFileInput(fname);
            }
            String str;
            do{
                str = readOneLine(fis);
                data = data+str;
                if (str.equalsIgnoreCase(""))break;

            } while (true);
            fis.close();
        } catch (Exception e){

        }
        return data;
    }
    private String readOneLine(FileInputStream fis){
        StringBuilder str = new StringBuilder("");
        int c;
        try {
            do {
                c= fis.read();
                if((c != '\n')  &&  (c!= -1))
                    str.append((char)c);
            } while ((c != '\n')  &&  (c!= -1));
        } catch (Exception e) {

        }
        return  str.toString();
    }

    void addNote(String title, String body, boolean isNew){
        FileOutputStream fos =null;
        //<title>.txt
        try {
            String fname =title +".txt";
            if (bSDCard) {
                File f =new File(sdfolder,fname);
                fos = new FileOutputStream(f);
            } else{
                fos = mCtx.openFileOutput(fname,
                        Context.MODE_PRIVATE);
            }
            for (int i=0;i< body.length();i++){
                fos.write(body.charAt(i));
            }
            fos.close();
        } catch (Exception e){

        }


        if (isNew == false) return;

        //NoteList.txt*/
        try {
            if (bSDCard){
                File f =new File(sdfolder,NOTELIST);
                fos = new FileOutputStream(f,true);
            }  else {
                fos =mCtx.openFileOutput(NOTELIST,
                        Context.MODE_APPEND);
            }
            for (int i=0;i<title.length();i++){
                fos.write(title.charAt(i));
            }
            fos.write('\n');
            fos.close();
        } catch (Exception e){

        }
    }


    void delNote(int i) {
        ArrayList<String> notelist =getNoteList();
        if((i<0)  || (i>=notelist.size())) return;
        String delTitle = notelist.get(i);
        notelist.remove(i);

        //  NoteList.txt
        FileOutputStream fos = null;
        try{
            if (bSDCard) {
                File f =new File(sdfolder,NOTELIST);
                fos = new FileOutputStream(f);
            } else {
                fos =mCtx.openFileOutput(NOTELIST, Context.MODE_PRIVATE);
            }
            for (int j=0;j<notelist.size();j++){
                String tltle =notelist.get(j);
                for(int k=0;k< delTitle.length();k++){
                    fos.write(delTitle.charAt(k));
                }
                fos.write('\n');
            }
            fos.close();
        } catch (Exception e){

        }

        //<title>.txt
        try {
            String fname = delTitle +".txt";
            if(bSDCard) {
                File f = new File(sdfolder ,fname);
                f.delete();
            } else {
                mCtx.deleteFile(fname);
            }
        } catch (Exception e){

        }
    }
}

