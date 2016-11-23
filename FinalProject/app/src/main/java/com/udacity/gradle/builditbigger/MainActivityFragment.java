package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.udacity.kavitha.jokeconsumer.JokeActivity;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Button clickButton;
    private ProgressBar spinner;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        spinner = (ProgressBar)rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        clickButton = ((Button) rootView.findViewById(R.id.tellJoke));
        clickButton.setOnClickListener(new View.OnClickListener() {
           // spinner.setVisibility(View.VISIBLE);
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);

                launchJokeActivity();
            }
        });


      /*  clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                launchJokeActivity();
            }
        });*/
        return rootView;
    }




    public void launchJokeActivity() {

        new AsyncTaskForBackend().execute(new Pair<Context, String>(this.getContext(), getString(R.string.joke_type)));
        spinner.setVisibility(View.GONE);
    }


}
