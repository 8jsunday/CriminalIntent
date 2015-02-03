package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.bignerdranch.android.criminalintent.entity.Crime;
import com.bignerdranch.android.criminalintent.entity.CrimeLab;
import com.bignerdranch.android.criminalintent.fragment.CrimeFragment;
import com.bignerdranch.android.criminalintent.fragment.CrimeListFragment;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by jermie on 1/31/2015.
 */
public class CrimePagerActivity extends ActionBarActivity implements CrimeFragment.Callbacks {

	private ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);

		final ArrayList<Crime> mCrimes = CrimeLab.get(this).getCrimes();
		FragmentManager fm = getSupportFragmentManager();

		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public Fragment getItem(int position) {

				return CrimeFragment.newInstance(mCrimes.get(position).getId());
			}

			@Override
			public int getCount() {

				return mCrimes.size();
			}
		});

		UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for (int i = 0; i<mCrimes.size(); i++) {
			if (mCrimes.get(i).getId().equals(crimeId)) {
				mViewPager.setCurrentItem(i);
				break;
			}

		}

	}

	//to prevent crashing, every activity that hosts crimefragment should implement Callbacks/this method
	@Override
	public void onCrimeUpdated(Crime c) {

	}
}
