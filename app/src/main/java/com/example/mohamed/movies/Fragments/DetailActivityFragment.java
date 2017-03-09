package com.example.mohamed.movies.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.movies.Adapters.mReviewsAdapter;
import com.example.mohamed.movies.Adapters.mTrailerAdapter;
import com.example.mohamed.movies.BuildConfig;
import com.example.mohamed.movies.Data.MovieDB;
import com.example.mohamed.movies.Models.Movie;
import com.example.mohamed.movies.Models.Reviews;
import com.example.mohamed.movies.Models.Trailers;
import com.example.mohamed.movies.R;
import com.squareup.picasso.Picasso;

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
public class DetailActivityFragment extends Fragment {

    public Movie movieDetail;
    public ImageView moviePoster;
    public TextView movieTitle, movieOverview, movieRating, movieReleaseDate;
    public Button btnFavourite;

    GridView lvTrailers;
    mTrailerAdapter mTrailerAdapter;
    List<Trailers> mTrailersList = new ArrayList<>();

    ListView lvReviews;
    mReviewsAdapter mReviewsAdapter;
    List<Reviews> mReviewsList = new ArrayList<>();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        moviePoster = (ImageView) rootView.findViewById(R.id.ivMoviePoster);
        movieTitle = (TextView) rootView.findViewById(R.id.tvMovieTitle);
        movieReleaseDate = (TextView) rootView.findViewById(R.id.tvReleaseDate);
        movieRating = (TextView) rootView.findViewById(R.id.tvRating);
        movieOverview = (TextView) rootView.findViewById(R.id.tvOverview);
        btnFavourite = (Button) rootView.findViewById(R.id.btnFavourite);
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDB movieDB = new MovieDB(getActivity());
                Long test = movieDB.insertFavourite(movieDetail.getMovieId(), movieDetail.getImageFullURL(),
                        movieDetail.getOverview(), movieDetail.getReleaseDate(),
                        movieDetail.getTitle(), movieDetail.getVoteAverage());
                if (test != -1) {
                    Toast.makeText(getActivity(), "Added To Favourite", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Already Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent movieIntent = getActivity().getIntent();
        movieDetail = (Movie) movieIntent.getSerializableExtra("MOVIE");

        if (movieDetail == null) {
            movieDetail = (Movie) getArguments().getSerializable("MOVIE");

        }

        if (movieDetail != null) {
            getActivity().setTitle(movieDetail.getTitle());
            Picasso.with(getActivity()).load(movieDetail.getImageFullURL()).into(moviePoster);
            movieTitle.setText(movieDetail.getTitle());
            movieReleaseDate.setText("Release Date : " + movieDetail.getReleaseDate());
            movieRating.setText("Rating : " + movieDetail.getVoteAverage() + "/10");
            movieOverview.setText("          " + movieDetail.getOverview());
        }

        lvTrailers = (GridView) rootView.findViewById(R.id.lvTrailers);
        mTrailerAdapter = new mTrailerAdapter(getActivity(), mTrailersList);
        lvTrailers.setAdapter(mTrailerAdapter);

        lvTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent appIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("vnd.youtube:" + mTrailersList.get(position).getTrailerKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + mTrailersList.get(position).getTrailerKey()));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }

            }
        });

        FetchTrailersData fetchTrailersData = new FetchTrailersData();
        fetchTrailersData.execute(String.valueOf(movieDetail.getMovieId()));

        lvReviews = (ListView) rootView.findViewById(R.id.lvReviews);
        mReviewsAdapter = new mReviewsAdapter(getActivity(), mReviewsList);
        lvReviews.setAdapter(mReviewsAdapter);

        FetchReviewsData fetchReviewsData = new FetchReviewsData();
        fetchReviewsData.execute(String.valueOf(movieDetail.getMovieId()));


        return rootView;
    }

    public class FetchTrailersData extends AsyncTask<String, Void, List<Trailers>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Trailers> doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            String responseJSONStr = null;
            List<Trailers> trailersList = new ArrayList<>();
            String id = params[0];

            try {

                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + id + "/videos?";
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
                    return trailersList;
                }
                responseJSONStr = buffer.toString();


            } catch (Exception e) {
                return trailersList;

            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        return trailersList;
                    }
                }
                try {
                    trailersList = getTrailersDataFromJson(responseJSONStr);
                } catch (Exception e) {
                    return trailersList;
                }

            }

            return trailersList;
        }

        @Override
        protected void onPostExecute(List<Trailers> trailers) {
            super.onPostExecute(trailers);
            mTrailerAdapter.clear();
            for (Trailers trailer : trailers) {
                mTrailerAdapter.add(trailer);
            }
        }

        private List<Trailers> getTrailersDataFromJson(String jsonResponse) throws Exception {
            List<Trailers> trailers = new ArrayList<>();
            final String RESULTS_KEY = "results";
            final String TRAILER_ID = "id";
            final String TRAILER_NAME = "name";
            final String TRAILER_KEY = "key";


            try {
                JSONObject trailerJSON = new JSONObject(jsonResponse);
                if (trailerJSON.has(RESULTS_KEY)) {
                    JSONArray trailersList = trailerJSON.getJSONArray(RESULTS_KEY);
                    if (trailersList.length() > 0) {
                        for (int i = 0; i < trailersList.length(); i++) {
                            JSONObject trailerDetailJSON = (JSONObject) trailersList.get(i);
                            if (trailerDetailJSON.has(TRAILER_ID) && trailerDetailJSON.has(TRAILER_NAME)
                                    && trailerDetailJSON.has(TRAILER_KEY)) {

                                String trailerID = trailerDetailJSON.getString(TRAILER_ID);
                                String trailerName = trailerDetailJSON.getString(TRAILER_NAME);
                                String trailerKey = trailerDetailJSON.getString(TRAILER_KEY);

                                Trailers tr = new Trailers(trailerID, trailerName, trailerKey);
                                trailers.add(tr);
                            }

                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error loading trailers", Toast.LENGTH_SHORT).show();
            }

            return trailers;
        }
    }

    public class FetchReviewsData extends AsyncTask<String, Void, List<Reviews>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Reviews> doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            String responseJSONStr = null;
            List<Reviews> reviewsList = new ArrayList<>();
            String id = params[0];

            try {

                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + id + "/reviews?";
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
                    return reviewsList;
                }
                responseJSONStr = buffer.toString();


            } catch (Exception e) {
                return reviewsList;

            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        return reviewsList;
                    }
                }
                try {
                    reviewsList = getReviewsDataFromJson(responseJSONStr);
                } catch (Exception e) {
                    return reviewsList;
                }

            }

            return reviewsList;
        }

        @Override
        protected void onPostExecute(List<Reviews> reviews) {
            super.onPostExecute(reviews);
            mReviewsAdapter.clear();
            for (Reviews review : reviews) {
                mReviewsAdapter.add(review);
            }
        }

        private List<Reviews> getReviewsDataFromJson(String jsonResponse) throws Exception {
            List<Reviews> reviews = new ArrayList<>();
            final String RESULTS_KEY = "results";
            final String REVIEW_ID = "id";
            final String REVIEW_AUTHOR = "author";
            final String REVIEW_CONTENT = "content";


            try {
                JSONObject reviewJSON = new JSONObject(jsonResponse);
                if (reviewJSON.has(RESULTS_KEY)) {
                    JSONArray reviewsList = reviewJSON.getJSONArray(RESULTS_KEY);
                    if (reviewsList.length() > 0) {
                        for (int i = 0; i < reviewsList.length(); i++) {
                            JSONObject reviewDetailJSON = (JSONObject) reviewsList.get(i);
                            if (reviewDetailJSON.has(REVIEW_ID) && reviewDetailJSON.has(REVIEW_AUTHOR)
                                    && reviewDetailJSON.has(REVIEW_CONTENT)) {

                                String reviewID = reviewDetailJSON.getString(REVIEW_ID);
                                String reviewAuthor = reviewDetailJSON.getString(REVIEW_AUTHOR);
                                String reviewContent = reviewDetailJSON.getString(REVIEW_CONTENT);

                                Reviews re = new Reviews(reviewID, reviewAuthor, reviewContent);
                                reviews.add(re);
                            }

                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error loading reviews", Toast.LENGTH_SHORT).show();
            }

            return reviews;
        }
    }

}
