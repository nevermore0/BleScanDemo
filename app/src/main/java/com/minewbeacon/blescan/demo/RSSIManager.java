package com.minewbeacon.blescan.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yuliwuli.blescan.demo.R;

public class RSSIManager extends Activity{
    public EditText set1m;
    public EditText set6m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssi);

        Button btn1 = (Button) findViewById(R.id.setBack);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(RSSIManager.this , MainActivity.class);
                startActivity(i);
            }
        });

        set1m =(EditText) findViewById(R.id.edit1m);
        Button btn1m = (Button) findViewById(R.id.confirm1m);

        set6m =(EditText) findViewById(R.id.edit6m);
        Button btn6m = (Button) findViewById(R.id.confirm6m);

    }

    public void onClick1m(View view) {
        String reads = set1m.getText().toString();
        double newrssi = Double.parseDouble(reads);
        MainActivity.userSet1m(newrssi);
    }

    public void onClick6m(View view) {
        String reads = set6m.getText().toString();
        double newrssi = Double.parseDouble(reads);
        MainActivity.userSet6m(newrssi);
    }
}