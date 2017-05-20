package com.anshdeep.dailytech.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {


    // convert date to format as 5 hours ago
    public static String manipulateDateFormat(String post_date) {

        if (post_date == null)
            return "";       //if no date is returned by the API then set corresponding date view to empty

        if (post_date.equals("0001-01-01T00:00:00Z")) //because Times of India is always returning this in time stamp which is Jan1,1 (wrong information they are sending)
            return "";

        SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date date = null;
        try {
            date = existingUTCFormat.parse(post_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            // Converting timestamp into x ago format
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(String.valueOf(date.getTime())),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            return timeAgo + "";
        } else {
            return post_date;
        }
    }

    public static boolean checkConnection(Context context) {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }

    }
}

