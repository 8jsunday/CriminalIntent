package com.bignerdranch.android.criminalintent.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends DialogFragment {

	private ImageView mImageView;

	public static final String IMAGE_PATH = ImageFragment.class.getSimpleName() + ".imagepath";
	public ImageFragment() {
		// Required empty public constructor
	}

	public static ImageFragment newInstance(String imagePath){
		Bundle args = new Bundle();
		args.putSerializable(IMAGE_PATH,imagePath);
		ImageFragment fragment = new ImageFragment();
		fragment.setArguments(args);
		fragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		mImageView = new ImageView(getActivity());
		String path = getArguments().getString(IMAGE_PATH);
		mImageView.setImageURI(Uri.parse(path));
		mImageView.setMaxHeight(750);
		mImageView.setMaxWidth(400);
		mImageView.setAdjustViewBounds(true);
		return mImageView;
	}


}
