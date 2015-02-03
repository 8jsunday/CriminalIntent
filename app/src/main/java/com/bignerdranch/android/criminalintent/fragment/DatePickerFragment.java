package com.bignerdranch.android.criminalintent.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.bignerdranch.android.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment {


	public static final String EXTRA_CRIME_DATE = DatePickerFragment.class.getSimpleName() + ".DATE";
	private Date mDate;

	public static DatePickerFragment newInstance(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_DATE, date);
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}


	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mDate = (Date) getArguments().getSerializable(EXTRA_CRIME_DATE);
		Calendar cal = Calendar.getInstance();
		cal.setTime(mDate);
		int year = cal.get(Calendar.YEAR);
		 int month = cal.get(Calendar.MONTH);
		 int day = cal.get(Calendar.DAY_OF_MONTH);

		View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_date_picker,null);
		DatePicker datePicker = (DatePicker)v.findViewById(R.id.date_picker_dialog);
	datePicker.init(year,month,day,new DatePicker.OnDateChangedListener() {
		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

			mDate = new GregorianCalendar(year,monthOfYear,dayOfMonth).getTime();
			getArguments().putSerializable(EXTRA_CRIME_DATE,mDate);

		}
	});

return new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.date_picker_title).setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
	@Override
	public void onClick(DialogInterface dialog, int which) {
		sendResult(Activity.RESULT_OK);
	}
}).create();

	}

	private void sendResult(int result){

		if(getTargetFragment()==null){
			return;
		}

		Intent i = new Intent();
		i.putExtra(EXTRA_CRIME_DATE,mDate);

		getTargetFragment().onActivityResult(getTargetRequestCode(),result,i);

	}
}
