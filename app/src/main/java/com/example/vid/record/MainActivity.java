package com.example.vid.record;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText mass;
    String massValue;
    TextView text;
    int[] massArray={89,88,87};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mass = (EditText) findViewById(R.id.vnos_teze);
        text = (TextView) findViewById(R.id.test);
    }

    public void sendMessage(View v) {

        massValue = mass.getText().toString();
        text.setText(massValue);

        Intent i = new Intent(MainActivity.this, Form.class);
        i.putExtra("mass", massArray);
        startActivity(i);
    }
}
