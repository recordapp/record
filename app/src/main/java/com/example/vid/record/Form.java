package com.example.vid.record;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Form extends AppCompatActivity {

    TextView test;
    String sessionId="";
    String mass2;

    EditText body_weight_form;
    EditText body_fluids_form;
    EditText heart_rate_form;
    EditText circ_form;

    String body_weight;
    String body_fluids;
    String heart_rate;
    String circ;
    String position_hr;
    String position_circ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> seznam = new ArrayList<String>();
        seznam = getIntent().getStringArrayListExtra("mass");

        test = (TextView) findViewById(R.id.test);

        test.setText(seznam.toString());

        //mass2 = Integer.toString(massArray[2]);

        body_weight_form = (EditText) findViewById(R.id.weight_form);
        body_fluids_form = (EditText) findViewById(R.id.body_fluid_form);
        heart_rate_form = (EditText) findViewById(R.id.heart_rate_form);
        circ_form = (EditText) findViewById(R.id.circumference_form);
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

        //Pridobivanje podatkov
        body_fluids = body_fluids_form.getText().toString();
        body_weight = body_weight_form.getText().toString();
        circ = circ_form.getText().toString();
        heart_rate = heart_rate_form.getText().toString();

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
        params.put("encounter/body_weight:0/any_event:0/weight|magnitude", body_weight);
        params.put("encounter/body_weight:0/any_event:0/weight|unit", "kg");
        params.put("encounter/pulse_heart_beat:0/any_event:0/position", position_hr);
        params.put("encounter/body_water:0/total_water_percentage|magnitude", body_fluids);
        params.put("encounter/body_water:0/total_water_percentage|unit", "%");
        params.put("encounter/pulse_heart_beat:0/any_event:0/heart_rate|magnitude", heart_rate);//
        params.put("encounter/pulse_heart_beat:0/any_event:0/heart_rate|unit", "/min");
        params.put("encounter/measurement_of_body_segment:0/ankle_circumference:0|magnitude", circ);
        params.put("encounter/measurement_of_body_segment:0/ankle_circumference:0|unit", "cm");
        params.put("encounter/measurement_of_body_segment:0/position", position_circ);

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

    public void onRadioButtonClicked(View view) {
        //Preverimo, če je gumb pritisnjen
        boolean checked = ((RadioButton) view).isChecked();

        //Preverimo, kateri gumb je bil  pritisnjen
        switch(view.getId()) {
            case R.id.standing_radio:
                if (checked)
                    position_circ = "at0037";
                    position_hr = "at1003";
                break;
            case R.id.Reclining_radio:
                if (checked)
                    position_circ="at0038";
                    position_hr="at1001";
                break;
            case R.id.Sitting_radio:
                if (checked)
                    position_circ = "at0039";
                    position_hr = "at0039";
                break;
            case R.id.Lying_radio:
                if (checked)
                    position_circ = "at0040";
                    position_hr = "at1000";
                break;
        }
    }
}
