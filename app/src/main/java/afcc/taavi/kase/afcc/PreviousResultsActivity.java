package afcc.taavi.kase.afcc;

import afcc.taavi.kase.afcc.Database.PreviousResults;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Taavi Kase
 *
 * Main activity
 */
public class PreviousResultsActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int RESULTS_LOADER = 0;
    /**
     * Called when Activity is first created
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_results);

        getLoaderManager().restartLoader(RESULTS_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri contentUri = PreviousResults.CONTENT_URI;

        switch (id) {
            case RESULTS_LOADER:
                return new CursorLoader(this, contentUri, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        switch(cursorLoader.getId()) {
            case RESULTS_LOADER:
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            String id = cursor.getString(cursor.getColumnIndex(PreviousResults._ID));
                            String result = cursor.getString(cursor.getColumnIndex(PreviousResults.COL_RESULT));
                            String date = cursor.getString(cursor.getColumnIndex(PreviousResults.COL_DATE));
                            String unit = cursor.getString(cursor.getColumnIndex(PreviousResults.COL_UNIT));

                            Log.d("PRA", id + ", " + result + ", " + date + ", " + unit);
                        }
                    }
                } else {
                    Log.d("PRA", "For some odd reason, cursor is null");
                }

                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}