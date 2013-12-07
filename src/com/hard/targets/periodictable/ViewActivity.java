package com.hard.targets.periodictable;

import java.io.IOException;
import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class ViewActivity extends Activity {

	DatabaseHelper myDbHelper;
	LinearLayout ll;
	TextView tv1, tv2, tv3, tv4;
	WebView wv;
	AdView av;
	String TABLE_NAME = "ptable", search;
	int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		setupActionBar();
		myDbHelper = new DatabaseHelper(this);
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
		ll = (LinearLayout) findViewById(R.id.llLayout);
		tv1 = (TextView) findViewById(R.id.tvAtomicNumber);
		tv2 = (TextView) findViewById(R.id.tvSymbol);
		tv3 = (TextView) findViewById(R.id.tvAtomicWeight);
		tv4 = (TextView) findViewById(R.id.tvName);
		wv = (WebView) findViewById(R.id.wvDetail);
		av = (AdView) findViewById(R.id.adViewView);
		Locale l = Locale.getDefault();
		if (l.toString().contains("hi_IN"))
			TABLE_NAME = "ptable_hi";
		else
			TABLE_NAME = "ptable";
		Intent t = getIntent();
		type = t.getIntExtra("type", 0);
		switch (type) {
		case 0:
			search = t.getStringExtra("search");
			Cursor c = myDbHelper.search(TABLE_NAME, search);
			if (c.getCount() == 0) {
				Toast.makeText(this, "Nothing found", Toast.LENGTH_LONG).show();
				c.close();
				this.finish();
			} else {
				c.moveToFirst();
				ll.setBackgroundColor(Color.parseColor("#" + c.getString(1)));
				tv1.setText(c.getString(3));
				tv2.setTextColor(Color.parseColor("#" + c.getString(0)));
				tv2.setText(c.getString(4));
				tv3.setText(c.getString(6));
				tv4.setText(c.getString(5));
				wv.loadUrl(c.getString(7));
				av.loadAd(new AdRequest());
			}
			c.close();
			break;
		case 1:
			search = t.getStringExtra("search");
			Cursor c1 = myDbHelper.view(TABLE_NAME, search);
			c1.moveToFirst();
			ll.setBackgroundColor(Color.parseColor("#" + c1.getString(1)));
			tv1.setText(c1.getString(3));
			tv2.setTextColor(Color.parseColor("#" + c1.getString(0)));
			tv2.setText(c1.getString(4));
			tv3.setText(c1.getString(6));
			tv4.setText(c1.getString(5));
			wv.loadUrl(c1.getString(7));
			av.loadAd(new AdRequest());
			break;
		default:
			break;
		}
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view, menu);
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

}
