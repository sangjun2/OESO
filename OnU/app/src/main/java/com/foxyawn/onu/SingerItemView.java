package com.foxyawn.onu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jsb on 2017-11-29.
 */

public class SingerItemView extends LinearLayout {
    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    ImageView imageView;

    public SingerItemView(Context context){
        super(context);
        init(context);
    }

    public SingerItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_singer_item,this, true);

        textView =(TextView)findViewById(R.id.company);
        textView2 =(TextView)findViewById(R.id.name);
        textView3 =(TextView)findViewById(R.id.price);
        textView4 =(TextView)findViewById(R.id.comment);
        imageView = (ImageView) findViewById(R.id.imageView);
    }
    public void setPlaceType(String placeType){textView.setText("["+placeType+"]");}
    public void setDistrict(String district){textView2.setText(district);}
    public void setPerson(String person){textView3.setText("수용인원 : "+ person);}
    public void setaddress(String address){textView4.setText(address);}
    public void setImage(int resId){imageView.setImageResource(resId);}
}
