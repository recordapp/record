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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText mass;
    String massValue;
    int[] massArray={89,88,87};
    TextView test;
    String sessionId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mass = (EditText) findViewById(R.id.vnos_teze);

        test = (TextView) findViewById(R.id.test);

        String patientIdEhr = "50bc1c98-8540-4d86-bbc7-813f7c55377e";

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://rest.ehrscape.com/rest/v1/session?username=medrockweek1&password=medrockweek1";

        String url2 ="https://rest.ehrscape.com/rest/v1/view/"+patientIdEhr+"/weight";

        //request
        //PRVI REQUEST. V URL-ju pošljemo geslo in username, kot odgovor pa dobimo SessionID.
        //Head in Body requesta sta prazna
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //opstring se uporabi, ker v primeru napacnega keya vrne null string.
                        sessionId = response.optString("sessionId", null);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        test.setText("Prišlo je do napake med identifikacijo na strežniku!");
                    }
                });

        //Dodamo request v queue
       // queue.add(jsObjRequest);

        Map<String,String> params = new HashMap<String, String>();

        //DRUGI REQUEST. V glavi pošljemo sessionID, v body-ju pa podatke za vpis. Oboje je v formatu JSON
        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest
                (Request.Method.GET, url2, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        test.setText("Vpis je bil uspešno poslan!");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        test.setText("Prišlo je do napake med prenosom podatkov!");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Glava (head)
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Ehr-Session", sessionId);
                return headers;
            }
        };
        //queue.add(jsObjRequest2);
    }

    public void openForm(View v) {

        massValue = mass.getText().toString();
        Intent i = new Intent(MainActivity.this, Form.class);
        i.putExtra("mass", massArray);
        startActivity(i);
    }

    public void getData(View V) {
        String patientIdEhr = "50bc1c98-8540-4d86-bbc7-813f7c55377e";

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://rest.ehrscape.com/rest/v1/session?username=medrockweek1&password=medrockweek1";

        String url2 ="https://rest.ehrscape.com/rest/v1/view/"+patientIdEhr+"/weight";

        //request
        //PRVI REQUEST. V URL-ju pošljemo geslo in username, kot odgovor pa dobimo SessionID.
        //Head in Body requesta sta prazna
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //opstring se uporabi, ker v primeru napacnega keya vrne null string.
                        sessionId = response.optString("sessionId", null);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        test.setText("Prišlo je do napake med identifikacijo na strežniku!");
                    }
                });

        //Dodamo request v queue
        queue.add(jsObjRequest);

        Map<String,String> params = new HashMap<String, String>();

        //DRUGI REQUEST. V glavi pošljemo sessionID, v body-ju pa podatke za vpis. Oboje je v formatu JSON
        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        test.setText("Vpis je bil uspešno poslan!");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        test.setText("Prišlo je do napake med prenosom podatkov!");
                        test.setText(error.toString());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Glava (head)
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Ehr-Session", sessionId);
                return headers;
            }
        };
        queue.add(jsObjRequest2);
    }
}
