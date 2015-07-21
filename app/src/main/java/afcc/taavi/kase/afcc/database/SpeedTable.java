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
public class SpeedTable implements BaseColumns {
    public static final String TABLE_NAME = "unit_table";
    public static final String COL_DISTANCE_ID ="distance_id";
    public static final String COL_TEXT = "text";
    public static final String DEFAULT_SORT_ORDER = _ID;

    public static final Uri CONTENT_URI = Uri.parse(Database.SCHEME + Database.AUTHORITY + "/" + TABLE_NAME);
    public static final int SPEEDS = 1;

    public static final UriMatcher URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.kase.consumption";

    static {
        URIMatcher.addURI(Database.AUTHORITY, TABLE_NAME, SPEEDS);
    }

    /**
     * Creates speed table
     *
     * @param db Instance of database
     */
    public static void createTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY, "
                + COL_DISTANCE_ID + " INTEGER, "
                + COL_TEXT + " TEXT"
                + ");";

        db.execSQL(sql);

        ContentValues values = new ContentValues();
        values.put(_ID, 0);
        values.put(COL_DISTANCE_ID, 0);
        values.put(COL_TEXT, "km/h");
        db.insert(TABLE_NAME, null, values);

        values.clear();
        values.put(_ID, 1);
        values.put(COL_DISTANCE_ID, 1);
        values.put(COL_TEXT, "mph");
        db.insert(TABLE_NAME, null, values);
    }
}
