package com.anshdeep.dailytech.ui.detail;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by ANSHDEEP on 20-05-2017.
 */

public class ArticleWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        //return remote view factory here
        return new ArticleWidgetRemoteViewsFactory(this, intent);
    }
}
