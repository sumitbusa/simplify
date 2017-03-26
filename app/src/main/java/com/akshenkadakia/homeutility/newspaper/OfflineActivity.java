package com.akshenkadakia.homeutility.newspaper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.newspaper.parser.RSSFeed;
import com.akshenkadakia.homeutility.newspaper.parser.RSSUtil;
import com.akshenkadakia.homeutility.newspaper.util.WriteObjectFile;

public class OfflineActivity extends Activity {

	// The RSS Feed item
	private RSSFeed feed;
	// The webview to display in
	private WebView browser;

	@SuppressWarnings("deprecation")
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_reading);
		// If we're above Honeycomb

		// Enable the vertical fading edge (by default it is disabled)
		((ScrollView)findViewById(R.id.scrollview)).setVerticalFadingEdgeEnabled(true);
		// Get the feed object
		feed = (RSSFeed)new WriteObjectFile(this).readObject(RSSUtil.getFeedName());
		// Get the position from the intent
		int position = getIntent().getExtras().getInt("pos");
		// Set the title based on the post
		setTitle(feed.getItem(position).getTitle());
		// Initialize the views
		browser = (WebView)findViewById(R.id.browser);
		// Set the background transparent
		browser.setBackgroundColor(Color.TRANSPARENT);
		// Set our webview properties
		WebSettings browserSettings = browser.getSettings();
		browserSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		browserSettings.setPluginState(PluginState.ON);
		browserSettings.setJavaScriptEnabled(true);
		// Set the views
		browser.loadDataWithBaseURL("http://blog.zackehh.com/", feed.getItem(position).getDescription(), "text/html", "UTF-8", null);
	}

	@Override
	public void onBackPressed(){
		super.onBackPressed();
		startActivity(new Intent(this, ListActivity.class));
		finish();
	}
}
