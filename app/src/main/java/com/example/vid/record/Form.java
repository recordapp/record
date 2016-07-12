package com.example.vid.record;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class Form extends AppCompatActivity {

    TextView test;
    String sessionId="";
    String mass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Bundle extras = getIntent().getExtras();
        int[] massArray = extras.getIntArray("mass");

        test = (TextView) findViewById(R.id.test);

        mass2 = Integer.toString(massArray[2]);
    }

    public void sendMessage(View v) {

        String patientIdEhr = "50bc1c98-8540-4d86-bbc7-813f7c55377e";

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://rest.ehrscape.com/rest/v1/session?username=medrockweek1&password=medrockweek1";

        String url2 = "https://rest.ehrscape.com/rest/v1/composition?ehrId=" + patientIdEhr +

                "&templateId=Heart%20Failure%20Detection&format=FLAT&committer=";

        //Spremenljivke za čas
        Calendar c = Calendar.getInstance();
        int minutes = c.get(Calendar.MINUTE);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        //Sestavimo format časa
        final String time = year+"-"+month+"-"+day+"T"+hours+":"+minutes+"Z";

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

        //Zgeneriramo HashMap, kjer so vpisani naslovi do posameznih arhetipov in njihove konkkretne vrednosti za vpis
        //Tole se bo vpisalo v body drugega requesta
        Map<String,String> params = new HashMap<String, String>();
        params.put("ctx/language","en");
        params.put("ctx/territory", "SI");
        params.put("encounter/body_weight:0/any_event:0/weight|magnitude", "70");
        params.put("encounter/body_weight:0/any_event:0/weight|unit", "kg");

        //DRUGI REQUEST. V glavi pošljemo sessionID, v body-ju pa podatke za vpis. Oboje je v formatu JSON
        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest
                (Request.Method.POST, url2, new JSONObject(params), new Response.Listener<JSONObject>() {
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
        queue.add(jsObjRequest2);
    };
}
