package com.bignerdranch.android.criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.Utils.DateFormatter;
import com.bignerdranch.android.criminalintent.Utils.ImageFilePath;
import com.bignerdranch.android.criminalintent.entity.Crime;
import com.bignerdranch.android.criminalintent.entity.CrimeLab;

import java.util.Date;
import java.util.UUID;


/**
 * Created by Jermie Domingo on 1/31/2015.
 */
public class CrimeFragment extends Fragment {

	public static final String EXTRA_CRIME_ID = CrimeFragment.class.getSimpleName() + ".CrimeId";
	private static final String DIALOG_IMAGE = "image";
	private static final int REQUEST_DATE = 0;
	private static final int SELECT_IMAGE = 1;
	private static final int PICK_CONTACT = 2;
	private static final String DIALOG_DATE = "date";

	private EditText mCrimeTitleEditText;
	private Button mCrimeDateButton;
	private CheckBox mCrimeSolvedCheckBox;
	private ImageView mCrimeImage;
	private ImageButton mBrowseButton;
	private Crime mCrime;
	private Button mSendReportButton;
	private Button mPickContactButton;
	private Button mCallSuspectButton;
	private Callbacks mCallbacks;

	public static CrimeFragment newInstance(UUID id) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, id);
		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCrime = CrimeLab.get(getActivity()).getCrime((UUID) getArguments().getSerializable(EXTRA_CRIME_ID));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime_detail, container, false);


		mCrimeTitleEditText = (EditText) v.findViewById(R.id.crime_title);
		mCrimeTitleEditText.setText(mCrime.getTitle());
		mCrimeTitleEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mCrime.setTitle(s.toString());
				mCallbacks.onCrimeUpdated(mCrime);
				getActivity().setTitle(mCrime.getTitle());
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		mCrimeDateButton = (Button) v.findViewById(R.id.crime_date);
		updateDate();
		mCrimeDateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());

				datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				datePickerFragment.show(fm, DIALOG_DATE);
			}
		});

		mCrimeSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		mCrimeSolvedCheckBox.setChecked(mCrime.isSolved());
		mCrimeSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCrime.setSolved(isChecked);
				mCallbacks.onCrimeUpdated(mCrime);
			}
		});


		mCrimeImage = (ImageView) v.findViewById(R.id.crime_image_view);
		mCrimeImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCrime.getPhoto() == null) {
					return;
				}

				FragmentManager fm = getActivity().getSupportFragmentManager();
				ImageFragment.newInstance(mCrime.getPhoto()).show(fm, DIALOG_IMAGE);
			}
		});

		mCrimeImage.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (mCrime.getPhoto() == null)
					return true;
				else {
					((ActionBarActivity) getActivity()).startSupportActionMode(new ActionMode.Callback() {
						@Override
						public boolean onCreateActionMode(ActionMode mode, Menu menu) {
							mode.getMenuInflater().inflate(R.menu.context_crime_image, menu);
							return true;
						}

						@Override
						public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
							return false;
						}

						@Override
						public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

							switch (item.getItemId()) {
								case R.id.delete_crime_item: {
									mCrime.setPhoto(null);
									mCrimeImage.setImageURI(null);
									mode.finish();
									return true;
								}
								default:
									return false;
							}

						}

						@Override
						public void onDestroyActionMode(ActionMode mode) {
						}
					});
					v.setSelected(true);
					return true;
				}
			}
		});

		if (mCrime.getPhoto() != null) {
			mCrimeImage.setImageURI(Uri.parse(mCrime.getPhoto()));

		}

		mBrowseButton = (ImageButton) v.findViewById(R.id.crime_image_button);
		mBrowseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(intent, SELECT_IMAGE);
			}
		});

//IMPLICIT INTENT FOR ACTION_SEND of PLAIN TEXT
		mSendReportButton = (Button) v.findViewById(R.id.crime_report_button);
		mSendReportButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
				intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.crime_report_subject));
				intent = Intent.createChooser(intent, getString(R.string.send_crime_report));
				startActivity(intent);
			}
		});


		mPickContactButton = (Button) v.findViewById(R.id.crime_suspect_button);
		mPickContactButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_CONTACT);
			}
		});


		mCallSuspectButton = (Button) v.findViewById(R.id.crime_call_button);
		mCallSuspectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mCrime.getPhone()));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});

		if (mCrime.getSuspect() != null) {
			mPickContactButton.setText("Suspect: " + mCrime.getSuspect());
			mCallSuspectButton.setEnabled(true);
		}
		return v;
	}

	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != Activity.RESULT_OK)
			return;

		if (requestCode == REQUEST_DATE) {

			Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_CRIME_DATE);
			mCrime.setDate(date);
			mCallbacks.onCrimeUpdated(mCrime);
			updateDate();
		} else if (requestCode == SELECT_IMAGE) {

			Uri selectedImageUri = data.getData();
			mCrime.setPhoto(ImageFilePath.getPath(getActivity(), selectedImageUri));
			mCrimeImage.setImageURI(selectedImageUri);
			mCallbacks.onCrimeUpdated(mCrime);

		} else if (requestCode == PICK_CONTACT) {

			Uri contactUri = data.getData();

			String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
			Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
			if (c.getCount() == 0) {
				c.close();
				return;
			}

			c.moveToFirst();
			String suspect = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
			mCrime.setSuspect(suspect);

			Cursor phoneCursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone._ID + "=" + contactId, null, null);

			while (phoneCursor.moveToNext()) {
				String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				mCrime.setPhone(number);
			}
			mPickContactButton.setText("Suspect: " + mCrime.getSuspect());
			mCallSuspectButton.setEnabled(true);
			mCallbacks.onCrimeUpdated(mCrime);
			phoneCursor.close();
			c.close();
		}


	}

	private void updateDate() {
		mCrimeDateButton.setText(DateFormatter.format(mCrime.getDate()));
	}

	private String getCrimeReport() {
		String solvedString;
		if (mCrime.isSolved()) {
			solvedString = getString(R.string.crime_report_solved);
		} else {
			solvedString = getString(R.string.crime_report_unsolved);
		}

		String dateString = DateFormatter.format(mCrime.getDate());

		String suspect = mCrime.getSuspect();
		if (suspect == null) {
			suspect = getString(R.string.crime_report_no_suspect);
		} else {
			suspect = getString(R.string.crime_report_suspect, suspect);
		}

		String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

		return report;
	}

	public interface Callbacks {
		void onCrimeUpdated(Crime c);
	}

}



