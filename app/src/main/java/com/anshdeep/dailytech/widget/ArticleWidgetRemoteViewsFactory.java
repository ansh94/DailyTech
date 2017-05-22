package com.anshdeep.dailytech.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.anshdeep.dailytech.R;
import com.anshdeep.dailytech.data.ArticleContract;

/**
 * Created by ANSHDEEP on 20-05-2017.
 */

public class ArticleWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext = null;
    private int mAppWidgetId;
    private Cursor data;

    public ArticleWidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }


    @Override
    public void onCreate() {
        //nothing to do
    }

    @Override
    public void onDataSetChanged() {
        if (data != null) {
            data.close();
        }

        // This method is called by the app hosting the widget (e.g., the launcher)
        // However, our ContentProvider is not exported so it doesn't have access to the
        // data. Therefore we need to clear (and finally restore) the calling identity so
        // that calls use our process and permission

        final long identityToken = Binder.clearCallingIdentity();

        data = mContext.getContentResolver().query(ArticleContract.ArticleEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (data != null) {
            data.close();
            data = null;
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                data == null || !data.moveToPosition(position)) {
            return null;
        }

        RemoteViews views = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_detail_list_item);


        int articleTitleIndex = data.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_ARTICLE_TITLE);
        String articleTitle = data.getString(articleTitleIndex);
        views.setTextViewText(R.id.news_widget_title, articleTitle);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.widget_detail_list_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
