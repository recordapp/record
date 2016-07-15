package com.example.vid.record;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class second extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle extras = getIntent().getExtras();
        String[] arrayB = extras.getStringArray("meritve");

        TextView t = (TextView) findViewById(R.id.textView);
        t.setText(arrayB[1]);
    }

    public void record2(View v) {
        Intent i = new Intent(second.this, results.class);
        startActivity(i);
    }
}
