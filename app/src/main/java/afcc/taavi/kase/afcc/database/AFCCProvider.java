package afcc.taavi.kase.afcc.database;

import afcc.taavi.kase.afcc.database.Database.DatabaseHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.security.InvalidParameterException;

/**
 * Created by Taavi Kase on 24.09.2014.
 *
 * Gives the layer between database and activities
 */
public class AFCCProvider extends ContentProvider {
    private DatabaseHelper mOpenHelper;
    private Provider[] providers;

    /**
     * Holds all sub-providers
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = DatabaseHelper.getInstance(getContext());
        providers = new Provider[] {
                new SettingsProvider(getContext(), mOpenHelper, SettingsTable.URIMatcher),
                new PreviousResultsProvider(getContext(), mOpenHelper,
                        PreviousResultsTable.URIMatcher)
        };

        return true;
    }

    /**
     * Gets the content type from URI
     *
     * @param uri ContentUri
     * @return String ContentType / null
     * @throws InvalidParameterException
     */
    @Override
    public String getType(Uri uri) {
        for (Provider p: providers) {
            if (p.matchThisProvider(uri))
                return p.getType(uri);
        }

        throw new InvalidParameterException();
    }

    /**
     * Inserts to database
     *
     * @param uri ContentUri
     * @param values ContentValues to insert
     * @throws InvalidParameterException
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        for (Provider p: providers) {
            if (p.matchThisProvider(uri))
                try {
                    return p.insert(uri, values);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
        }

        throw new InvalidParameterException();
    }

    /**
     * Queries database/server to get information
     *
     * @param uri Path to database table
     * @param projection fields to get data from
     * @param selection where clause
     * @param selectionArgs where parameters
     * @param sortOrder order to sort data gotten from database
     * @return cursor
     * @throws InvalidParameterException
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        for (Provider p: providers) {
            if (p.matchThisProvider(uri))
                try {
                    return p.query(uri, projection, selection, selectionArgs, sortOrder);
                } catch(Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
        }

        throw new InvalidParameterException();
    }

    /**
     * Deletes entries from database
     *
     * @param uri ContextUri
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove
     * all rows and get a count pass "1" as the whereClause
     * @throws InvalidParameterException
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        for(Provider p: providers) {
            if(p.matchThisProvider(uri)) {
                return p.delete(uri, selection, selectionArgs);
            }
        }

        throw new InvalidParameterException();
    }

    /**
     * Updates table in database/server, when no where arguments are passed, whole table gets
     * updated
     *
     * @param uri ContextUri
     * @param selection where clause
     * @param selectionArgs where parameters
     * @throws InvalidParameterException
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        for (Provider p: providers) {
            if (p.matchThisProvider(uri)) {
                return p.update(uri, values, selection, selectionArgs);
            }
        }

        throw new InvalidParameterException();
    }

    /**
     * Closes database
     */
    public void close() {
        mOpenHelper.close();
    }

    /**
     * Calls close
     */
    public void shutdown() {
        close();
    }
}