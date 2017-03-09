package com.example.mohamed.movies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mohamed.movies.R;
import com.example.mohamed.movies.Models.Reviews;

import java.util.List;

/**
 * Created by Mohamed on 28/11/2016.
 */

public class mReviewsAdapter extends BaseAdapter {

    Context mContext;
    List<Reviews> mReviewsList;
    Reviews mLock = new Reviews();

    public mReviewsAdapter(Context mContext, List<Reviews> mReviewsList) {
        this.mContext = mContext;
        this.mReviewsList = mReviewsList;
    }

    public void add(Reviews item) {
        synchronized (mLock) {
            mReviewsList.add(item);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            mReviewsList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mReviewsList.size();
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
        Reviews reviews = mReviewsList.get(position);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.review_item, parent, false);

        TextView tvAuthor = (TextView) rootView.findViewById(R.id.tvAuthor);
        TextView tvContent = (TextView) rootView.findViewById(R.id.tvContent);

        tvAuthor.setText(reviews.getReviewAuthor().toString() + ":");
        tvContent.setText("          " + reviews.getReviewContent().toString());
        return rootView;
    }
}
