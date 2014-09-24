package afcc.taavi.kase.afcc;

import afcc.taavi.kase.afcc.Database.DatabaseHelper;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Taavi on 24.09.2014.
 *
 * Abstract class that other providers extend
 */
public abstract class Provider {
    protected DatabaseHelper mHelper;
    protected Context mContext;
    private UriMatcher mMatcher;

    /**
     * @param context Application context
     * @param helper Database helper
     * @param matcher URI matcher
     * */
    public Provider(Context context, DatabaseHelper helper, UriMatcher matcher) {
        mContext = context;
        mHelper = helper;
        mMatcher = matcher;
    }

    // Abstract methods
    public abstract int delete(Uri uri, String selection, String[] selectionArgs);
    public abstract String getType(Uri uri);
    public abstract Uri insert(Uri uri, ContentValues values) throws Exception;
    public abstract Cursor query(Uri uri, String[] projection, String selection,
                                 String[] selectionArgs, String sortOrder);

    public abstract int update(Uri uri, ContentValues values, String selection,
                               String[] selectionArgs);

    public boolean matchThisProvider(Uri uri) {
        return mMatcher.match(uri) != UriMatcher.NO_MATCH;
    }
}