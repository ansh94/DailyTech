package com.anshdeep.dailytech.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ANSHDEEP on 21-03-2017.
 */

public class ArticleContract {

    /* Add content provider constants to the Contract
     Clients need to know how to access the article data, and it's your job to provide
     these content URI's for the path to that data:
        1) Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the TaskEntry class
      */

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.anshdeep.dailytech";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "movies" directory
    public static final String PATH_ARTICLES = "articles";


    /* ArticleEntry is an inner class that defines the contents of the task table */
    public static final class ArticleEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICLES).build();


        // Task table and column names
        public static final String TABLE_NAME = "articles";

        // Since ArticleEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_ARTICLE_AUTHOR = "author";
        public static final String COLUMN_ARTICLE_TITLE = "title";
        public static final String COLUMN_ARTICLE_DESCRIPTION = "description";
        public static final String COLUMN_ARTICLE_URL = "url";
        public static final String COLUMN_ARTICLE_IMAGE_URL = "imageurl";
        public static final String COLUMN_ARTICLE_TIME = "published";
        public static final String COLUMN_ARTICLE_SOURCE = "source";


    }
}
