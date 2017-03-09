package com.example.mohamed.movies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mohamed.movies.R;
import com.example.mohamed.movies.Models.Trailers;

import java.util.List;

/**
 * Created by Mohamed on 27/11/2016.
 */

public class mTrailerAdapter extends BaseAdapter {

    Context mContext;
    List<Trailers> mTrailersList;
    Trailers mLock = new Trailers();

    public mTrailerAdapter(Context mContext, List<Trailers> mTrailersList) {
        this.mContext = mContext;
        this.mTrailersList = mTrailersList;
    }

    public void add(Trailers item) {
        synchronized (mLock) {
            mTrailersList.add(item);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            mTrailersList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTrailersList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trailers trailers = mTrailersList.get(position);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.trailer_item, parent, false);

        TextView tvTrailer = (TextView) rootView.findViewById(R.id.tvTrailer);
        tvTrailer.setText(trailers.getTrailerName().toString());
        return rootView;
    }
}