package com.anshdeep.dailytech.ui.ArticlesActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anshdeep.dailytech.BuildConfig;
import com.anshdeep.dailytech.DailyTechApp;
import com.anshdeep.dailytech.R;
import com.anshdeep.dailytech.api.model.Article;
import com.anshdeep.dailytech.ui.DetailActivity.DetailActivity;
import com.anshdeep.dailytech.ui.FavoriteMovieActivity.FavoriteMovieActivity;
import com.anshdeep.dailytech.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView,SwipeRefreshLayout.OnRefreshListener, ArticlesAdapter.ArticlesAdapterOnClickHandler{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.subtitle) TextView toolbarSubtitle;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.swipeRefresh) SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.appBar) AppBarLayout appBar;

    @Inject
    MainPresenter presenter;

    ArticlesAdapter adapter;
    LinearLayoutManager mLinearLayoutManager;


    private static final String EXTRA_ARTICLES = "EXTRA_ARTICLES";


    String apiKey = BuildConfig.NEWS_API_KEY;
    String retrofitUrlSourceName;
    List<Article> listOfArticles = new ArrayList<>();

    private ActionBarDrawerToggle drawerToggle;

    boolean flag = true; //flag to hide progress bar when swipe refresh is triggered

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((DailyTechApp) getApplication()).getAppComponent().inject(MainActivity.this);

        ButterKnife.bind(this);

        presenter.setView(this);

        // Adding toolbar to main screen
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        // Tie DrawerLayout events to the ActionBarToggle
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();  //IMPORTANT: without this hamburger icon will not come


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
            hideLoading();
            swipeToRefresh(false);
            flag = true; //restore flag to old state(i.e. true)
        }

    }

    private void performLoading() {
        if (flag) {
            showLoading();
        }

        if (NetworkUtils.checkConnection(this)) {
            presenter.getArticles(retrofitUrlSourceName, apiKey);
            flag = true;
        } else if (!NetworkUtils.checkConnection(this)) {
            showErrorMessage("Please check your internet connection!");
            hideLoading();
            swipeToRefresh(false);
        } else {
            hideLoading();
            swipeToRefresh(false);
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
    }


    private void performDrawerItemAction(String sourceName, String urlSourceName) {

        if (sourceName == null) {
            toolbarSubtitle.setText(" " + R.string.techcrunch);
            retrofitUrlSourceName = "techcrunch";
        } else if (!toolbarSubtitle.getText().equals(" " + sourceName)) {
            toolbarSubtitle.setText(" " + sourceName);

            SharedPreferences.Editor sharedPref = this.getPreferences(Context.MODE_PRIVATE).edit();
            sharedPref.putString("SUBTITLE", sourceName);
            sharedPref.putString("URL_SOURCE_NAME", urlSourceName);
            sharedPref.commit();

            retrofitUrlSourceName = urlSourceName;
            performLoading();

        }

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

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void swipeToRefresh(boolean refreshingStatus) {
        mSwipeRefresh.setRefreshing(refreshingStatus);
    }

    @Override
    public void showArticles(List<Article> articleList) {
        adapter.setDataAdapter(articleList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}