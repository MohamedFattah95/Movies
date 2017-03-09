package com.example.mohamed.movies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.mohamed.movies.Models.Movie;
import com.example.mohamed.movies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohamed on 01/11/2016.
 */

public class mMovieAdapter extends BaseAdapter {

    Context mContext;
    List<Movie> mMoviesList;
    Movie mLock = new Movie();

    public mMovieAdapter(Context mContext, List<Movie> mMoviesList) {
        this.mContext = mContext;
        this.mMoviesList = mMoviesList;
    }

    public void add(Movie item) {
        synchronized (mLock) {
            mMoviesList.add(item);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            mMoviesList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMoviesList.size();
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
        Movie movie = mMoviesList.get(position);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.movie_item, parent, false);

        ImageView ivMovie = (ImageView) rootView.findViewById(R.id.ivMovie);
        Picasso.with(mContext).load(movie.getImageFullURL()).into(ivMovie);

        return rootView;
    }
}
