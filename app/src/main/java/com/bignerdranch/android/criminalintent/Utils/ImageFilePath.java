package com.bignerdranch.android.criminalintent.Utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * Created by Limbaniandroid.com
 */
public class ImageFilePath {


	public static String getPath(Context c, Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = c.getContentResolver().query(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}


}
