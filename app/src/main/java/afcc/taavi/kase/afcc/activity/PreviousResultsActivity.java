package afcc.taavi.kase.afcc.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import afcc.taavi.kase.afcc.AfccApplication;
import afcc.taavi.kase.afcc.R;
import afcc.taavi.kase.afcc.database.PreviousResultsTable;

/**
 * Created by Taavi Kase
 *
 * This activity shows saved results as a list view
 */
public class PreviousResultsActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "PreviousResultsActivity";
    private static final int RESULTS_LOADER = 0;
    private Tracker mTracker;

    /**
     * Here we will change the data from cursor to more convenient format
     */
    SimpleCursorAdapter.ViewBinder viewBinder = new SimpleCursorAdapter.ViewBinder() {
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            switch (view.getId()) {
                case R.id.nrText:
                    TextView nrText = (TextView) view;
                    int row = cursor.getPosition() + 1;
                    String rowNr = "" + row;
                    nrText.setText(rowNr);
                    break;
                default:
                    return false;
            }

            return true;
        }
    };

    private SimpleCursorAdapter mAdapter;
    private String[] mProjection = {PreviousResultsTable._ID, PreviousResultsTable.COL_ROW,
            PreviousResultsTable.COL_RESULT, PreviousResultsTable.COL_UNIT,
            PreviousResultsTable.COL_DATE};

    /**
     * Called when Activity is first created
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_results);

        AfccApplication application = (AfccApplication) getApplication();
        mTracker = application.getDefaultTracker();

        try {
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(TAG, "No actionbar");
        }

        setPreviousResults();
    }

    /**
     * Called when user selects a menu item
     *
     * @param item Item that was selected
     * @return True if selection was handled, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Queries previous results from database, puts it to ListView
     */
    private void setPreviousResults() {
        int[] to = {R.id.idText, R.id.nrText, R.id.resultText, R.id.unitText, R.id.dateText};
        mAdapter = new SimpleCursorAdapter(this, R.layout.previous_results_list_row, null,
                mProjection, to, RESULTS_LOADER);

        mAdapter.setViewBinder(viewBinder);
        getSupportLoaderManager().restartLoader(RESULTS_LOADER, null, this);

        ListView resultsList = (ListView) findViewById(android.R.id.list);
        resultsList.setAdapter(mAdapter);

        TextView empty = (TextView) findViewById(R.id.empty);
        resultsList.setEmptyView(empty);

        resultsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                return longClick(id);
            }
        });
    }

    /**
     * Called when user long clicks a list item
     *
     * @param id ID of an item that was clicked
     * @return true
     */
    private boolean longClick(final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getResourceString(R.string.qst_delete));
        builder.setPositiveButton(getResourceString(R.string.ans_del), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                onDeleteClicked(id);
            }
        });

        builder.setNegativeButton(getResourceString(R.string.ans_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
        return true;
    }

    /**
     * Called when user clicks on Delete in the alert dialog
     *
     * @param id ID of an item that is to be deleted
     */
    private void onDeleteClicked(long id) {
        Uri uri = PreviousResultsTable.CONTENT_URI;
        String selection = PreviousResultsTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        ContentResolver content = getContentResolver();
        content.delete(uri, selection, selectionArgs);

        getSupportLoaderManager().restartLoader(RESULTS_LOADER, null, this);
        mAdapter.notifyDataSetChanged();

        makeToast(getResourceString(R.string.inf_deleted));
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id     The ID whose loader is to be created.
     * @param bundle Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri contentUri = PreviousResultsTable.CONTENT_URI;

        switch (id) {
            case RESULTS_LOADER:
                return new CursorLoader(this, contentUri, null, null, null, null);
            default:
                return null;
        }
    }

    /**
     * Called when a previously created loader has finished loading.
     * Swap the new cursor for adapter (the framework will take care of closing the
     * old cursor once we return.)
     *
     * @param cursorLoader Current cursorLoader object
     * @param cursor       Holds data from database
     */
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        switch (cursorLoader.getId()) {
            case RESULTS_LOADER:
                mAdapter.swapCursor(cursor);
                break;
        }
    }

    /**
     * Called when activity is resumed
     */
    @Override
    public void onResume() {
        super.onResume();

        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * Called when a previously created loader is reset, making the data unavailable.
     * This is called when the last Cursor provided to onLoadFinished()
     * above is about to be closed.  We need to make sure we are no
     * longer using it.
     *
     * @param cursorLoader Current cursorLoader object.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }
}