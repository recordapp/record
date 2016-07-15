package com.example.vid.record;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class vpis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpis);
    }

    public void sign(View v) {
        Intent i = new Intent(vpis.this, MainActivity.class);
        startActivity(i);
    }
}
