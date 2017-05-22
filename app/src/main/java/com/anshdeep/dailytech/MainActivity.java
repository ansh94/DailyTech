package com.anshdeep.dailytech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anshdeep.dailytech.api.model.Article;
import com.anshdeep.dailytech.api.model.NewsResponse;
import com.anshdeep.dailytech.api.service.NewsArrayInterface;
import com.anshdeep.dailytech.ui.ArticlesAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ArticlesAdapter.ArticlesAdapterOnClickHandler {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.subtitle) TextView toolbarSubtitle;
    @BindView(R.id.noData) LinearLayout noData;
    @BindView(R.id.no_Internet) LinearLayout noNet;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.empty_view) View emptyView;
    @BindView(R.id.refresh) ImageView refresh;
    @BindView(R.id.swipeRefresh) SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.appBar) AppBarLayout appBar;

    ArticlesAdapter adapter;
    LinearLayoutManager mLinearLayoutManager;

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final String EXTRA_ARTICLES = "EXTRA_ARTICLES";


    public static final String BASE_URL = "https://newsapi.org/v1/";
    String apiKey = BuildConfig.NEWS_API_KEY;
    String retrofitUrlSourceName;
    List<Article> listOfArticles = new ArrayList<>();

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;

    boolean flag = true; //flag to hide progress bar when swipe refresh is triggered

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        // Adding toolbar to main screen
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        // Tie DrawerLayout events to the ActionBarToggle
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();  //IMPORTANT: without this hamburger icon will not come

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Sets whether analytics collection is enabled for this app on this device.
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);


        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        adapter = new ArticlesAdapter(listOfArticles, this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);


        //set onRefreshListener on SwipeRefreshLayout
        mSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefresh.setOnRefreshListener(this);

        // Setup drawer view
        setupDrawerContent(navigationView);

        if (savedInstanceState != null) {
            ArrayList<Article> articles = savedInstanceState.getParcelableArrayList(EXTRA_ARTICLES);
            adapter.setDataAdapter(articles);

            progressBar.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
            noData.setVisibility(View.INVISIBLE);
            noNet.setVisibility(View.INVISIBLE);
            mSwipeRefresh.setRefreshing(false);
            flag = true; //restore flag to old state(i.e. true)
        }


        //when refresh icon below No Data Found message is clicked
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Setup drawer view
                performLoading();
            }
        });

    }

    private void performLoading() {
        emptyView.setVisibility(View.VISIBLE);
        if (flag)
            progressBar.setVisibility(View.VISIBLE);
        noData.setVisibility(View.INVISIBLE);
        noNet.setVisibility(View.INVISIBLE);

        //check network connection
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            getRetrofitArray(retrofitUrlSourceName, apiKey);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            noData.setVisibility(View.INVISIBLE);
            noNet.setVisibility(View.VISIBLE);
            mSwipeRefresh.setRefreshing(false);
            flag = true; //restore flag to old state(i.e. true)
        }
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Handle navigation view item clicks here.
                        int id = menuItem.getItemId();

                        switch (id) {
                            case R.id.ars_technica:
                                performDrawerItemAction("Ars Technica", "ars-technica");
                                break;
                            case R.id.engadget:
                                performDrawerItemAction("Engadget", "engadget");
                                break;
                            case R.id.hacker_news:
                                performDrawerItemAction("Hacker News", "hacker-news");
                                break;
                            case R.id.recode:
                                performDrawerItemAction("Recode", "recode");
                                break;
                            case R.id.techcrunch:
                                performDrawerItemAction("TechCrunch", "techcrunch");
                                break;
                            case R.id.techradar:
                                performDrawerItemAction("TechRadar", "techradar");
                                break;
                            case R.id.the_next_web:
                                performDrawerItemAction("The Next Web", "the-next-web");
                                break;
                            case R.id.the_verge:
                                performDrawerItemAction("The Verge", "the-verge");
                                break;

                        }

                        // Highlight the selected item has been done by NavigationView
                        menuItem.setChecked(true);

                        // Close the navigation drawer
                        mDrawer.closeDrawers();

                        return true;
                    }
                }
        );

        //read share preference and call
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String restoredSubTitle = sharedPref.getString("SUBTITLE", null);
        String restoredRetrofitUrlSourceName = sharedPref.getString("URL_SOURCE_NAME", null);
        performDrawerItemAction(restoredSubTitle, restoredRetrofitUrlSourceName);
        performLoading();
    }


    private void performDrawerItemAction(String sourceName, String urlSourceName) {

        if (sourceName == null) {
            toolbarSubtitle.setText(" " + R.string.techcrunch);
            retrofitUrlSourceName = "techcrunch";
        } else {


            toolbarSubtitle.setText(" " + sourceName);

            SharedPreferences.Editor sharedPref = this.getPreferences(Context.MODE_PRIVATE).edit();
            sharedPref.putString("SUBTITLE", sourceName);
            sharedPref.putString("URL_SOURCE_NAME", urlSourceName);
            sharedPref.commit();

            retrofitUrlSourceName = urlSourceName;
        }


        performLoading();

    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.
        // ActionBarDrawToggle() does not require it and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        int id = item.getItemId();
        if (id == R.id.action_favorites) {
            Intent favoritesIntent = new Intent(this, FavoriteMovieActivity.class);
            startActivity(favoritesIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        flag = false;
        performLoading();

    }

    void getRetrofitArray(String newsSource, String apiKey) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsArrayInterface serviceRequest = retrofit.create(NewsArrayInterface.class);

        Call<NewsResponse> call = serviceRequest.getArticles(newsSource, apiKey);

        /*
          enqueue() asynchronously sends the request and notifies your app with a callback when a response comes back.
          Since this request is asynchronous, Retrofit handles it on a background thread so that the main UI thread
          isn't blocked or interfered with.
         */
        call.enqueue(new Callback<NewsResponse>() {

            /*
            onResponse(): invoked for a received HTTP response. This method is called for a response that can be correctly
            handled even if the server returns an error message. So if you get a status code of 404 or 500, this method will
            still be called. To get the status code in order for you to handle situations based on them, you can use the
            method response.code().You can also use the isSuccessful() method to find out if the status code is in
            the range 200-300, indicating success.
             */

            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {

                int statusCode = response.code();

                if (statusCode != 200) {
                    emptyView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    noData.setVisibility(View.VISIBLE);
                    noNet.setVisibility(View.INVISIBLE);
                    return;
                }

                appBar.setExpanded(true, true); //unhide app bar when new new source is chosen
                mLinearLayoutManager.scrollToPositionWithOffset(0, 0);//scroll to top of recycler view
                listOfArticles = response.body().getArticles();


                adapter.setDataAdapter(listOfArticles);
                adapter.notifyDataSetChanged();

                progressBar.setVisibility(View.INVISIBLE);
                emptyView.setVisibility(View.INVISIBLE);
                noData.setVisibility(View.INVISIBLE);
                noNet.setVisibility(View.INVISIBLE);
                mSwipeRefresh.setRefreshing(false);
                flag = true; //restore flag to old state(i.e. true)
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }

        });
    }

    @Override
    public void onBackPressed() {  //when back pressed check drawer is open or not
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(Article article) {
        //read share preference and call
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String restoredSubTitle = sharedPref.getString("SUBTITLE", "TechCrunch");
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("Article", article);
        intent.putExtra("Source", restoredSubTitle);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<Article> articles = adapter.getMovies();
        if (articles != null && !articles.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_ARTICLES, articles);
        }

    }

}
