package com.babajisoft.sanketc.helper;

/**
 * Created by s5 on 9/9/16.
 */

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

import com.google.android.gms.analytics.ExceptionParser;
import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class GoogleAnalyticsTracker {

    private static Tracker mTracker;
    private static GoogleAnalytics mGa;
    private Context mContext;

    public GoogleAnalyticsTracker(Context context, int resource) {
        mContext = context;
        mGa = GoogleAnalytics.getInstance(context);
        mTracker = getTracker(resource);

        Thread.setDefaultUncaughtExceptionHandler(new AnalyticsExceptionReporter(mTracker,
                Thread.getDefaultUncaughtExceptionHandler(), context));
    }

    synchronized Tracker getTracker(int xmlResource) {
        return mGa.newTracker(xmlResource);
    }

    public void sendScreenLabel(String screenLabel) {
        mTracker.setScreenName(screenLabel);
        mTracker.send(new HitBuilders.AppViewBuilder().build());
    }

    public void sendCustomDimension(int index, String value) {
        mTracker.send(new HitBuilders.AppViewBuilder().setCustomDimension(index, value).build());
    }

    private class AnalyticsExceptionReporter extends ExceptionReporter {

        public AnalyticsExceptionReporter(Tracker tracker, UncaughtExceptionHandler originalHandler, Context context) {
            super(tracker, originalHandler, context);
            setExceptionParser(new AnalyticsExceptionParser());
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            String exceptionDescription =  getExceptionParser().getDescription(t.getName(), e);

            //Add code to store the exception stack trace in shared preferences

            super.uncaughtException(t, e);
        }
    }

    private class AnalyticsExceptionParser implements ExceptionParser {

        @Override
        public String getDescription(String arg0, Throwable arg1) {
            StringBuilder exceptionFirsLine = new StringBuilder();
            for (StackTraceElement element : arg1.getStackTrace()) {
                exceptionFirsLine.append(element.toString());
                break;
            }

            //150 Bytes is the maximum allowed by Analytics for custom dimensions values. Assumed that 1 Byte = 1 Character (UTF-8)
            String exceptionDescription = exceptionFirsLine.toString();
            if(exceptionDescription.length() > 150)
                exceptionDescription = exceptionDescription.substring(0, 149);

            return exceptionDescription;
        }
    }
}