package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.bignerdranch.android.criminalintent.entity.Crime;
import com.bignerdranch.android.criminalintent.fragment.CrimeFragment;
import com.bignerdranch.android.criminalintent.fragment.CrimeListFragment;


public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {


	@Override
	protected Fragment createFragment() {
		return new CrimeListFragment();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_masterdetail;
	}

	@Override
	public void onCrimeSelected(Crime crime) {
		if (findViewById(R.id.detail_fragment_container) == null && crime != null) {
			Intent i = new Intent(this, CrimePagerActivity.class);
			i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
			startActivity(i);
		} else {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();

			Fragment oldDetail = fm.findFragmentById(R.id.detail_fragment_container);
			if (oldDetail != null) {
				ft.remove(oldDetail);
			}
			Fragment newDetail;
			if (crime != null) {
				newDetail = CrimeFragment.newInstance(crime.getId());
				ft.add(R.id.detail_fragment_container, newDetail);
			}


			ft.commit();
		}
	}

	@Override
	public void onCrimeUpdated(Crime c) {
		FragmentManager fm = getSupportFragmentManager();
		CrimeListFragment listFragment = (CrimeListFragment) fm.findFragmentById(R.id.fragment_container);
		listFragment.updateUI();
	}
}

