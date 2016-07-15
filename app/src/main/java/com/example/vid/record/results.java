package com.example.vid.record;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class results extends AppCompatActivity {

    String sessionId;
    TextView test;
    ArrayList<String> seznam = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        test = (TextView) findViewById(R.id.textView3);

        seznam = getIntent().getStringArrayListExtra("seznam");

        //int algoritm = algoritm(seznam);
        //test.setText(HeartAttackPrediction(algoritm, 5));
        //test.setText(seznam.toString());
        //test.setText("ss");
    }

    public int algoritm(ArrayList<String> list1) {
        int sestevek = 0;
        int x = 0;
        int n=0;

        for(int i=0; i<(list1.size()-1); i++) {
            x = Integer.parseInt(list1.get(i));
            sestevek = sestevek + x;
            n=i;
            test.setText(""+n);
        }
        int povprecje = x/n;
        //int povprecje=1;

        return ((100*Integer.parseInt(list1.get(n+1)))/povprecje)-100;
        //return 5;
    }

    public String HeartAttackPrediction(int izracun, int meja) {

        if(izracun>meja) {
            return "Please, visit a doctor";
        }
        else {
            return "Everything OK";
        }
    }
}
