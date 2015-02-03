package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by jermie on 1/31/2015.
 */
public abstract class SingleFragmentActivity extends ActionBarActivity {

	protected abstract Fragment createFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragment_container);
		if (fragment == null) {
			fragment = createFragment();
			fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
		}


	}

	protected int getLayoutId() {
		return R.layout.activity_fragment;
	}
}
