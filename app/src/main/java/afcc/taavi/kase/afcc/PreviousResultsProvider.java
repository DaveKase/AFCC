package afcc.taavi.kase.afcc;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

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
        return null;
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
        return null;
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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
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
        return 0;
    }
}