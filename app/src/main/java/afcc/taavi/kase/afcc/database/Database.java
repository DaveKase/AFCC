package afcc.taavi.kase.afcc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Taavi Kase on 24.09.2014.
 *
 * Holds everything that is needed to create and maintain a database
 */
public class Database {
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "kase.taavi.averagefuelconsumption";
    private static final String TAG = "Database";

    /**
     * Helps to create and update database
     */
    public static final class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "averageconsumption.db";
        private static final int DATABASE_VERSION = 12;
        private static DatabaseHelper mInstance;
        private static SQLiteDatabase myWritableDb;
        private Context mContext;

        /**
         * Constructor
         *
         * @param context Application context
         */
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mContext = context;
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

            return myWritableDb;
        }

        /**
         * Creates a database
         *
         * @param db Instance of database
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "creating database " + DATABASE_NAME + " version: " + DATABASE_VERSION);

            DistanceTable.createTable(db, mContext);
            UnitTable.createTable(db, mContext);
            ConsumptionTable.createTable(db);
            SpeedTable.createTable(db);

            SettingsTable.createTable(db);
            PreviousResultsTable.createTable(db);
        }

        /**
         * Upgrades a database
         *
         * @param db         Instance of database
         * @param oldVersion Number of a previous database version
         * @param newVersion Number of a new database version
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);

            onUpgradeDistance(db);
            onUpgradeUnit(db);
            onUpgradeConsumption(db);
            onUpgradeSpeed(db);

            onUpgradeSettings(db);
            onUpgradePreviousResults(db);
        }

        /**
         * Upgrades distance table
         *
         * @param db Instance of database
         */
        private void onUpgradeDistance(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + DistanceTable.TABLE_NAME);
            DistanceTable.createTable(db, mContext);
        }

        /**
         * Upgrades unit table
         *
         * @param db Instance of database
         */
        private void onUpgradeUnit(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + UnitTable.TABLE_NAME);
            UnitTable.createTable(db, mContext);
        }

        /**
         * Upgrades consumption table
         *
         * @param db Instance of database
         */
        private void onUpgradeConsumption(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + ConsumptionTable.TABLE_NAME);
            ConsumptionTable.createTable(db);
        }

        /**
         * Upgrades speed table
         *
         * @param db Instance of database
         */
        private void onUpgradeSpeed(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + SpeedTable.TABLE_NAME);
            SpeedTable.createTable(db);
        }

        /**
         * Upgrades settings table
         *
         * @param db Instance of database
         */
        private void onUpgradeSettings(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + SettingsTable.TEMP_TABLE_NAME + " AS SELECT * FROM "
                    + SettingsTable.TABLE_NAME + ";");

            db.execSQL("DROP TABLE IF EXISTS " + SettingsTable.TABLE_NAME);
            db.execSQL("CREATE TABLE " + SettingsTable.TABLE_NAME + " AS SELECT * FROM "
                    + SettingsTable.TEMP_TABLE_NAME + ";");

            db.execSQL("DROP TABLE IF EXISTS " + SettingsTable.TEMP_TABLE_NAME);
        }

        /**
         * Upgrades previous results table
         *
         * @param db Instance of database
         */
        private void onUpgradePreviousResults(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + PreviousResultsTable.TEMP_TABLE_NAME + " AS SELECT * FROM "
                    + PreviousResultsTable.TABLE_NAME + ";");

            db.execSQL("DROP TABLE IF EXISTS " + PreviousResultsTable.TABLE_NAME);
            db.execSQL("CREATE TABLE " + PreviousResultsTable.TABLE_NAME + " AS SELECT * FROM "
                    + PreviousResultsTable.TEMP_TABLE_NAME + ";");

            db.execSQL("DROP TABLE IF EXISTS " + PreviousResultsTable.TEMP_TABLE_NAME);
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
    }
}
