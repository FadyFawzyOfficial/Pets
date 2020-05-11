package com.engineerfadyfawzi.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.engineerfadyfawzi.pets.data.PetContract.PetEntry;
import com.engineerfadyfawzi.pets.data.PetDbHelper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Display list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity
{
    /**
     * Database helper that will provide us access to the database
     */
    private PetDbHelper mDbHelper;
    
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
        
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new PetDbHelper( this );
    }
    
    @Override
    protected void onStart()
    {
        super.onStart();
        displayDatabaseInfo();
    }
    
    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo()
    {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT };
        
        // Perform a query on the pets table
        Cursor cursor = db.query(
                PetEntry.TABLE_NAME,    // The table to query
                projection,             // The columns to return
                null,           // The columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,           // Don't group the rows
                null,           // Don't filter by row groups
                null );         // The sort order
        
        TextView displayView = findViewById( R.id.text_view_pet );
        
        try
        {
            // Create a header in the TextView that looks like this:
            //
            // The pets tables contains <number fo rows in Cursor> pets.
            // _id - name - breed - gender - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText( "The pets table contains " + cursor.getCount() + " pets.\n\n" );
            displayView.append( PetEntry._ID + " - " +
                    PetEntry.COLUMN_PET_NAME + " - " +
                    PetEntry.COLUMN_PET_BREED + " - " +
                    PetEntry.COLUMN_PET_GENDER + " - " +
                    PetEntry.COLUMN_PET_WEIGHT + "\n" );
            
            // Figure out the index of each column
            int petIdColumnIndex = cursor.getColumnIndex( PetEntry._ID );
            int petNameColumnIndex = cursor.getColumnIndex( PetEntry.COLUMN_PET_NAME );
            int petBreedColumnIndex = cursor.getColumnIndex( PetEntry.COLUMN_PET_BREED );
            int petGenderColumnIndex = cursor.getColumnIndex( PetEntry.COLUMN_PET_GENDER );
            int petWeightColumnIndex = cursor.getColumnIndex( PetEntry.COLUMN_PET_WEIGHT );
            
            // Iterate through all the returned rows in the cursor
            while ( cursor.moveToNext() )
            {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentPetID = cursor.getInt( petIdColumnIndex );
                String currentPetName = cursor.getString( petNameColumnIndex );
                String currentPetBreed = cursor.getString( petBreedColumnIndex );
                int currentPetGender = cursor.getInt( petGenderColumnIndex );
                int currentPetWeight = cursor.getInt( petWeightColumnIndex );
                
                // Display the values from each column of the current row (pet) in the cursor in the
                // TextView
                displayView.append( "\n" + currentPetID + " - " +
                        currentPetName + " - " +
                        currentPetBreed + " - " +
                        currentPetGender + " - " +
                        currentPetWeight );
            }
        }
        finally
        {
            // Always close the cursor when you're done reading from it.
            // This release all its resources and makes it invalid.
            cursor.close();
        }
    }
    
    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertPet()
    {
        // Get a database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put( PetEntry.COLUMN_PET_NAME, "Toto" );
        values.put( PetEntry.COLUMN_PET_BREED, "Terrier" );
        values.put( PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE );
        values.put( PetEntry.COLUMN_PET_WEIGHT, 7 );
        
        // Insert a new row for Toto in the database, returning the ID value of that new row.
        // The first argument for db.insert is the pets table name.
        // The second argument provides the name of a column in which the framework can insert NULL
        // in the event that ContentValues is empty (if this is set to "null",
        // then the framework will not insert a row when there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
        long newRowId = db.insert( PetEntry.TABLE_NAME, null, values );
        
        Log.v( "CatalogActivity", "New row ID " + newRowId );
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
                displayDatabaseInfo();
                return true;
            
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do noting for now
                return true;
        }
        
        return super.onOptionsItemSelected( item );
    }
}