package com.example.mohamed.movies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mohamed.movies.Fragments.DetailActivityFragment;
import com.example.mohamed.movies.Fragments.MainActivityFragment;
import com.example.mohamed.movies.Models.Movie;
import com.example.mohamed.movies.Interfaces.MovieListener;
import com.example.mohamed.movies.R;

public class MainActivity extends AppCompatActivity implements MovieListener {

    boolean mIsTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MainActivityFragment mainActivityFragment = new MainActivityFragment();
        mainActivityFragment.setMovieListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.llMain,mainActivityFragment,"").commit();

        if (null != findViewById(R.id.llDetail)) {
            mIsTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return true;
        }
    }

    @Override
    public void setSelectedMovie(Movie movie) {

        if (!mIsTwoPane) {
            Intent detailActivity = new Intent(this, DetailActivity.class);
            detailActivity.putExtra("MOVIE", movie);
            startActivity(detailActivity);
        } else {
            DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MOVIE", movie);
            detailActivityFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.llDetail, detailActivityFragment,"").commit();
        }

    }
}
