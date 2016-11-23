package com.android.udacity.kavitha.jokeconsumer;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.jokeproducer.JokeTeller;

import java.io.IOException;

/**
 * Created by Kavitha on 11/2/2016.
 */
public class JokeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        // Setting the textview with the jokes received via Intent extra.
        TextView jokeTextView = (TextView) findViewById(R.id.txtJokes);
        jokeTextView.setText(this.getIntent().getStringExtra("MESSAGE"));
    }
}