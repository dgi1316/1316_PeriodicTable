package com.hard.targets.periodictable;

import java.util.Locale;

import com.hard.targets.periodictable.imagezoom.ImageViewTouch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class TableActivity extends Activity {

	private ImageViewTouch mImageView;
	Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		setupActionBar();
		
		Locale l = Locale.getDefault();
		
		if (l.toString().contains("hi_IN"))
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ptable_hi);
		else
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ptable);
		
		mImageView = (ImageViewTouch) findViewById(R.id.imageView1);
		mImageView.setImageBitmapReset(bitmap, true);
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.table, menu);
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
