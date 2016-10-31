package com.android.udacity.kavitha.popularmoviesstage2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.udacity.kavitha.popularmoviesstage2.R;

/**
 * Created by ashwath on 9/29/2016.
 */

public class FragmentVoid extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.none_movie,container,false);
        return view;
    }
}