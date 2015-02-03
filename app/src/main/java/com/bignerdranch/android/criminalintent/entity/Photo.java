package com.bignerdranch.android.criminalintent.entity;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Jermie Domingo on 2/1/2015.
 */
public class Photo {

	private static final String JSON_FILE_NAME = "filename";

	private String mFileName;

	public Photo(String fileName) {
		mFileName = fileName;
	}

	public Photo(JSONObject json) throws JSONException {
		mFileName = json.getString(JSON_FILE_NAME);
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JSON_FILE_NAME, mFileName);
		return jsonObject;
	}

	public String getFileName() {
		return mFileName;
	}
}

