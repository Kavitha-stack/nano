package com.sam_chordas.android.stockhawk.rest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.Stock;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Kavitha on 10/25/2016.
 */

public class ListViewAdapter extends BaseAdapter {

    public ArrayList<Stock> stockList;
    Activity activity;

    public ListViewAdapter(Activity activity, ArrayList<Stock> list) {
        super();
        ButterKnife.inject(activity);
        this.activity = activity;

        this.stockList = list;
    }

    @Override
    public int getCount() {
        return stockList.size();
    }

    @Override
    public Stock getItem(int position) {
        return stockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        @InjectView(R.id.date)
        TextView date;
        @InjectView(R.id.open)
        TextView open;
        @InjectView(R.id.low)
        TextView low;
        @InjectView(R.id.high)
        TextView high;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.list_item, parent, false);
        holder = new ViewHolder(view);

        Stock s = getItem(position);
        holder.date.setText(s.getDateString());
        holder.date.setContentDescription(s.getDateString());

        holder.low.setText(String.valueOf(s.getLow()));
        holder.low.setContentDescription(String.valueOf(s.getLow()));

        holder.high.setText(String.valueOf(s.getHigh()));
        holder.high.setContentDescription(String.valueOf(s.getHigh()));

        holder.open.setText(String.valueOf(s.getOpen()));
        holder.open.setContentDescription(String.valueOf(s.getOpen()));
        return view;
    }
}