package com.udacity.gradle.builditbigger.paid;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.udacity.gradle.builditbigger.AsyncTaskForBackend;
import com.udacity.gradle.builditbigger.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaidFragment extends Fragment {

    private Button clickButton;
    private ProgressBar spinner;

    public PaidFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_paid, container, false);
        spinner = (ProgressBar)rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

       /* clickButton.setOnClickListener(new View.OnClickListener() {
            //spinner.setVisibility(View.VISIBLE);
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                launchJokeActivity();
            }
        });
*/

        clickButton = ((Button) rootView.findViewById(R.id.tellJoke));
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                launchJokeActivity();
            }
        });
        return rootView;
    }


    public void launchJokeActivity() {

        new AsyncTaskForBackend().execute(new Pair<Context, String>(this.getContext(), getString(R.string.joke_type)));
        spinner.setVisibility(View.GONE);
    }

}
