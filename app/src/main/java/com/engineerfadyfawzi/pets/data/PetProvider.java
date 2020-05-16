package com.engineerfadyfawzi.pets.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

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
        
        // COMPLETED: Add 2 content URIs to URI matcher
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
        return null;
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
        return null;
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