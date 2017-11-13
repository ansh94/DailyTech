package com.anshdeep.dailytech.data;

import com.anshdeep.dailytech.data.db.DbHelper;
import com.anshdeep.dailytech.data.network.ApiHelper;
import com.anshdeep.dailytech.data.prefs.PreferencesHelper;

/**
 * Created by ansh on 25/09/17.
 */

public interface DataManager extends DbHelper, PreferencesHelper,ApiHelper {

    // Add more methods if required
}
