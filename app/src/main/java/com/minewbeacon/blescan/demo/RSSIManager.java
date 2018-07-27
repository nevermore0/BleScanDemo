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
    public EditText edt;

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

        Button btn2 = (Button) findViewById(R.id.confirm);
        edt =(EditText) findViewById(R.id.edit);

    }

    public void onClick(View view) {
        String reads = edt.getText().toString();
        double newrssi = Double.parseDouble(reads);
        MainActivity.userSetRSSI(newrssi);
    }
}