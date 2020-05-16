package com.engineerfadyfawzi.pets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.engineerfadyfawzi.pets.data.PetContract.PetEntry;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity
{
    /**
     * EditText field to enter the pet's name
     */
    private EditText mNameEditText;
    
    /**
     * EditText field to enter the pet's breed
     */
    private EditText mBreedEditText;
    
    /**
     * EditText field to enter the pet's weight
     */
    private EditText mWeightEditText;
    
    /**
     * EditText field to enter the pet's gender
     */
    private Spinner mGenderSpinner;
    
    /**
     * Gender of the pet. The possible valid values are in the PetContract.java file:
     * {@link PetEntry#GENDER_UNKNOWN}, {@link PetEntry#GENDER_MALE}, or
     * {@link PetEntry#GENDER_FEMALE}.
     */
    private int mGender = PetEntry.GENDER_UNKNOWN;
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_editor );
        
        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById( R.id.edit_pet_name );
        mBreedEditText = findViewById( R.id.edit_pet_breed );
        mWeightEditText = findViewById( R.id.edit_pet_weight );
        mGenderSpinner = findViewById( R.id.spinner_gender );
        
        setupSpinner();
    }
    
    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner()
    {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource( this,
                R.array.array_gender_option, android.R.layout.simple_spinner_item );
        
        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_dropdown_item_1line );
        
        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter( genderSpinnerAdapter );
        
        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected( AdapterView< ? > parent, View view, int position, long id )
            {
                String selection = ( String ) parent.getItemAtPosition( position );
                if ( !TextUtils.isEmpty( selection ) )
                {
                    if ( selection.equals( getString( R.string.gender_male ) ) )
                        mGender = PetEntry.GENDER_MALE;
                    else if ( selection.equals( getString( R.string.gender_female ) ) )
                        mGender = PetEntry.GENDER_FEMALE;
                    else
                        mGender = PetEntry.GENDER_UNKNOWN;
                }
            }
            
            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected( AdapterView< ? > adapterView )
            {
                mGender = PetEntry.GENDER_UNKNOWN;
            }
        } );
    }
    
    /**
     * Get user input from editor and save new pet into database.
     */
    private void insertPet()
    {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String petName = mNameEditText.getText().toString().trim();
        String petBreed = mBreedEditText.getText().toString();
        int petGender = mGender;
        int petWeight;
        
        try
        {
            // If the user doesn't enter a weight the text field will be empty string "" (No user input).
            // This throws NumberFormatException because we are trying to convert an empty string to an integer
            petWeight = Integer.parseInt( mWeightEditText.getText().toString().trim() );
        }
        catch ( NumberFormatException numberFormatException )
        {
            petWeight = 0; // I set the weight to 0 because this is the default value in the database so..
        }
        
        // Create a ContentValues object where column names are the keys,
        // and a new pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put( PetEntry.COLUMN_PET_NAME, petName );
        values.put( PetEntry.COLUMN_PET_BREED, petBreed );
        values.put( PetEntry.COLUMN_PET_GENDER, petGender );
        values.put( PetEntry.COLUMN_PET_WEIGHT, petWeight );
        
        // Insert a new pet into the provider, returning the content URI for the new pet.
        Uri newUri = getContentResolver().insert( PetEntry.CONTENT_URI, values );
        
        // Show a toast message depending on whether or not the insertion was successful
        if ( newUri != null )
            // If the insertion was successful and we can display a toast indicate that.
            Toast.makeText( this, getString( R.string.editor_insert_pet_successful ),
                    Toast.LENGTH_SHORT ).show();
        else
            // Otherwise, the content URI is null, then there was an erro with insertion.
            Toast.makeText( this, getString( R.string.editor_insert_pet_failed ),
                    Toast.LENGTH_SHORT ).show();
    }
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate( R.menu.menu_editor, menu );
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // User clicked on a menu option in the app bar overflow menu
        switch ( item.getItemId() )
        {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                insertPet();
                // Exit activity (return to previous one)
                finish();
                return true;
            
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask( this );
                return true;
        }
        
        return super.onOptionsItemSelected( item );
    }
}