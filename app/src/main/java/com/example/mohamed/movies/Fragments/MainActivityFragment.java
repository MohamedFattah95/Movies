package com.example.mohamed.movies.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.mohamed.movies.Adapters.mMovieAdapter;
import com.example.mohamed.movies.BuildConfig;
import com.example.mohamed.movies.Data.MovieDB;
import com.example.mohamed.movies.Interfaces.MovieListener;
import com.example.mohamed.movies.Models.Movie;
import com.example.mohamed.movies.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    GridView gvMovies;
    mMovieAdapter mMovieAdapter;
    List<Movie> mMoviesList = new ArrayList<>();

    private MovieListener movieListener;

    public void setMovieListener(MovieListener movieListener) {
        this.movieListener = movieListener;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    public MainActivityFragment() {
        setHasOptionsMenu(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gvMovies = (GridView) rootView.findViewById(R.id.gvMovies);
        mMovieAdapter = new mMovieAdapter(getActivity(), mMoviesList);
        gvMovies.setAdapter(mMovieAdapter);
        gvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMoviesList.get(position);
                movieListener.setSelectedMovie(movie);
            }
        });

        updateMovies();

        return rootView;
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateMovies() {
        if (isOnline()) {
            FetchMoviesData fetchMoviesData = new FetchMoviesData();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String Sorting = prefs.getString(getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_popular));
            fetchMoviesData.execute(Sorting);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public class FetchMoviesData extends AsyncTask<String, Void, List<Movie>> {

        MovieDB movieDB;
        List<Movie> moviesList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            if (params[0] == "favourite") {
                moviesList = fetchFavourite();
                return moviesList;
            } else {
                HttpURLConnection httpURLConnection = null;
                BufferedReader bufferedReader = null;

                String responseJSONStr = null;
                String sort_order = params[0];

                try {

                    final String BASE_URL = "http://api.themoviedb.org/3/movie/" + sort_order;
                    final String API_KEY_PARAM = "api_key";

                    Uri uri = Uri.parse(BASE_URL).buildUpon().
                            appendQueryParameter(API_KEY_PARAM, BuildConfig.MY_MOVIE_DB_API_KEY).build();

                    String requestURL = uri.toString();
                    URL url = new URL(requestURL);


                    // Create the request to THE MOVIE DB, and open the connection
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }
                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return moviesList;
                    }
                    responseJSONStr = buffer.toString();

                } catch (Exception e) {
                    return moviesList;

                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (final IOException e) {
                            return moviesList;
                        }
                    }
                    try {
                        moviesList = getMovieDataFromJson(responseJSONStr);
                    } catch (Exception e) {
                        return moviesList;
                    }

                }
                return moviesList;
            }
        }

        private List<Movie> fetchFavourite() {
            Cursor cursor = movieDB.getMovieData();

            List<Movie> allMovies = new ArrayList<>();

            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    Movie movie = new Movie();

                    movie.setMovieId(cursor.getInt(cursor.getColumnIndex("id")));
                    movie.setImageURL(cursor.getString(cursor.getColumnIndex("poster_path")));
                    movie.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    movie.setVoteAverage(cursor.getString(cursor.getColumnIndex("vote_average")));
                    movie.setReleaseDate(cursor.getString(cursor.getColumnIndex("release_date")));
                    movie.setOverview(cursor.getString(cursor.getColumnIndex("overview")));
                    allMovies.add(movie);
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return allMovies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            mMovieAdapter.clear();
            for (Movie movie : movies) {
                mMovieAdapter.add(movie);
            }
        }

        private List<Movie> getMovieDataFromJson(String jsonResponse) throws Exception {
            List<Movie> movies = new ArrayList<>();
            final String RESULTS_KEY = "results";
            final String POSTER_PATH = "poster_path";
            final String MOVIE_ID = "id";
            final String MOVIE_TITLE = "original_title";
            final String MOVIE_OVERVIEW = "overview";
            final String MOVIE_RATING = "vote_average";
            final String MOVIE_RELEASE_DATE = "release_date";


            try {
                JSONObject movieJSON = new JSONObject(jsonResponse);
                if (movieJSON.has(RESULTS_KEY)) {
                    JSONArray movieList = movieJSON.getJSONArray(RESULTS_KEY);
                    if (movieList.length() > 0) {
                        for (int i = 0; i < movieList.length(); i++) {
                            JSONObject movieDetailJSON = (JSONObject) movieList.get(i);
                            if (movieDetailJSON.has(POSTER_PATH) && movieDetailJSON.has(MOVIE_ID)
                                    && movieDetailJSON.has(MOVIE_TITLE) && movieDetailJSON.has(MOVIE_RELEASE_DATE)
                                    && movieDetailJSON.has(MOVIE_RATING) && movieDetailJSON.has(MOVIE_OVERVIEW)) {

                                String posterPath = movieDetailJSON.getString(POSTER_PATH);
                                String movieTitle = movieDetailJSON.getString(MOVIE_TITLE);
                                String movieDate = movieDetailJSON.getString(MOVIE_RELEASE_DATE);
                                String movieRate = movieDetailJSON.getString(MOVIE_RATING);
                                String movieOver = movieDetailJSON.getString(MOVIE_OVERVIEW);
                                int movieID = Integer.parseInt(movieDetailJSON.getString(MOVIE_ID));

                                Movie movie = new Movie(movieID, posterPath, movieTitle, movieDate, movieRate, movieOver);
                                movies.add(movie);
                            }

                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error loading movies", Toast.LENGTH_SHORT).show();
            }
            return movies;
        }
    }
}

