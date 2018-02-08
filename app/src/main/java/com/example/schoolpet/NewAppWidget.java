package com.example.schoolpet;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    static int number=1;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setImageViewResource(R.id.imageView_widget, R.mipmap.study1);
        if(number==1){
            RemoteViews views1 = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1.setImageViewResource(R.id.imageView_widget, R.mipmap.study1);
            number=0;
        }else {
            RemoteViews views2 = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views2.setImageViewResource(R.id.imageView_widget, R.mipmap.study1);
            number=1;
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        if(number==1){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views.setImageViewResource(R.id.imageView_widget, R.mipmap.study1);
            number=0;
        }else {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views.setImageViewResource(R.id.imageView_widget, R.mipmap.study1);
            number=1;
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

