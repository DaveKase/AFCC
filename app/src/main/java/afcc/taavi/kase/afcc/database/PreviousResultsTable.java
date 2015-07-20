package afcc.taavi.kase.afcc.database;

import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Taavi on 6.04.2015.
 *
 * Holds previous results table static variables and URIs
 */
public class PreviousResultsTable implements BaseColumns {
    public static final String TABLE_NAME = "previous_results";
    public static final String COL_DATE = "date";
    public static final String COL_RESULT = "result";
    public static final String COL_UNIT = "unit";
    public static final String COL_ROW = "nr";
    public static final String DEFAULT_SORT_ORDER = _ID;

    public static final Uri CONTENT_URI = Uri.parse(Database.SCHEME + Database.AUTHORITY + "/" + TABLE_NAME);
    public static final int RESULTS = 1;
    public static final int RESULT = 2;

    public static final UriMatcher URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.kase.results";

    static {
        URIMatcher.addURI(Database.AUTHORITY, TABLE_NAME, RESULTS);
        URIMatcher.addURI(Database.AUTHORITY, TABLE_NAME + "/#", RESULT);
    }
    //public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.kase.result";

    public static void createTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY, "
                + COL_DATE + " DATETIME, "
                + COL_RESULT + " TEXT, "
                + COL_UNIT + " TEXT, "
                + COL_ROW + " TEXT"
                + ");";

        db.execSQL(sql);
    }
}
