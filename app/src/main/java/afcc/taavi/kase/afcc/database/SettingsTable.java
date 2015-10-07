package afcc.taavi.kase.afcc.database;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Taavi on 6.04.2015.
 *
 * Holds settings table static variables and URI
 */
public class SettingsTable implements BaseColumns {
    public static final String TABLE_NAME = "settings";
    public static final String TEMP_TABLE_NAME="temp_settings";
    public static final String COL_DISTANCE = "distance";
    public static final String COL_UNIT = "unit";
    public static final String COL_CONSUMPTION = "consumption";
    public static final String COL_SPEED = "speed";
    public static final String DEFAULT_SORT_ORDER = _ID;

    public static final Uri CONTENT_URI = Uri.parse(Database.SCHEME + Database.AUTHORITY + "/"
            + TABLE_NAME);

    public static final int SETTINGS = 1;

    public static final UriMatcher URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static String CONTENT_TYPE = "vnd.android.cusror.dir/vnd.kase.settings";

    static {
        URIMatcher.addURI(Database.AUTHORITY, TABLE_NAME, SETTINGS);
    }

    /**
     * Creates settings table
     *
     * @param db Instance of database
     */
    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + COL_DISTANCE + " INTEGER, "
                + COL_UNIT + " INTEGER, "
                + COL_CONSUMPTION + " INTEGER, "
                + COL_SPEED + " INTEGER"
                + ");");

        ContentValues values = new ContentValues();
        values.put(_ID, 0);
        values.put(COL_DISTANCE, 0);
        values.put(COL_UNIT, 0);
        values.put(COL_CONSUMPTION, 0);
        values.put(COL_SPEED, 0);
        db.insert(TABLE_NAME, null, values);
    }
}
