package com.engineerfadyfawzi.pets;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.engineerfadyfawzi.pets.data.PetContract.PetEntry;

/**
 * {@link PetCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class PetCursorAdapter extends CursorAdapter
{
    /**
     * Constructs a new {@link PetCursorAdapter}.
     *
     * @param context the context (of the app)
     * @param cursor  The cursor from which to get the data.
     */
    public PetCursorAdapter( Context context, Cursor cursor )
    {
        super( context, cursor, 0 /* flags */ );
    }
    
    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * The newView method is used to inflate a new view and return it,
     * you don't bind any data to the view at this point.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     *
     * @return the newly created list item view.
     */
    @Override
    public View newView( Context context, Cursor cursor, ViewGroup parent )
    {
        // Inflate a list item view using the layout specified in the list_item.xml
        return LayoutInflater.from( context ).inflate( R.layout.list_item, parent, false );
    }
    
    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the pet_name
     * TextView in the list item layout.
     *
     * The bindView method is used to bind all data to a given view,
     * such as setting the text on a TextView.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView( View view, Context context, Cursor cursor )
    {
        // Find individual views that we want to modify in the list item layout.
        TextView petNameTextView = view.findViewById( R.id.pet_name_text_view );
        TextView petBreedTextView = view.findViewById( R.id.pet_breed_text_view );
        
        // Find the columns of pet attributes that we're interested in.
        int petNameColumnIndex = cursor.getColumnIndex( PetEntry.COLUMN_PET_NAME );
        int petBreedColumnIndex = cursor.getColumnIndex( PetEntry.COLUMN_PET_BREED );
        
        // Read the pet attributes from the Cursor for the current pet.
        String petName = cursor.getString( petNameColumnIndex );
        String petBreed = cursor.getString( petBreedColumnIndex );
        
        // Update the TextViews with the attributes for the current pet.
        petNameTextView.setText( petName );
        petBreedTextView.setText( petBreed );
    }
}