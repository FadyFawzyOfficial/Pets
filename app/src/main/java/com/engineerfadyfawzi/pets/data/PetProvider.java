package com.engineerfadyfawzi.pets.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * {@link ContentProvider} for Pets app.
 */
public class PetProvider extends ContentProvider
{
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
        // COMPLETED: Create and initialize a PetDbHelper object to gain access to the pets database.
        //  Make sure the variable is a global variable, so it can be referenced from other
        //  ContentProvider methods.
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