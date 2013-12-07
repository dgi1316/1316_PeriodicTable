package com.hard.targets.periodictable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ShareActionProvider;

public class MainActivity extends Activity implements OnClickListener {

	private ShareActionProvider mShareActionProvider;
	
	Button bLearn, bTable, bLookup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bLearn = (Button) findViewById(R.id.btnLearn);
		bTable = (Button) findViewById(R.id.btnTable);
		bLookup = (Button) findViewById(R.id.btnLookup);
		
		bLearn.setOnClickListener(this);
		bTable.setOnClickListener(this);
		bLookup.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem item = menu.findItem(R.id.menu_item_share);
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		String playStoreLink = "https://play.google.com/store/apps/details?id=" + getPackageName();
		String yourShareText = "Install this app " + playStoreLink;
		Intent shareIntent = ShareCompat.IntentBuilder.from(this).setType("text/plain").setText(yourShareText).getIntent();
		setShareIntent(shareIntent);
		return true;
	}

	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}

	@Override
	public void onClick(View arg0) {
		Intent intent;
		switch (arg0.getId()) {
		case R.id.btnLearn:
			intent = new Intent(this, LearnActivity.class);
			startActivity(intent);
			break;
		case R.id.btnTable:
			intent = new Intent(this, TableActivity.class);
			startActivity(intent);
			break;
		case R.id.btnLookup:
			intent = new Intent(this, LookupActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
