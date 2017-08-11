package com.anshdeep.dailytech.ui.DetailActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.anshdeep.dailytech.R;
import com.anshdeep.dailytech.ui.FavoriteMovieActivity.FavoriteMovieActivity;

/**
 * Created by ANSHDEEP on 20-05-2017.
 */

public class ArticleWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_detail);

            // Create an Intent to launch MainActivity for example when clicking on the title
            Intent intent = new Intent(context, FavoriteMovieActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            rv.setOnClickPendingIntent(R.id.widget, pendingIntent);


            Log.d("ArticleWidgetProvider", "onUpdate called: ");
            // Set up the collection
            setRemoteAdapter(context, rv);


            Intent clickIntentTemplate = new Intent(context, DetailActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.widget_list_new, clickPendingIntentTemplate);
            rv.setEmptyView(R.id.widget_list_new, R.id.widget_empty);


            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_new);
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, rv);

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);


    }

    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list_new,
                new Intent(context, ArticleWidgetRemoteViewsService.class));
    }
}
