package com.example.vid.record;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText mass;
    String massValue;
    int[] massArray={89,88,87};
    TextView test;
    String sessionId = "";
    ArrayList<String> seznam = new ArrayList<String>();

    String weight;
    String body_water;
    String heart_rate;
    String ankle_circ;

    EditText weight_form;
    EditText body_water_form;
    EditText heart_rate_form;
    EditText ankle_circ_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mass = (EditText) findViewById(R.id.vnos_teze);

        test = (TextView) findViewById(R.id.test);

        weight_form = (EditText) findViewById(R.id.editText);
        body_water_form = (EditText) findViewById(R.id.editText2);
        heart_rate_form = (EditText) findViewById(R.id.editText3);
        ankle_circ_form = (EditText) findViewById(R.id.editText4);

    }

    public void record(View v) {

        weight = weight_form.getText().toString();
        body_water = body_water_form.getText().toString();
        heart_rate = heart_rate_form.getText().toString();
        ankle_circ = ankle_circ_form.getText().toString();

        String[] array = {weight, body_water, heart_rate, ankle_circ};

        Intent i = new Intent(MainActivity.this, second.class);
        i.putExtra("meritve", array);
        startActivity(i);
    }
}
