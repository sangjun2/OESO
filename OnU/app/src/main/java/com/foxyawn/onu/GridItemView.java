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

public class GridItemView extends LinearLayout {
    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    ImageView imageView;

    public GridItemView(Context context){
        super(context);
        init(context);
    }

    public GridItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_grid_item,this, true);

        textView =(TextView)findViewById(R.id.name);
        textView2 =(TextView)findViewById(R.id.address);
        textView3 =(TextView)findViewById(R.id.introduce);
        textView4 =(TextView)findViewById(R.id.price);
        imageView = (ImageView) findViewById(R.id.imageView);
    }
    public void setName(String name){textView.setText(name);}
    public void setAddress(String address){textView2.setText(address);}
    public void setIntroduce(String introduce){textView3.setText(introduce);}
    public void setPrice(String price){textView4.setText(price);}
    public void setImage(int resId){imageView.setImageResource(resId);}
    public ImageView getImageView(){return imageView;}
}
