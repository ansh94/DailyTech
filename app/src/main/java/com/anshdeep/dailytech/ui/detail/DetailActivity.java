package com.anshdeep.dailytech.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anshdeep.dailytech.R;
import com.anshdeep.dailytech.data.db.AppDbHelper;
import com.anshdeep.dailytech.data.db.DbOpenHelper;
import com.anshdeep.dailytech.data.model.Article;
import com.anshdeep.dailytech.util.NetworkUtils;
import com.bumptech.glide.Glide;
import com.like.LikeButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ANSHDEEP on 18-05-2017.
 */

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_detail) Toolbar mToolbar;

    @BindView(R.id.news_image_detail) ImageView newsImage;

    @BindView(R.id.news_title_detail) TextView newsTitle;

    @BindView(R.id.news_author_detail) TextView newsAuthor;

    @BindView(R.id.news_description_detail) TextView newsDescription;

    @BindView(R.id.btn_open_article) Button btnOpenArticle;

    @BindView(R.id.share_button) ImageButton btnShare;

    @BindView(R.id.root_view) View parentView;


    public static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private String articleUrl;
    private Article article;
    private String source;
    private LikeButton heartButton;

    // Chrome custom tab variables
    CustomTabsIntent customTabsIntent;
    DbOpenHelper dbOpenHelper;
    AppDbHelper appDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        dbOpenHelper = new DbOpenHelper(this, "article");
        appDbHelper = new AppDbHelper(dbOpenHelper);

        Intent intent = getIntent();


        articleUrl = intent.getStringExtra("ArticleUrl");
        if (intent.hasExtra("Favorite")) {
//            source = article.getSource();
        }


        if (intent.hasExtra("Source")) {
            source = intent.getStringExtra("Source");
        }


        heartButton = (LikeButton) findViewById(R.id.heart_button);
        if (articleUrl != null) {

//            article = appDbHelper.returnArticleByUrl(articleUrl);

            if (article == null) {
                article = intent.getParcelableExtra("Article");
            }

//            updateFavoriteButtons();

            //set thumbnail associated with the article
            Glide.with(this)
                    .load(article.getUrlToImage())
                    .error(R.drawable.noimg)
                    .thumbnail(0.1f) // if you pass 0.1f as the parameter, Glide will display the original image reduced to 10% of the size. If the original image has 1000x1000 pixels, the thumbnail will have 100x100 pixels
                    .crossFade() //animation
                    .into(newsImage);

            // set title
            newsTitle.setText(article.getTitle());

            // set author
            newsAuthor.setText(getString(R.string.by) + " " + article.getAuthor());

            // set title
            newsDescription.setText(article.getDescription());

//            setupChromeCustomTabs();

            btnOpenArticle.setText(getString(R.string.read_full_article) + " " + source);
            // button click handling
            btnOpenArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtils.checkConnection(DetailActivity.this)) {
                        // Launch Chrome Custom Tabs on click
                        customTabsIntent.launchUrl(DetailActivity.this, Uri.parse(article.getUrl()));
                    } else {
                        Toast.makeText(DetailActivity.this, R.string.internet_connection_msg, Toast.LENGTH_LONG).show();
                    }


                }
            });

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareArticleUrl();
                }
            });
        }
    }

    private void shareArticleUrl() {
        String mimeType = "text/plain";
        String title = "Share Article";
        String newsTitle = article.getTitle();
        String newsUrl = article.getUrl();

        ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(newsTitle + "\n" + newsUrl)
                .startChooser();
    }

//    public void updateFavoriteButtons() {
//        Log.d("DetailActivity", "update favorite buttons : " + article.getUrl());
//
////        boolean result = appDbHelper.isArticlePresent(article.getUrl());
//
//        Log.d("DetailActivity", "isFavorite: " + result);
//        if (result) {
//            heartButton.setLiked(true);
//        } else {
//            heartButton.setLiked(false);
//        }
//        updateAllWidgets();
//
////        new AsyncTask<Void, Void, Boolean>() {
////
////            @Override
////            protected Boolean doInBackground(Void... params) {
////                return isFavorite();
////            }
////
////            @Override
////            protected void onPostExecute(Boolean isFavorite) {
////                if (isFavorite) {
////                    heartButton.setLiked(true);
////                } else {
////                    heartButton.setLiked(false);
////                }
////                updateAllWidgets();
////
////            }
////        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//
//        heartButton.setOnLikeListener(new OnLikeListener() {
//            @Override
//            public void liked(LikeButton likeButton) {
//                markAsFavorite();
//            }
//
//            @Override
//            public void unLiked(LikeButton likeButton) {
//                removeFromFavorites();
//            }
//        });
//
//
//    }
//
//    private void updateAllWidgets() {
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
//        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ArticleWidgetProvider.class));
//        if (appWidgetIds.length > 0) {
//            new ArticleWidgetProvider().onUpdate(this, appWidgetManager, appWidgetIds);
//        }
//    }
//
////    private boolean isFavorite() {
////        Cursor movieCursor = getContentResolver().query(
////                ArticleContract.ArticleEntry.CONTENT_URI,
////                new String[]{ArticleContract.ArticleEntry.COLUMN_ARTICLE_URL},
////                ArticleContract.ArticleEntry.COLUMN_ARTICLE_URL + " = " + "'" + article.getUrl() + "'",
////                null,
////                null);
////
////        if (movieCursor != null && movieCursor.moveToFirst()) {
////            movieCursor.close();
////            return true;
////        } else {
////            return false;
////        }
////    }
//
//    public void markAsFavorite() {
//
//        boolean result = appDbHelper.isArticlePresent(article.getUrl());
//
//        if (!result) {
////            Article newArticle = new Article();
////            newArticle.setAuthor(article.getAuthor());
////            newArticle.setTitle(article.getTitle());
////            newArticle.setDescription(article.getDescription());
////            newArticle.setUrl(article.getUrl());
////            newArticle.setUrlToImage(article.getUrlToImage());
////            newArticle.setPublishedAt(article.getPublishedAt());
////            newArticle.setSource(source);
//            appDbHelper.insertArticle(article);
//            Log.d("ArticleDao", "Inserted new article, ID: " + article.getId());
//            updateFavoriteButtons();
//            Snackbar.make(parentView, R.string.article_added, Snackbar.LENGTH_LONG).show();
//        }
//
////        new AsyncTask<Void, Void, Void>() {
////
////            @Override
////            protected Void doInBackground(Void... params) {
////                if (!isFavorite()) {
////                    ContentValues articleValues = new ContentValues();
////                    articleValues.put(ArticleContract.ArticleEntry.COLUMN_ARTICLE_AUTHOR,
////                            article.getAuthor());
////                    articleValues.put(ArticleContract.ArticleEntry.COLUMN_ARTICLE_TITLE,
////                            article.getTitle());
////                    articleValues.put(ArticleContract.ArticleEntry.COLUMN_ARTICLE_DESCRIPTION,
////                            article.getDescription());
////                    articleValues.put(ArticleContract.ArticleEntry.COLUMN_ARTICLE_URL,
////                            article.getUrl());
////                    articleValues.put(ArticleContract.ArticleEntry.COLUMN_ARTICLE_IMAGE_URL,
////                            article.getUrlToImage());
////                    articleValues.put(ArticleContract.ArticleEntry.COLUMN_ARTICLE_TIME,
////                            article.getPublishedAt());
////                    articleValues.put(ArticleContract.ArticleEntry.COLUMN_ARTICLE_SOURCE,
////                            source);
////
////                    // Insert the content values via a ContentResolver
////                    Uri uri = getContentResolver().insert(
////                            ArticleContract.ArticleEntry.CONTENT_URI,
////                            articleValues
////                    );
////
////
////                }
////                return null;
////            }
////
////            @Override
////            protected void onPostExecute(Void aVoid) {
////                updateFavoriteButtons();
////                Snackbar.make(parentView, R.string.article_added, Snackbar.LENGTH_LONG).show();
////            }
////        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//    }
//
//
//    public void removeFromFavorites() {
//
//        boolean result = appDbHelper.isArticlePresent(article.getUrl());
//
//        if (result) {
//            appDbHelper.removeArticle(article);
//            updateFavoriteButtons();
//            Snackbar.make(parentView, R.string.article_removed, Snackbar.LENGTH_LONG).show();
//        }
//
//
////        new AsyncTask<Void, Void, Void>() {
////
////            @Override
////            protected Void doInBackground(Void... params) {
////                if (isFavorite()) {
////                    getContentResolver().delete(ArticleContract.ArticleEntry.CONTENT_URI,
////                            ArticleContract.ArticleEntry.COLUMN_ARTICLE_URL + " = " + "'" + article.getUrl() + "'", null);
////
////
////                }
////                return null;
////            }
////
////            @Override
////            protected void onPostExecute(Void aVoid) {
////                updateFavoriteButtons();
////                Snackbar.make(parentView, R.string.article_removed, Snackbar.LENGTH_LONG).show();
////            }
////        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//    }
//
//
//    private void setupChromeCustomTabs() {
//        customTabsIntent = new CustomTabsIntent.Builder()
//                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
//                .setShowTitle(true)
//                .build();
//
//    }
}
