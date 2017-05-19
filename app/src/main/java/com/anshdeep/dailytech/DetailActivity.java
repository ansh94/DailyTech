package com.anshdeep.dailytech;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anshdeep.dailytech.api.model.Article;
import com.bumptech.glide.Glide;

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


    public static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private Article article;
    private String source;

    // Chrome custom tab variables
    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";

    CustomTabsClient mClient;
    CustomTabsSession mCustomTabsSession;
    CustomTabsServiceConnection mCustomTabsServiceConnection;
    CustomTabsIntent customTabsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        //set the toolbar
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        article = intent.getParcelableExtra("Article");
        source = intent.getStringExtra("Source");
        Log.d(LOG_TAG, "article title = " + article.getTitle());
        if (article != null) {


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
            newsAuthor.setText("By " + article.getAuthor());

            // set title
            newsDescription.setText(article.getDescription());

            setupChromeCustomTabs();

            btnOpenArticle.setText("Read full article on " + source);
            // button click handling
            btnOpenArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(DetailActivity.this, "Button pressed", Toast.LENGTH_SHORT).show();
                    // Launch Chrome Custom Tabs on click
                    customTabsIntent.launchUrl(DetailActivity.this, Uri.parse(article.getUrl()));
                }
            });
        }
    }

    private void setupChromeCustomTabs() {
        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {

                //Pre-warming
                mClient = customTabsClient;
                mClient.warmup(0L);
                //Initialize a session as soon as possible.
                mCustomTabsSession = mClient.newSession(null);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient = null;
            }
        };

        CustomTabsClient.bindCustomTabsService(DetailActivity.this, CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);

        customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setShowTitle(true)
                .build();

    }


}
