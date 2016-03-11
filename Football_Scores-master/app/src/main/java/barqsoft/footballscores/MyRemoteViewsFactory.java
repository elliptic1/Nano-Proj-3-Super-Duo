package barqsoft.footballscores;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by todd on 2/22/16.
 */
public class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;
    private int mAppWidgetId;

    public MyRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        // Since we reload the cursor in onDataSetChanged() which gets called immediately after
        // onCreate(), we do nothing here.
    }

    @Override
    public void onDataSetChanged() {
        // Refresh the cursor
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = mContext.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null, null,
                null, null);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // Get the data for this position from the content provider
        String home = "a1";
        String away = "a2";
        if (mCursor.moveToPosition(position)) {
            final int homeColIndex = mCursor.getColumnIndex(DatabaseContract.scores_table.HOME_COL);
            final int awayColIndex = mCursor.getColumnIndex(DatabaseContract.scores_table.AWAY_COL);
            home = mCursor.getString(homeColIndex);
            away = mCursor.getString(awayColIndex);
        }
        // Return a proper item with the proper day and temperature
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget);
        rv.setTextViewText(R.id.widget_item_id, away+"-"+home);
        // Set the click intent so that we can handle it and show a toast message
        final Intent fillInIntent = new Intent(mContext, MainActivity.class);
//        final Bundle extras = new Bundle();
//        extras.putString(WeatherWidgetProvider.EXTRA_DAY_ID, home);
//        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_item_id, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
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
