package com.example.mohamed.movies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.mohamed.movies.Fragments.DetailActivityFragment;
import com.example.mohamed.movies.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent sentIntent = getIntent();
        Bundle sentBundle = sentIntent.getExtras();
        DetailActivityFragment detailFragment = new DetailActivityFragment();
        detailFragment.setArguments(sentBundle);
        getSupportFragmentManager().beginTransaction().add(R.id.llDetail, detailFragment, "").commit();
    }

}
