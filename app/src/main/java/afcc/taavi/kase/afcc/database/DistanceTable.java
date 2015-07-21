package afcc.taavi.kase.afcc.database;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Taavi on 21.07.2015.
 *
 * Holds previous results table static variables and URIs
 */
public class DistanceTable implements BaseColumns {
    public static final String TABLE_NAME = "distance_table";
    public static final String COL_TEXT = "text";
    public static final String DEFAULT_SORT_ORDER = _ID;

    public static final Uri CONTENT_URI = Uri.parse(Database.SCHEME + Database.AUTHORITY + "/" + TABLE_NAME);
    public static final int DISTANCES = 1;

    public static final UriMatcher URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.kase.distance";

    static {
        URIMatcher.addURI(Database.AUTHORITY, TABLE_NAME, DISTANCES);
    }

    /**
     * Creates distance table
     *
     * @param db Instance of database
     */
    public static void createTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY, "
                + COL_TEXT + " TEXT"
                + ");";

        db.execSQL(sql);

        ContentValues values = new ContentValues();
        values.put(_ID, 0);
        values.put(COL_TEXT, "Kilometers");
        db.insert(TABLE_NAME, null, values);

        values.clear();
        values.put(_ID, 1);
        values.put(COL_TEXT, "Miles");
        db.insert(TABLE_NAME, null, values);
    }
}
