package afcc.taavi.kase.afcc;

import afcc.taavi.kase.afcc.Database.PreviousResults;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Taavi on 27.09.2014.
 *
 * Layer between Provider, Settings table and UI
 */
public class PreviousResultsProvider extends Provider  {
    /**
     * Constructor
     *
     * @param context Application context
     * @param helper  Database helper
     * @param matcher URI matcher
     */
    public PreviousResultsProvider(Context context, Database.DatabaseHelper helper, UriMatcher matcher) {
        super(context, helper, matcher);
    }

    /**
     * Returns content type
     *
     * @param uri Path to Settings table
     * @return ContentType of Settings table
     */
    @Override
    public String getType(Uri uri) {
        int uriType = PreviousResults.URIMatcher.match(uri);
        switch (uriType) {
            case PreviousResults.RESULTS:
                return PreviousResults.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    /**
     * Deletes records from database
     *
     * @param uri Path to Settings table
     * @param selection Column names to select
     * @param selectionArgs Arguments for selection
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Inserts data into settings table
     *
     * @param uri Path to Settings table
     * @param values Values to insert into Settings table
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) throws Exception {
        try {
            int uriType = PreviousResults.URIMatcher.match(uri);

            if (uriType != PreviousResults.RESULTS) {
                throw new IllegalArgumentException("Unknown URI");
            }

            SQLiteDatabase db = this.mHelper.getMyWritableDatabase();
            long id = db.insertOrThrow(PreviousResults.TABLE_NAME, null, values);
            return Uri.withAppendedPath(uri, "" + id);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Queries data from database
     *
     * @param uri Path to Settings table
     * @param projection Column names
     * @param selection Column names to select
     * @param selectionArgs Arguments for selection
     * @param sortOrder The order to sort the result by
     * @return Cursor object with resulting data
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        if (projection == null || projection.length == 0) {
            projection = new String[]{PreviousResults._ID, PreviousResults.COL_RESULT,
                    PreviousResults.COL_UNIT, PreviousResults.COL_DATE};
        }

        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = PreviousResults.DEFAULT_SORT_ORDER;
        }

        SQLiteDatabase db = this.mHelper.getMyWritableDatabase();
        Cursor cursor = db.query(PreviousResults.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);

        cursor.setNotificationUri(this.mContext.getContentResolver(), uri);
        return cursor;
    }

    /**
     * Updates Settings table
     *
     * @param uri Path to Settings table
     * @param values Values to insert into Settings table
     * @param selection Column names to select
     * @param selectionArgs Arguments for selection
     * @return Updated row count
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch(PreviousResults.URIMatcher.match(uri)) {
            case PreviousResults.RESULTS:
                SQLiteDatabase db = this.mHelper.getMyWritableDatabase();
                Cursor cursor = db.query(PreviousResults.TABLE_NAME,
                        new String[] {PreviousResults._ID}, null, null, null, null, null);

                if(cursor.getCount() > 0) {
                    return db.update(PreviousResults.TABLE_NAME, values, selection, selectionArgs);
                } else {
                    try {
                        insert(uri, values);
                        return 1;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            default:
                throw new IllegalArgumentException("Update works only for specific history");
        }
    }
}