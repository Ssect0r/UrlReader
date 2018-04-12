package com.example.sect0r.urlreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainScreen extends AppCompatActivity {
    TextView textView;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    RequestQueue requestQueue;
    int choice=0;
    String instrumentName;
    int instrumentVersion;
    String displayPeriod;
    String epic;
    String exchangeId;
    double displayOffer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        textView = (TextView) findViewById(R.id.textView);
        spinner = (Spinner) findViewById(R.id.spinnerCountry);
        adapter = ArrayAdapter.createFromResource(this, R.array.country, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(MainScreen.this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choice = i;


                switch(choice) {
                    case 0:
                        textView.setText("");

                        jsonParsing("https://api.ig.com/deal/samples/markets/ANDROID_PHONE/en_GB/igi");
                        break;
                    case 1:
                        textView.setText("");

                        jsonParsing("https://api.ig.com/deal/samples/markets/ANDROID_PHONE/fr_FR/frm");
                        break;
                    case 2:
                        textView.setText("");

                        jsonParsing("https://api.ig.com/deal/samples/markets/ANDROID_PHONE/de_DE/dem");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                textView.setText("");
                choice = 0;

            }
        });

    }


    private void jsonParsing(String url)
    {
        requestQueue.getCache().clear();
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                List<JSONObject> arrayMarket = new ArrayList<JSONObject>();
                try {
                    JSONArray jsonArray = response.getJSONArray("markets");
                    for(int i = 0; i<jsonArray.length();i++) {

                        JSONObject market = jsonArray.getJSONObject(i);
                         instrumentName = market.getString("instrumentName");
                         instrumentVersion = market.getInt("instrumentVersion");
                         displayPeriod = market.getString("displayPeriod");
                         epic = market.getString("epic");
                         exchangeId = market.getString("exchangeId");
                         displayOffer = market.getDouble("displayOffer");

                        arrayMarket.add(market);


                        Collections.sort(arrayMarket, new Comparator<JSONObject>() {
                            @Override
                            public int compare(JSONObject jsonObject, JSONObject t1) {
                                int compare = 0;
                                try {
                                    String A = jsonObject.getString("instrumentName");
                                    String B = t1.getString("instrumentName");
                                    compare = A.compareTo(B);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return compare;
                            }
                        });
                    }
                    for (JSONObject obj:arrayMarket) {
                            textView.append(obj.getString("instrumentName") + "," +obj.getString("instrumentVersion") + "," + obj.getString("displayPeriod") + ","
                                    + obj.getString("epic") + "," + obj.getString("exchangeId") + ","
                                    + obj.getString("displayOffer") + "\n\n");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        request.setShouldCache(false);
        requestQueue.add(request);


    }

}