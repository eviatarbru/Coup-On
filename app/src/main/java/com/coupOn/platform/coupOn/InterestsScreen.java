package com.coupOn.platform.coupOn;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shashank.platform.coup_on.R;

import java.util.Arrays;

public class InterestsScreen extends AppCompatActivity implements View.OnClickListener {

    private boolean [] isPressed;
    private String [] interests = {"Gaming", "Utility", "Entertainment", "Merch", "Spa", "Movies", "Sports", "Animals", "Flights", "Transportation", "Museums and shit", "Gayming"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests_screen);
        int val;
        String st;
        RelativeLayout tempLayout;
        GridLayout container = findViewById(R.id.container);
        container.setColumnCount(3);
        container.setRowCount(this.interests.length / 3);

//        LinearLayout container = findViewById(R.id.container);
//        RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        RelativeLayout.LayoutParams paramsLeft, paramsCenter, paramsRight;
//        paramsLeft = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//        paramsCenter = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//        paramsRight = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//        paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//        paramsCenter.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//        paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
//        paramsLeft.addRule(RelativeLayout.LEFT);
        GridLayout.LayoutParams params;
        this.isPressed = new boolean[this.interests.length];
        for(int i = 0; i < this.interests.length / 3 * 3; i++)
        {
//            tempLayout = new RelativeLayout(this);
//            tempLayout.setLayoutParams(relParams);
//            val = 3 * i + 1;
//            st = interests[val];
//            tempLayout.addView(createButton(st, val - 1, -1), paramsLeft);
//            tempLayout.addView(createButton(st, val, 0), paramsCenter);
//            tempLayout.addView(createButton(st, val + 1, 1), paramsRight);
//            container.addView(tempLayout);
            params = new GridLayout.LayoutParams();
            params.height = LayoutParams.WRAP_CONTENT;
            params.width = LayoutParams.WRAP_CONTENT;
            st = interests[i];
            params.columnSpec = GridLayout.spec(i % 3, 1, 1);
            params.rowSpec = GridLayout.spec(i / 3, 1, 1);
            params.topMargin = 30;
            params.rightMargin = 5;
            params.leftMargin = 5;
            container.addView(createButton(st, i), params);
        }
    }

    public Button createButton(String st, int val)
    {
        Button btn;
        btn = new Button(this);
        btn.setId(val);
//        btn.setTag(val);
        btn.setText(st);
        btn.setAllCaps(false);
        btn.setOnClickListener(this);
        btn.setTextColor(getResources().getColor(R.color.textInputLayout));
        btn.setBackground(getDrawable(R.drawable.gridbuttonunselected));
        return btn;
    }

    @Override
    public void onClick(View view) {
        int index = view.getId();
        this.isPressed[index] = !this.isPressed[index];
        if(this.isPressed[index])
        {
            ((Button)view).setTextColor(getResources().getColor(R.color.textInputLayoutLight));
            view.setBackground(getDrawable(R.drawable.gridbuttonselected));
        }
        else
        {
            ((Button)view).setTextColor(getResources().getColor(R.color.textInputLayout));
            view.setBackground(getDrawable(R.drawable.gridbuttonunselected));
        }
        System.out.println(Arrays.toString(this.isPressed));
    }

    public void registerComplition(View view)
    {
        for(int i = 0; i < this.isPressed.length; i++)
        {
            if(this.isPressed[i])
                System.out.println(this.isPressed[i]);
        }
    }
}