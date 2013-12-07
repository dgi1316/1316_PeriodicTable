package com.hard.targets.periodictable;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Locale;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.SQLException;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class LookupActivity extends FragmentActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lookup);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.lookup, menu);
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

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	public static class DummySectionFragment extends Fragment {
		
		public static final String ARG_SECTION_NUMBER = "section_number";
		int position;
		View rootView = null;
		DatabaseHelper myDbHelper;
		String TABLE_NAME = "ptable";
		AutoCompleteTextView actvSearch;
		ImageButton btnSearch;
		LinkedList<String> ll;
		ArrayAdapter<String> arrayAdapter;

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			position = getArguments().getInt(ARG_SECTION_NUMBER);
			myDbHelper = new DatabaseHelper(getActivity());
			Locale l = Locale.getDefault();
			if (l.toString().contains("hi_IN"))
				TABLE_NAME = "ptable_hi";
			else
				TABLE_NAME = "ptable";
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
			switch (position) {
			case 0:
				rootView = inflater.inflate(R.layout.fragment_lookup_dummy, container, false);
				actvSearch = (AutoCompleteTextView) rootView.findViewById(R.id.actvSearch);
				btnSearch = (ImageButton) rootView.findViewById(R.id.btnSearch);
				ll = myDbHelper.populateList(TABLE_NAME);
				arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, ll);
				actvSearch.setAdapter(arrayAdapter);
				btnSearch.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						InputMethodManager imm = (InputMethodManager) getActivity().getSystemService("input_method");
						imm.hideSoftInputFromWindow(actvSearch.getWindowToken(), 0);
						String search = actvSearch.getText().toString();
						if (!search.contentEquals("")) {
							Intent view = new Intent(getActivity(), ViewActivity.class);
							view.putExtra("search", search);
							view.putExtra("type", position);
							startActivity(view);
						} else
							Toast.makeText(getActivity(), "Enter Element Name or Atomic # to search", Toast.LENGTH_SHORT).show();
					}
				});
				break;
			case 1:
				rootView = inflater.inflate(R.layout.fragment_lookup_dummy1, container, false);
				final ListView lv = (ListView) rootView.findViewById(R.id.listView1);
				LinkedList<String> ll = myDbHelper.getAllElements(TABLE_NAME);
				ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ll);
				lv.setAdapter(aa);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						Intent view1 = new Intent(getActivity(), ViewActivity.class);
						view1.putExtra("search", "" + (lv.getItemIdAtPosition(arg2) + 1));
						view1.putExtra("type", position);
						startActivity(view1);
					}
				});
				break;
			default:
				break;
			}
			return rootView;
		}
	}

}
