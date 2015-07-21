package afcc.taavi.kase.afcc.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Taavi on 22.07.2015.
 *
 * Layer between Provider, Unit table and UI
 */
public class SpeedProvider extends Provider {
    /**
     * Constructor
     *
     * @param context Application context
     * @param helper  Database helper
     * @param matcher URI matcher
     */
    public SpeedProvider(Context context, Database.DatabaseHelper helper, UriMatcher matcher) {
        super(context, helper, matcher);
    }

    /**
     * Returns content type
     *
     * @param uri Path to Speed table
     * @return ContentType of Speed table
     */
    @Override
    public String getType(Uri uri) {
        int uriType = SpeedTable.URIMatcher.match(uri);
        switch (uriType) {
            case SpeedTable.SPEEDS:
                return SpeedTable.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    /**
     * Queries data from database
     *
     * @param uri           Path to Speed table
     * @param projection    Column names
     * @param selection     Column names to select
     * @param selectionArgs Arguments for selection
     * @param sortOrder     The order to sort the result by
     * @return Cursor object with resulting data
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (projection == null || projection.length == 0)
            projection = new String[]{SpeedTable._ID, SpeedTable.COL_DISTANCE_ID, SpeedTable.COL_TEXT};

        if (TextUtils.isEmpty(sortOrder))
            sortOrder = SpeedTable.DEFAULT_SORT_ORDER;

        SQLiteDatabase db = this.mHelper.getMyWritableDatabase();
        Cursor cursor = db.query(SpeedTable.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);

        cursor.setNotificationUri(this.mContext.getContentResolver(), uri);
        return cursor;
    }

    /**
     * Doesn't allow to delete anything from database
     *
     * @param uri           Path to Speed table
     * @param selection     Column names to select
     * @param selectionArgs Arguments for selection
     * @throws UnsupportedOperationException
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    /**
     * Doesn't allow to insert anything into database
     *
     * @param uri    Path to Speed table
     * @param values Values to insert into Speed table
     * @throws UnsupportedOperationException
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Doesn't allow any updates to Speed table
     *
     * @param uri           Path to Speed table
     * @param values        Values to insert into Speed table
     * @param selection     Column names to select
     * @param selectionArgs Arguments for selection
     * @throws UnsupportedOperationException
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
