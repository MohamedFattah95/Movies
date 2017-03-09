package com.example.mohamed.movies.Data;

import android.provider.BaseColumns;

/**
 * Created by Mohamed on 30/11/2016.
 */

public class MovieDBSchema {

    public static final class Favourite implements BaseColumns {

        public static final String TABLE_NAME = "favourite";
        public static final String ID = "id";
        public static final String POSTER_PATH = "poster_path";
        public static final String TITLE = "title";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String RELEASE_DATE = "release_date";
        public static final String OVERVIEW = "overview";

    }
}
