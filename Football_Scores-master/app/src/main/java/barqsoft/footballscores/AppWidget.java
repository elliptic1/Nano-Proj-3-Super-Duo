package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;

import barqsoft.footballscores.service.MyFetchService;

public class AppWidget extends AppWidgetProvider {

    final private String TAG = "AppWidget";

    private String line1;
    private String line2;
    private String line3;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        switch (action) {
            case "barqsoft.footballscores.update.scores":
                line1 = intent.getStringExtra("line1");
                line2 = intent.getStringExtra("line2");
                line3 = intent.getStringExtra("line3");

                Log.d(TAG, "got update request: " + line1 + "/" + line2 + "/" + line3);

        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), AppWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        } else {
            if (appWidgetIds == null) Log.e(TAG, "appWidgetIds is null");
            if (appWidgetIds != null && appWidgetIds.length == 0) Log.e(TAG, "appWidgetIds length is 0");
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(MainActivity.LOG_TAG, "onEnabled");
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        Intent service_start = new Intent(context, MyFetchService.class);
        context.startService(service_start);

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            Log.d(TAG, "Updating widget " + appWidgetId);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget_layout_id, pendingIntent);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            views.setTextViewText(R.id.widget_text_1, line1);
            views.setTextViewText(R.id.widget_text_2, line2);
            views.setTextViewText(R.id.widget_text_3, line3);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}
