package com.example.vid.record;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText mass;
    String massValue;
    int[] massArray={89,88,87};
    TextView test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mass = (EditText) findViewById(R.id.vnos_teze);

    }

    public void sendMessage(View v) {

        massValue = mass.getText().toString();

        test = (TextView) findViewById(R.id.test);

        String patientIdEhr = "50bc1c98-8540-4d86-bbc7-813f7c55377e";

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://ip.jsontest.com/";

        //request
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        test.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        test.setText("Prišlo je do napake med identifikacijo na strežniku!");
                    }
                });

        queue.add(jsObjRequest);

        /*Intent i = new Intent(MainActivity.this, Form.class);
        i.putExtra("mass", massArray);
        startActivity(i);*/
    }
}
