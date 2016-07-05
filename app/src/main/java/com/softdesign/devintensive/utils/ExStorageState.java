package com.softdesign.devintensive.utils;

import android.os.Environment;

/**
 * Class that contains functions for a checking the state of External Storage.
 */
public class ExStorageState {
	/** 
	 * Gets the current state of External Storage.
	 * @return The current state of external storage.
	 */
	protected static String getState() {		
		return Environment.getExternalStorageState();			
	}	
	
	/** 
	 * Checks that the state of External Storage is Removed.
	 * @return True, if external storage is removed.
	 */
	public static boolean isRemoved() {
		return Environment.MEDIA_REMOVED.equals(getState());
	}

	/**
	 * Checks that the state of External Storage is Writable.
	 * @return True, if external storage is mounted.
	 */
	public static boolean isWritable() {
		return Environment.MEDIA_MOUNTED.equals(getState());
	}

    /**
     * Checks that the state of External Storage is Readable.
     * @return True, if external storage is read only.
     */
    public static boolean isReadable() {
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(getState());
    }

	/**
	 * Checks that the state of External Storage is Available (Mounted or
	 * ReadOnly).
	 * @return True, if external storage is available for use (mounted or read only).
	 */
	public static boolean isAvailable() {
		return ExStorageState.isWritable() ||
                ExStorageState.isReadable();
	}
}
