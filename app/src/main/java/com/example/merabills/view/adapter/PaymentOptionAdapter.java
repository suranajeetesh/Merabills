package com.example.merabills.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PaymentOptionAdapter extends BaseAdapter implements SpinnerAdapter {
    private final ArrayList<String> mData;

    public PaymentOptionAdapter(ArrayList<String> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(parent.getContext());
        } else {
            textView = (TextView) convertView;
        }
        textView.setText(mData.get(position));
        return textView;
    }
}
