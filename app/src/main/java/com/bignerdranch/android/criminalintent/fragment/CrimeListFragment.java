package com.bignerdranch.android.criminalintent.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.Utils.DateFormatter;
import com.bignerdranch.android.criminalintent.entity.Crime;
import com.bignerdranch.android.criminalintent.entity.CrimeLab;

import java.util.ArrayList;

public class CrimeListFragment extends Fragment {


	private CrimeAdapter adapter;
	private ArrayList<Crime> mCrimes;
	private Button mAddButton;
	private Callbacks mCallbacks;
	private ListView listView;

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
		setHasOptionsMenu(true);

		mCrimes = CrimeLab.get(getActivity()).getCrimes();

		adapter = new CrimeAdapter(mCrimes);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_crime_list, container, false);


		LinearLayout emptyLayout = (LinearLayout) v.findViewById(R.id.empty);

		listView = (ListView) v.findViewById(R.id.list);
		listView.setEmptyView(emptyLayout);
		listView.setAdapter(adapter);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			registerForContextMenu(listView);
		} else {
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
				@Override
				public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

				}

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					getActivity().getMenuInflater().inflate(R.menu.context_crime_list, menu);
					return true;
				}

				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}

				@TargetApi(Build.VERSION_CODES.HONEYCOMB)
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
						case R.id.delete_crime_item: {
							CrimeLab crimeLab = CrimeLab.get(getActivity());
							for (int i = adapter.getCount() - 1; i >= 0; i--) {
								if (listView.isItemChecked(i)) {
									crimeLab.deleteCrime(adapter.getItem(i));
									mCallbacks.onCrimeSelected(null);
								}
							}

							mode.finish();
							adapter.notifyDataSetChanged();
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
		}

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Crime crime = adapter.getItem(position);
				mCallbacks.onCrimeSelected(crime);
			}
		});


		mAddButton = (Button) v.findViewById(R.id.add_crime_button);
		mAddButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				addNewCrime();
			}
		});


		return v;
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_add_crime: {
				addNewCrime();
				return true;
			}

			default:
				return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.context_crime_list, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		Crime c = adapter.getItem(info.position);
		switch (item.getItemId()) {
			case R.id.delete_crime_item: {
				CrimeLab.get(getActivity()).deleteCrime(c);
				adapter.notifyDataSetChanged();
				mCallbacks.onCrimeSelected(null);
				return true;
			}
			default:
				return super.onContextItemSelected(item);
		}

	}

	private void addNewCrime() {
		Crime crime = new Crime();
		CrimeLab.get(getActivity()).addCrime(crime);
		adapter.notifyDataSetChanged();
		mCallbacks.onCrimeSelected(crime);
	}

	public void updateUI() {
		adapter.notifyDataSetChanged();
	}

	public interface Callbacks {
		void onCrimeSelected(Crime crime);
	}

	private class CrimeAdapter extends ArrayAdapter<Crime> {

		public CrimeAdapter(ArrayList<Crime> crimes) {
			super(getActivity(), 0, crimes);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, parent, false);
			}

			Crime crime = getItem(position);
			TextView titleTextView = (TextView) convertView.findViewById(R.id.crime_title_text_view);
			titleTextView.setText(crime.getTitle());

			TextView dateTextView = (TextView) convertView.findViewById(R.id.crime_date_text_view);
			dateTextView.setText(DateFormatter.format(crime.getDate()));

			CheckBox isSolvedCheckBox = (CheckBox) convertView.findViewById(R.id.crime_solved_check_box);
			isSolvedCheckBox.setChecked(crime.isSolved());

			return convertView;
		}
	}


}
