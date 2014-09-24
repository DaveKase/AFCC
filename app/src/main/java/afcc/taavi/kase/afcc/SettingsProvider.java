package afcc.taavi.kase.afcc;

import afcc.taavi.kase.afcc.Database.DatabaseHelper;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Taavi on 24.09.2014.
 */
public class SettingsProvider extends Provider {

    public SettingsProvider(Context context, DatabaseHelper helper, UriMatcher matcher) {
        super(context, helper, matcher);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType(Uri uri) {int uriType = Database.Settings.URIMatcher.match(uri);
        switch (uriType) {
            case Database.Settings.SETTINGS:
                return Database.Settings.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) throws Exception {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}