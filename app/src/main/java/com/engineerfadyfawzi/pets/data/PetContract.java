package com.engineerfadyfawzi.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Pets app.
 */
public final class PetContract
{
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private PetContract()
    {
    
    }
    
    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.engineerfadyfawzi.pets";
    
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse( "content://" + CONTENT_AUTHORITY );
    
    /**
     * Possible path (appended the base content URI for possible URI's)
     * For instance, content://com.engineerfadyfawzi.pets/pets/ is a valid path for
     * looking at pet data. content://com.engineerfadyfawzi.pets/staff will fail.
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_PETS = "pets";
    
    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    public static abstract class PetEntry implements BaseColumns
    {
        /**
         * Name of database table for pets
         */
        public static final String TABLE_NAME = "pets";
        
        /**
         * The content URI to access the pet data in the provider.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath( BASE_CONTENT_URI, PATH_PETS );
        
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;
        
        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;
        
        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        public static final String _ID = BaseColumns._ID;
        
        /**
         * Name of the pet.
         *
         * Type: TEXT
         */
        public static final String COLUMN_PET_NAME = "name";
        
        /**
         * Breed of the pet.
         *
         * Type: TEXT
         */
        public static final String COLUMN_PET_BREED = "breed";
        
        /**
         * Gender of the pet.
         *
         * The only possible values are {@link PetEntry#GENDER_UNKNOWN},
         * {@link PetEntry#GENDER_MALE}, or{@link PetEntry#GENDER_FEMALE}.
         *
         * Type: INTEGER
         */
        public static final String COLUMN_PET_GENDER = "gender";
        
        /**
         * Weight of the pet.
         *
         * Type: INTEGER
         */
        public static final String COLUMN_PET_WEIGHT = "weight";
        
        /**
         * Possible values for the gender of the pet.
         */
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        
        /**
         * Returns whether or not the given gender is {@link #GENDER_UNKNOWN}. {@link #GENDER_MALE}
         * or {@link #GENDER_FEMALE}.
         *
         * @param gender
         *
         * @return
         */
        public static boolean isValidGender( int gender )
        {
            return gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE;
        }
    }
}