package com.example.momentary.sportapp;
import android.app.Fragment;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import com.bumptech.glide.Glide;

public class HomeActivity extends android.support.v4.app.Fragment {
    public Spinner spnFood;
    String[] Food=new String[] {"拉雞飼料","可以飼料","很棒飼料","終極飼料"};
    View v;
    int screenWide;
    int screenHeight;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_home, container, false);
        // 拿到螢幕大小
        setBoard();

        ImageButton imageButton=(ImageButton)v.findViewById(R.id.imgFood); //R.id.imageButton 取得元件變數的id
        imageButton.setOnClickListener(onClick); //onclick  執行方法名稱 此為設定要偵聽的動作
        spnFood=(Spinner) v.findViewById(R.id.spnFood);
        ArrayAdapter <String> adapterFood=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,Food);
        adapterFood.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnFood.setAdapter(adapterFood);

        return v;
    }

    private void setBoard(){
        DisplayMetrics monitorsize =new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(monitorsize);
        screenWide=monitorsize.widthPixels;
        screenHeight=monitorsize.heightPixels;
        //Layout layout=(Layout) v.findViewById(R.id.backgroud_home);

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

        }
    };

}
