package com.udacity.gradle.builditbigger.free;


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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.udacity.gradle.builditbigger.AsyncTaskForBackend;
import com.udacity.gradle.builditbigger.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FreeFragment extends Fragment {
    InterstitialAd mInterstitialAd;
    private Button clickButton;
    private ProgressBar spinner;

    public FreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_free, container, false);
        View root = inflater.inflate(R.layout.fragment_free, container, false);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this.getContext(), "ca-app-pub-3940256099942544~3347511713");
        spinner = (ProgressBar)root.findViewById(R.id.progressBar);

        spinner.setVisibility(View.GONE);

        mInterstitialAd = new InterstitialAd(this.getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                System.out.println("closed");
                launchJokeActivity();
            }
        });

        clickButton = ((Button) root.findViewById(R.id.tellJoke));
        clickButton.setOnClickListener(new View.OnClickListener() {
           // spinner.setVisibility(View.VISIBLE);
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                requestNewInterstitial();
                launchJokeActivity();
            }
        });

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        return root;
    }



    private void requestNewInterstitial() {
        System.out.println("inside request loading thei nterstitial ad");
        /*AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        .build();*/

        AdRequest adRequest = new AdRequest.Builder().build();

        mInterstitialAd.loadAd(adRequest);
        System.out.println("inside request after loading thei nterstitial ad");
    }

    public void launchJokeActivity() {

        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            System.out.println("inside is loaded");
            mInterstitialAd.show();
        }else {
            new AsyncTaskForBackend().execute(new Pair<Context,String>(this.getContext(),getString(R.string.joke_type)));
            spinner.setVisibility(View.GONE);
        }
    }




}
