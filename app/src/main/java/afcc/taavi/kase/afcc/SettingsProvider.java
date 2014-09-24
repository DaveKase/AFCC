package afcc.taavi.kase.afcc;

import afcc.taavi.kase.afcc.Database.DatabaseHelper;
import afcc.taavi.kase.afcc.Database.Settings;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Taavi Kase on 24.09.2014.
 *
 * Layer between Provider, Settings table and UI
 */
public class SettingsProvider extends Provider {
    /**
     * Constructor
     *
     * @param context Application context
     * @param helper Database helper
     * @param matcher URI matcher
     */
    public SettingsProvider(Context context, DatabaseHelper helper, UriMatcher matcher) {
        super(context, helper, matcher);
    }

    /**
     * Doesn't allow to delete anything from database
     *
     * @param uri Path to Settings table
     * @param selection Column names to select
     * @param selectionArgs Arguments for selection
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns content type
     *
     * @param uri Path to Settings table
     * @return ContentType of Settings table
     */
    @Override
    public String getType(Uri uri) {
        int uriType = Database.Settings.URIMatcher.match(uri);
        switch (uriType) {
            case Settings.SETTINGS:
                return Settings.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
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
            int uriType = Settings.URIMatcher.match(uri);

            if (uriType != Settings.SETTINGS) {
                throw new IllegalArgumentException("Unknown URI");
            }

            SQLiteDatabase db = this.mHelper.getMyWritableDatabase();
            long id = db.insertOrThrow(Settings.TABLE_NAME, null, values);
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
        if (projection == null || projection.length == 0)
            projection = new String[]{Settings._ID, Settings.COL_DISTANCE, Settings.COL_UNIT,
                    Settings.COL_UNIT, Settings.COL_SPEED};

        if (TextUtils.isEmpty(sortOrder))
            sortOrder = Settings.DEFAULT_SORT_ORDER;

        SQLiteDatabase db = this.mHelper.getMyWritableDatabase();
        Cursor cursor = db.query(Settings.TABLE_NAME, projection, selection, selectionArgs,
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
        switch(Settings.URIMatcher.match(uri)) {
            case Settings.SETTINGS:
                SQLiteDatabase db = this.mHelper.getMyWritableDatabase();
                Cursor cursor = db.query(Settings.TABLE_NAME, new String[] {Settings._ID}, null, null, null, null, null);

                if(cursor.getCount() > 0) {
                    return db.update(Settings.TABLE_NAME, values, selection, selectionArgs);
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