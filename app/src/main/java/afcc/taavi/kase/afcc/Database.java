package afcc.taavi.kase.afcc;

import android.content.Context;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Taavi on 24.09.2014.
 */
public class Database {
    private static final String SCHEME = "content://";
    private static final String AUTHORITY = "kase.taavi.averagefuelconsumption";
    private static final String TAG = "Database";

    public static final class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "averageconsumption.db";
        private static final int DATABASE_VERSION = 1;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "creating database " + DATABASE_NAME + " version: " + DATABASE_VERSION);
            onCreateSettings(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
            onUpgradeSettings(db);
        }

        private void onCreateSettings(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + Settings.TABLE_NAME + " ("
                    + Settings._ID + " INTEGER PRIMARY KEY, "
                    + Settings.COL_DISTANCE + " INTEGER, "
                    + Settings.COL_UNIT + " INTEGER, "
                    + Settings.COL_CONSUMPTION + " INTEGER"
                    + ");");
        }

        private void onUpgradeSettings(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + Settings.TABLE_NAME);
            onCreateSettings(db);
        }
    }

    public static interface BaseColumns extends android.provider.BaseColumns {}

    public static final class Settings implements BaseColumns {
        public static final String TABLE_NAME ="settings";
        public static final String COL_DISTANCE = "distance";
        public static final String COL_UNIT = "unit";
        public static final String COL_CONSUMPTION = "consumption";

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/" + TABLE_NAME);
        public static final int SETTINGS = 1;

        public static final UriMatcher URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        static {
            URIMatcher.addURI(AUTHORITY, TABLE_NAME, SETTINGS);
        }

        public static String CONTENT_TYPE = "vnd.android.cusror.dir/vnd.kase.settings";
    }
}