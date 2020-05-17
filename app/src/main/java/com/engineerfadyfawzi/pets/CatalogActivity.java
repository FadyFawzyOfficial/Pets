package com.engineerfadyfawzi.pets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.engineerfadyfawzi.pets.data.PetContract.PetEntry;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Display list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks< Cursor >
{
    /**
     * Identifies for the pet data loader
     */
    private static final int PET_LOADER = 0;
    
    /**
     * Adapter for the ListView
     */
    private PetCursorAdapter mPetCursorAdapter;
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_catalog );
        
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                Intent intent = new Intent( CatalogActivity.this, EditorActivity.class );
                startActivity( intent );
            }
        } );
        
        // Find the ListView which will be populated with the pet data.
        ListView petListView = findViewById( R.id.list_view );
        
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById( R.id.empty_view );
        petListView.setEmptyView( emptyView );
        
        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        mPetCursorAdapter = new PetCursorAdapter( this, null );
        // Attach cursor adapter to the ListView.
        petListView.setAdapter( mPetCursorAdapter );
        
        // Initializes the CursorLoader. The PET_LOADER value is eventually passed to onCreateLoader().
        // Kick off the loader
        getSupportLoaderManager().initLoader( PET_LOADER, null, this );
    }
    
    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertPet()
    {
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put( PetEntry.COLUMN_PET_NAME, "Toto" );
        values.put( PetEntry.COLUMN_PET_BREED, "Terrier" );
        values.put( PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE );
        values.put( PetEntry.COLUMN_PET_WEIGHT, 7 );
        
        // Insert a new row for Toto in the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert( PetEntry.CONTENT_URI, values );
        
        // long newRowId = ContentUris.parseId( newUri );
        // Log.v( "CatalogActivity", "New row ID " + newRowId );
    }
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate( R.menu.menu_catalog, menu );
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // User clicked on a menu option in the app bar overflow menu
        switch ( item.getItemId() )
        {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                return true;
            
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do noting for now
                return true;
        }
        
        return super.onOptionsItemSelected( item );
    }
    
    @Override
    public Loader< Cursor > onCreateLoader( int loaderID, Bundle args )
    {
        // Define a projection that specifies which columns from the table we care about.
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED };
        
        // This loader will execute the ContentProvider's query method on a background thread.
        return new CursorLoader(
                this,               // Parent activity context
                PetEntry.CONTENT_URI,       // Table to query (URI): Provider content URI to query
                projection,                 // Projection to return: Columns to incluede in the resulting Cursor
                null,               // No selection clause
                null,           // No selection arguments
                null );             // Default sort order
    }
    
    /**
     * Defines the callback that CursorLoader calls when it's finished its query.
     *
     * @param loader
     * @param cursor
     */
    @Override
    public void onLoadFinished( Loader< Cursor > loader, Cursor cursor )
    {
        // Update {@link PetCursorAdapter} w ith this new cursor containing updated pet data
        mPetCursorAdapter.swapCursor( cursor );
    }
    
    /**
     * Invoked when the CursorLoader is being reset. For example, this is called if the data in the
     * provider (PetContentProvider or database) changes and teh Cursor becomes stale.
     *
     * @param loader
     */
    @Override
    public void onLoaderReset( Loader< Cursor > loader )
    {
        // Callback called when the data needs to be deleted.
        // Clears out the adapter's reference to the Cursor.
        // This prevents memory leaks.
        mPetCursorAdapter.swapCursor( null );
    }
}