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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mass = (EditText) findViewById(R.id.vnos_teze);

        test = (TextView) findViewById(R.id.test);

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

        String query = "select%20%20%20%20%20a_a/data%5Bat0002%5D/events%5Bat0003%5D/data%5Bat0001%5D/items%5Bat0004%5D/value%20as%20Weight,%20%20%20%20%20a/context/start_time,%20%20%20%20%20e/ehr_id/value,%20%20%20%20%20a_b/data%5Bat0001%5D/events%5Bat0002%5D/data%5Bat0003%5D/items%5Bat0004%5D/value%20as%20Total_water_percentage%20from%20EHR%20e%20contains%20COMPOSITION%20a%20contains%20(%20%20%20%20%20OBSERVATION%20a_a%5BopenEHR-EHR-OBSERVATION.body_weight.v1%5D%20and%20%20%20%20%20OBSERVATION%20a_b%5BopenEHR-EHR-OBSERVATION.body_water.v0%5D)%20where%20%20%20%20%20e/ehr_id/value%3D'50bc1c98-8540-4d86-bbc7-813f7c55377e'%20and%20%20%20%20%20a/context/start_time%3E'2016-07-13T09:57:26.346%2B02:00'%20offset%200%20limit%20100";
        String url2 ="https://rest.ehrscape.com/rest/v1/query/?aql="+query;

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
        //params.put("aql", "select a from EHR e contains COMPOSITION a[openEHR-EHR-COMPOSITION.encounter.v1] where a/name/value='Encounter' offset 0 limit 100");

        //DRUGI REQUEST. V glavi pošljemo sessionID, v body-ju pa podatke za vpis. Oboje je v formatu JSON
        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        test.setText("Vpis je bil uspešno poslan!");
                        try {

                            String resultSet = response.getString("resultSet");
                            JSONArray resultSetJSON = new JSONArray(resultSet);
                            JSONObject weightJSON = resultSetJSON.getJSONObject(3);
                            test.setText("ss");
                            String weight = weightJSON.getString("Weight");
                            JSONObject weight1JSON = new JSONObject(weight);
                            String weight1 = weight1JSON.getString("units");
                            test.setText(weight1.toString());

                            /*for(int i = 0; i<resultSetJSON.length(); i++) {
                                JSONObject meritevJSON = resultSetJSON.getJSONObject(i);
                                String meritev = meritevJSON.getString();
                                JSONObject tezaJSON = meritevJSON.getJSONObject();
                                ArrayList<JSONObject> seznam_meritev = resultSetJSON.getJSONObject(i);
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                            test.setText("Napaka pri parsanju");
                        }
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
