package com.example.vid.record;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class second extends AppCompatActivity {

    String weight;
    String body_water;
    String heart_rate;
    String ankle_circ;
    String position_circ;
    String position_hr;

    String sessionId;


    ArrayList<String> seznam = new ArrayList<String>();

    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle extras = getIntent().getExtras();
        String[] arrayB = extras.getStringArray("meritve");

        weight = arrayB[0];
        body_water = arrayB[1];
        heart_rate = arrayB[2];
        ankle_circ = arrayB[3];

        test = (TextView) findViewById(R.id.textView);

        //hardcode test

    }

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

    public void record2(View v) {
        test.setText(position_circ);

        String patientIdEhr = "50bc1c98-8540-4d86-bbc7-813f7c55377e";
        String patientIdEhr2 = "1229d4bc-2e95-4366-a555-3ce4e8655fd9";
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://rest.ehrscape.com/rest/v1/session?username=medrockweek1&password=medrockweek1";
        String url2 = "https://rest.ehrscape.com/rest/v1/composition?ehrId=" + patientIdEhr +

                "&templateId=Heart%20Failure%20Detection&format=FLAT&committer=";

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
        params.put("encounter/body_weight:0/any_event:0/weight|magnitude", weight);
        params.put("encounter/body_weight:0/any_event:0/weight|unit", "kg");
        params.put("encounter/pulse_heart_beat:0/any_event:0/position", position_hr);
        params.put("encounter/body_water:0/total_water_percentage|magnitude", body_water);
        params.put("encounter/body_water:0/total_water_percentage|unit", "%");
        params.put("encounter/pulse_heart_beat:0/any_event:0/heart_rate|magnitude", heart_rate);//
        params.put("encounter/pulse_heart_beat:0/any_event:0/heart_rate|unit", "/min");
        params.put("encounter/measurement_of_body_segment:0/ankle_circumference:0|magnitude", ankle_circ);
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
                        test.setText("Prišlo je do napake med prenosom podatkov!1");
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

        String url3 = "https://rest.ehrscape.com/rest/v1/session?username=medrockweek1&password=medrockweek1";
        String query = "select%20%20%20%20%20a_a/data%5Bat0002%5D/events%5Bat0003%5D/data%5Bat0001%5D/items%5Bat0004%5D/value%20as%20Weight,%20%20%20%20%20a/context/start_time,%20%20%20%20%20e/ehr_id/value,%20%20%20%20%20a_b/data%5Bat0001%5D/events%5Bat0002%5D/data%5Bat0003%5D/items%5Bat0004%5D/value%20as%20Total_water_percentage%20from%20EHR%20e%20contains%20COMPOSITION%20a%20contains%20(%20%20%20%20%20OBSERVATION%20a_a%5BopenEHR-EHR-OBSERVATION.body_weight.v1%5D%20and%20%20%20%20%20OBSERVATION%20a_b%5BopenEHR-EHR-OBSERVATION.body_water.v0%5D)%20where%20%20%20%20%20e/ehr_id/value%3D'"+patientIdEhr+"'%20and%20%20%20%20%20a/context/start_time%3E'"+datum()+"%2B02:00'%20offset%200%20limit%20100";
        String url4 ="https://rest.ehrscape.com/rest/v1/query/?aql="+query;

        //request
        //PRVI REQUEST. V URL-ju pošljemo geslo in username, kot odgovor pa dobimo SessionID.
        //Head in Body requesta sta prazna
        JsonObjectRequest jsObjRequest3 = new JsonObjectRequest
                (Request.Method.POST, url3, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //opstring se uporabi, ker v primeru napacnega keya vrne null string.
                        sessionId = response.optString("sessionId", null);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        test.setText("Prišlo je do napake med identifikacijo na strežniku!1");
                    }
                });

        //Dodamo request v queue
        queue.add(jsObjRequest3);

        Map<String,String> params2 = new HashMap<String, String>();
        //params.put("aql", "select a from EHR e contains COMPOSITION a[openEHR-EHR-COMPOSITION.encounter.v1] where a/name/value='Encounter' offset 0 limit 100");

        //DRUGI REQUEST. V glavi pošljemo sessionID, v body-ju pa podatke za vpis. Oboje je v formatu JSON
        JsonObjectRequest jsObjRequest4 = new JsonObjectRequest
                (Request.Method.GET, url4, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //test.setText("Vpis je bil uspešno poslan!");
                        try {

                            String resultSet = response.getString("resultSet");
                            JSONArray resultSetJSON = new JSONArray(resultSet);
                            String test1 = "";
                            for(int i = 0; i<resultSetJSON.length(); i++) {
                                JSONObject weightJSON = resultSetJSON.getJSONObject(i);
                                String weight = weightJSON.getString("Weight");
                                JSONObject weight1JSON = new JSONObject(weight);
                                String weight1 = weight1JSON.getString("magnitude");
                                seznam.add(weight1.toString());
                                test1 = test1 + weight1.toString()+" ";
                            }
                            //test.setText(seznam.toString());

                            Intent i = new Intent(second.this, results.class);
                            i.putStringArrayListExtra("seznam", seznam);
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            test.setText("Napaka pri parsanju");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        test.setText("Prišlo je do napake med prenosom podatkov!2");
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
        queue.add(jsObjRequest4);
        //test.setText(datum());
        System.out.println("aa"+datum());


    }

    public String datum() {
        //Spremenljivke za čas
        Calendar c = Calendar.getInstance();


        String minutes=String.format("%02d",c.get(Calendar.MINUTE));
        String hours=String.format("%02d",c.get(Calendar.HOUR_OF_DAY)-1);
        String second=String.format("%02d",c.get(Calendar.SECOND));

        String day=String.format("%02d",c.get(Calendar.DAY_OF_MONTH));
        String month = String.format("%02d",c.get(Calendar.MONTH)+1);
        String year=String.format("%02d",c.get(Calendar.YEAR));

        int ms = c.get(Calendar.MILLISECOND);

        //Sestavimo format časa
        return year+"-"+month+"-"+day+"T"+hours+":"+minutes+":"+second+"."+ms;
    }
}
