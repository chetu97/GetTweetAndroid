package com.chaitanya.sanoriya.simpletwitterapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{

    private TextView latestTextView;
    private TextView tweetTextView;
    private EditText usernameEditText;
    private String username;
    private JSONObject jsonObject;
    private final String URL = "http://192.168.1.5:8000/tweet/";
    private String tweet;
    /*private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private SimpleDateFormat simpleDateFormat;
    private Date date;
    private HashMap<String, String> hashMap;*/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latestTextView = (TextView) findViewById(R.id.latestTextView);
        tweetTextView = (TextView) findViewById(R.id.tweetTextView);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        latestTextView.setVisibility(View.INVISIBLE);
        tweetTextView.setVisibility(View.INVISIBLE);
        /*firebaseDatabase = FirebaseDatabase.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");*/
    }

    public void getTweet(View view)
    {
        username = usernameEditText.getText().toString().trim();
        try
        {
            jsonObject = new JSONObject();
            jsonObject.put("username", username);
        } catch (Exception e)
        {
            Singleton.toast(MainActivity.this, e.getMessage());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    Log.v("JSON RESPONSE : ", response.toString());
                    if (response.getString("success").equals("1"))
                    {
                        tweet = response.getString("data");
                        latestTextView.setVisibility(View.VISIBLE);
                        tweetTextView.setVisibility(View.VISIBLE);
                        tweetTextView.setText(tweet);
                        /*date = new Date();
                        databaseReference = firebaseDatabase.getReference(simpleDateFormat.format(date));
                        databaseReference.child("username").setValue(username);
                        databaseReference.child("tweet").setValue(tweet);*/
                    } else if (response.getString("sucess").equals(""))
                    {
                        Singleton.toast(MainActivity.this, "User Not Found");
                    } else
                    {
                        Singleton.toast(MainActivity.this, "Error!");
                    }
                } catch (Exception e)
                {
                    Singleton.toast(MainActivity.this, e.getMessage());
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Singleton.toasterror(MainActivity.this, error);
            }
        });
        Singleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
    }
}
