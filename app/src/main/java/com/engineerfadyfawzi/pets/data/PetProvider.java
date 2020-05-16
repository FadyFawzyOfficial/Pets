package com.engineerfadyfawzi.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.engineerfadyfawzi.pets.data.PetContract.PetEntry;

/**
 * {@link ContentProvider} for Pets app.
 */
public class PetProvider extends ContentProvider
{
    /**
     * URI matcher code for the content URI for the pets table
     */
    private static final int PETS = 100;
    
    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int PET_ID = 101;
    
    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this code.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher( UriMatcher.NO_MATCH );
    
    // Static initializer. This is run the first time anything is called from this class.
    static
    {
        // The calls to addURI() go here, for all the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        //
        // The content URI of the form "content://com.engineerfadyfawzi.pets/pets" will map to the
        // integer code {@link #PETS}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.
        sUriMatcher.addURI( PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS );
        
        // The content URI of the form "content://com.engineerfadyfawzi.pet/pets/#" will map to the
        // integer code {@link #PET_ID}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wild card is used where "#" can be substituted for an integer.
        // For example, "content://com.engineerfadyfawzi.pets/pets/3" matches, but
        // "content://com.engineerfadyfawzi.pets/pets" (without a number at the end) doesn't match.
        sUriMatcher.addURI( PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS + "/#", PET_ID );
    }
    
    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = PetProvider.class.getSimpleName();
    
    /**
     * Database helper object
     */
    private PetDbHelper mDbHelper;
    
    /**
     * Initialize the provider and the database helper object.
     *
     * @return
     */
    @Override
    public boolean onCreate()
    {
        mDbHelper = new PetDbHelper( getContext() );
        
        return true;
    }
    
    /**
     * Perform the query for the given URI.
     * Use the given projection, selection, selection arguments, and sort order
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     *
     * @return
     */
    @Override
    public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder )
    {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        
        // This cursor will hold the result of the query
        Cursor cursor;
        
        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match( uri );
        
        switch ( match )
        {
            case PETS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order.
                // The cursor could contain multiple rows of the pets table.
                // COMPLETED: Perform database query on pets table
                cursor = database.query( PetEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder );
                break;
            
            case PET_ID:
                // For the PET_ID code, extract out the ID form the URI.
                // For an example URI such as "content://com.engineerfadyfawzi.pets/pets/3",
                // the selection will be "_id=?" and the selection arguments will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[]{ String.valueOf( ContentUris.parseId( uri ) ) };
                
                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query( PetEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder );
                break;
            
            default:
                throw new IllegalArgumentException( "Cannot query unknown URI " + uri );
        }
        
        return cursor;
    }
    
    /**
     * Insert new data into the provider with the given ContentsValues.
     *
     * @param uri
     * @param contentValues
     *
     * @return
     */
    @Override
    public Uri insert( Uri uri, ContentValues contentValues )
    {
        int match = sUriMatcher.match( uri );
        
        switch ( match )
        {
            case PETS:
                return insertPet( uri, contentValues );
            default:
                throw new IllegalArgumentException( "Insertion is not supported for " + uri );
        }
    }
    
    /**
     * Insert a pet into the database with the given content values.
     * Return the new content URI for that specific row in the database.
     *
     * @param uri
     * @param contentValues
     *
     * @return
     */
    private Uri insertPet( Uri uri, ContentValues contentValues )
    {
        // COMPLETED: Insert a new pet into the pets database table with the given contentValues
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        
        // Insert a new pet with the given values in the database, returning the ID of that new row.
        long newRowId = database.insert( PetEntry.TABLE_NAME, null, contentValues );
        
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if ( newRowId == -1 )
        {
            Log.e( LOG_TAG, "Failed to insert row for " + uri );
            return null;
        }
        
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID (of the newly inserted row) appended to the end of it.
        return ContentUris.withAppendedId( uri, newRowId );
    }
    
    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     *
     * @param uri
     * @param contentValues
     * @param selection
     * @param selectionArgs
     *
     * @return
     */
    @Override
    public int update( Uri uri, ContentValues contentValues, String selection, String[] selectionArgs )
    {
        return 0;
    }
    
    /**
     * Delete the data at the given selection and selection arguments.
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     *
     * @return
     */
    @Override
    public int delete( Uri uri, String selection, String[] selectionArgs )
    {
        return 0;
    }
    
    /**
     * Return the MIME type of data for the content URI.
     *
     * @param uri
     *
     * @return
     */
    @Override
    public String getType( Uri uri )
    {
        return null;
    }
}