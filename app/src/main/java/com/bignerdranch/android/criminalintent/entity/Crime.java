package com.bignerdranch.android.criminalintent.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by jermie on 1/31/2015.
 */
public class Crime {

	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE = "date";
	private static final String JSON_PHOTO = "photo";
	private static final String JSON_SUSPECT = "suspect";
	private static final String JSON_PHONE = "phone";

	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	private String mPhoto;
	private String mSuspect;
	private String mPhone;


	public Crime() {
		mId = UUID.randomUUID();
		mDate = new Date();
	}

	public Crime(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		mSolved = json.getBoolean(JSON_SOLVED);
		mDate = new Date(json.getLong(JSON_DATE));

		if(json.has(JSON_TITLE)){
			mTitle = json.getString(JSON_TITLE);
		}
		if (json.has(JSON_PHOTO)) {
			mPhoto = json.getString(JSON_PHOTO);
		}

		if (json.has(JSON_SUSPECT)) {
			mSuspect = json.getString(JSON_SUSPECT);
		}

		if (json.has(JSON_PHONE)) {
			mPhone = json.getString(JSON_PHONE);
		}
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public UUID getId() {
		return mId;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean solved) {
		mSolved = solved;
	}

	public String getPhoto() {
		return mPhoto;
	}

	public void setPhoto(String photo) {
		mPhoto = photo;
	}

	public String getSuspect() {
		return mSuspect;
	}

	public void setSuspect(String suspect) {
		mSuspect = suspect;
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		mPhone = phone;
	}

	@Override
	public String toString() {

		return mTitle;
	}

	public JSONObject toJSON() throws JSONException {

		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_SOLVED, mSolved);
		json.put(JSON_DATE, mDate.getTime());
		json.put(JSON_PHOTO, mPhoto);
		json.put(JSON_SUSPECT, mSuspect);
		json.put(JSON_PHONE, mPhone);

		return json;
	}

}
