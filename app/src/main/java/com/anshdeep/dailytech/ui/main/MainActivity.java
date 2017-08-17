package com.anshdeep.dailytech.ui.main;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.anshdeep.dailytech.BuildConfig;
import com.anshdeep.dailytech.R;
import com.anshdeep.dailytech.data.model.Article;
import com.anshdeep.dailytech.ui.base.BaseActivity;
import com.anshdeep.dailytech.ui.detail.DetailActivity;
import com.anshdeep.dailytech.ui.favorite.FavoriteMovieActivity;
import com.anshdeep.dailytech.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView, SwipeRefreshLayout.OnRefreshListener, ArticlesAdapter.ArticlesAdapterOnClickHandler {


    @Inject
    MainPresenter mPresenter;

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;

    @BindView(R.id.nav_view) NavigationView navigationView;

    @BindView(R.id.subtitle) TextView toolbarSubtitle;

    @BindView(R.id.swipeRefresh) SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @BindView(R.id.appBar) AppBarLayout appBar;

    private ArticlesAdapter adapter;
    private LinearLayoutManager mLinearLayoutManager;
    private static final String EXTRA_ARTICLES = "EXTRA_ARTICLES";
    private String apiKey = BuildConfig.NEWS_API_KEY;
    private List<Article> listOfArticles = new ArrayList<>();
    private ActionBarDrawerToggle drawerToggle;
    private boolean flag = true; //flag to hide progress bar when swipe refresh is triggered

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(MainActivity.this);

        setUp();


        if (savedInstanceState != null) {
            ArrayList<Article> articles = savedInstanceState.getParcelableArrayList(EXTRA_ARTICLES);
            adapter.setDataAdapter(articles);
            hideLoading();
            swipeToRefresh(false);
            flag = true; //restore flag to old state(i.e. true)
        }

    }

    //should move this method in presenter
    private void performLoading() {
        if (flag) {
            showLoading();
        }

        if (NetworkUtils.checkConnection(this)) {
            mPresenter.getArticles(apiKey);
            flag = true;
        } else if (!NetworkUtils.checkConnection(this)) {
            showMessage("Please check your internet connection!");
            hideLoading();
            swipeToRefresh(false);
        } else {
            hideLoading();
            swipeToRefresh(false);
            flag = true; //restore flag to old state(i.e. true)
        }
    }

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


    private void performDrawerItemAction(String subtitle, String urlSourceName) {
        if (subtitle == null) {
            mPresenter.updateSharedPrefs("TechCrunch", "techcrunch");
            updateSubtitle("TechCrunch");
            performLoading();
        } else {

            //to prevent loading when same item is clicked again
            if (!subtitle.equals(mPresenter.getSubtitle())) {
                mPresenter.updateSharedPrefs(subtitle, urlSourceName);
                updateSubtitle(subtitle);
                performLoading();
            }

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

    void setUpNavMenu() {
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

        if (mPresenter.getSubtitle() == null) {
            performDrawerItemAction(null, null);
        } else {
            updateSubtitle(mPresenter.getSubtitle());
            performLoading();
        }
    }


    @Override
    protected void setUp() {
        // Adding toolbar to main screen
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        // Tie DrawerLayout events to the ActionBarToggle
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();  //IMPORTANT: without this hamburger icon will not come

        setUpRecyclerView();
        setUpNavMenu();


        //set onRefreshListener on SwipeRefreshLayout
        mSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefresh.setOnRefreshListener(this);
    }

    private void setUpRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        adapter = new ArticlesAdapter(listOfArticles, this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();

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
    public void updateSubtitle(String subtitle) {
        toolbarSubtitle.setText(" " + subtitle);
    }

}
