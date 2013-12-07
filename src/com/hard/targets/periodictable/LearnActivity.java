package com.hard.targets.periodictable;

import java.io.IOException;
import java.util.Locale;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LearnActivity extends FragmentActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.learn, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 118;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}
	}

	public static class DummySectionFragment extends Fragment {
		
		public static final String ARG_SECTION_NUMBER = "section_number";
		DatabaseHelper myDbHelper;
		LinearLayout ll;
		TextView tvAtomicNumber, tvSymbol, tvName, tvAtomicWeight;
		WebView wv;
		String txtcolor, color, url;
		String TABLE_NAME = "ptable";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_learn_dummy, container, false);
			Locale l = Locale.getDefault();
			if (l.toString().contains("hi_IN"))
				TABLE_NAME = "ptable_hi";
			else
				TABLE_NAME = "ptable";
			myDbHelper = new DatabaseHelper(getActivity());
			try {
				myDbHelper.createDatabase();
			} catch (IOException e) {
				throw new Error("Unable to create database.");
			}
			try {
				myDbHelper.openDatabase();
			} catch (SQLException sqle) {
				throw sqle;
			}
			
			ll = (LinearLayout) rootView.findViewById(R.id.section_Layout);
			tvAtomicNumber = (TextView) rootView.findViewById(R.id.section_Atomic_Number);
			tvSymbol = (TextView) rootView.findViewById(R.id.section_Symbol);
			tvName = (TextView) rootView.findViewById(R.id.section_Name);
			tvAtomicWeight = (TextView) rootView.findViewById(R.id.section_Atomic_Weight);
			wv = (WebView) rootView.findViewById(R.id.section_Detail);
			
			txtcolor = "#" + myDbHelper.getTextColor(TABLE_NAME, (getArguments().getInt(ARG_SECTION_NUMBER)));
			tvSymbol.setTextColor(Color.parseColor(txtcolor));
			color = "#" + myDbHelper.getColor(TABLE_NAME, (getArguments().getInt(ARG_SECTION_NUMBER)));
			ll.setBackgroundColor(Color.parseColor(color));
			
			tvAtomicNumber.setText(myDbHelper.getAtomicNumber(TABLE_NAME, (getArguments().getInt(ARG_SECTION_NUMBER))));
			tvSymbol.setText(myDbHelper.getSymbol(TABLE_NAME, (getArguments().getInt(ARG_SECTION_NUMBER))));
			tvName.setText(myDbHelper.getName(TABLE_NAME, (getArguments().getInt(ARG_SECTION_NUMBER))));
			tvAtomicWeight.setText(myDbHelper.getAtomicWeight(TABLE_NAME, ((getArguments().getInt(ARG_SECTION_NUMBER)))));
			
			url = myDbHelper.getURL(TABLE_NAME, getArguments().getInt(ARG_SECTION_NUMBER));
			wv.loadUrl(url);
			
			return rootView;
		}
	}

}
