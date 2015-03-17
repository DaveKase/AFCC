package afcc.taavi.kase.afcc;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Taavi Kase on 24.09.2014.
 *
 * Holds everything that is needed to create and maintain a database
 */
public class Database {
    private static final String SCHEME = "content://";
    private static final String AUTHORITY = "kase.taavi.averagefuelconsumption";
    private static final String TAG = "Database";

    /**
     * Helps to create and update database
     */
    public static final class DatabaseHelper extends SQLiteOpenHelper {
        private static DatabaseHelper mInstance;
        private static final String DATABASE_NAME = "averageconsumption.db";
        private static final int DATABASE_VERSION = 6;
        private static SQLiteDatabase myWritableDb;

        /**
         * Constructor
         *
         * @param context Application context
         */
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Returns a instance of database helper
         *
         * @param context Application context
         * @return Instance of database helper
         */
        public static DatabaseHelper getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new DatabaseHelper(context);
            }

            return mInstance;
        }

        /**
         * Returns a writable database instance in order not to open and close many
         * SQLiteDatabase objects simultaneously
         *
         * @return a writable instance to SQLiteDatabase
         */
        public SQLiteDatabase getMyWritableDatabase() {
            if ((myWritableDb == null) || (!myWritableDb.isOpen())) {
                myWritableDb = this.getWritableDatabase();
            }

            myWritableDb.enableWriteAheadLogging();

            return myWritableDb;
        }

        /**
         * Closes the database
         */
        @Override
        public void close() {
            super.close();
            if (myWritableDb != null) {
                myWritableDb.close();
                myWritableDb = null;
            }
        }

        /**
         * Creates a database
         *
         * @param db Instance of database
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "creating database " + DATABASE_NAME + " version: " + DATABASE_VERSION);
            onCreateSettings(db);
            onCreatePreviousResults(db);
        }

        /**
         * Upgrades a database
         *
         * @param db Instance of database
         * @param oldVersion Number of a previous database version
         * @param newVersion Number of a new database version
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
            onUpgradeSettings(db);
            onUpgradePreviousResults(db);
        }

        /**
         * Creates settings table
         *
         * @param db Instance of database
         */
        private void onCreateSettings(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + Settings.TABLE_NAME + " ("
                    + Settings._ID + " INTEGER PRIMARY KEY, "
                    + Settings.COL_DISTANCE + " INTEGER, "
                    + Settings.COL_UNIT + " INTEGER, "
                    + Settings.COL_CONSUMPTION + " INTEGER, "
                    + Settings.COL_SPEED + " INTEGER"
                    + ");");

            ContentValues values = new ContentValues();
            values.put(Settings._ID, 0);
            values.put(Settings.COL_DISTANCE, 0);
            values.put(Settings.COL_UNIT, 0);
            values.put(Settings.COL_CONSUMPTION, 0);
            values.put(Settings.COL_SPEED, 0);
            db.insert(Settings.TABLE_NAME, null, values);
        }

        /**
         * Creates previous results table
         *
         * @param db Instance of database
         */
        private void onCreatePreviousResults(SQLiteDatabase db) {
            String sql = "CREATE TABLE " + PreviousResults.TABLE_NAME + "("
                    + PreviousResults._ID + " INTEGER PRIMARY KEY, "
                    + PreviousResults.COL_DATE + " DATETIME, "
                    + PreviousResults.COL_RESULT + " TEXT, "
                    + PreviousResults.COL_UNIT + " TEXT, "
                    + PreviousResults.COL_ROW + " TEXT"
                    + ");";

            Log.d(TAG, "sql = " + sql);

            db.execSQL(sql);
        }

        /**
         * Upgrades settings table
         *
         * @param db Instance of database
         */
        private void onUpgradeSettings(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + Settings.TABLE_NAME);
            onCreateSettings(db);
        }

        /**
         * Upgrades previous results table
         *
         * @param db Instance of database
         */
        private void onUpgradePreviousResults(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + PreviousResults.TABLE_NAME);
            onCreatePreviousResults(db);
        }
    }

    /**
     * Adds basic columns to database tables
     */
    public static interface BaseColumns extends android.provider.BaseColumns {}

    /**
     * Holds settings table static variables and URI
     */
    public static final class Settings implements BaseColumns {
        public static final String TABLE_NAME ="settings";
        public static final String COL_DISTANCE = "distance";
        public static final String COL_UNIT = "unit";
        public static final String COL_CONSUMPTION = "consumption";
        public static final String COL_SPEED = "speed";
        public static final String DEFAULT_SORT_ORDER = _ID;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/" + TABLE_NAME);
        public static final int SETTINGS = 1;

        public static final UriMatcher URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        static {
            URIMatcher.addURI(AUTHORITY, TABLE_NAME, SETTINGS);
        }

        public static String CONTENT_TYPE = "vnd.android.cusror.dir/vnd.kase.settings";
    }

    /**
     * Holds previous results table static variables and URIs
     */
    public static final class PreviousResults implements BaseColumns {
        public static final String TABLE_NAME = "previous_results";
        public static final String COL_DATE = "date";
        public static final String COL_RESULT = "result";
        public static final String COL_UNIT = "unit";
        public static final String COL_ROW = "nr";
        public static final String DEFAULT_SORT_ORDER = _ID;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/" + TABLE_NAME);
        public static final int RESULTS = 1;
        public static final int RESULT = 2;

        public static final UriMatcher URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        static {
            URIMatcher.addURI(AUTHORITY, TABLE_NAME, RESULTS);
            URIMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", RESULT);
        }

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.kase.results";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.kase.result";
    }
}