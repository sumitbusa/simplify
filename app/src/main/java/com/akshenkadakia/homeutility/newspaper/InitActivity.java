package com.akshenkadakia.homeutility.newspaper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.TextView;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.newspaper.util.LoadRSSFeed;

import static com.akshenkadakia.homeutility.newspaper.parser.RSSUtil.RSSFEEDURL;

public class InitActivity extends AppCompatActivity {

	// Keep track of when feed exists
	private SharedPreferences prefs;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getString(R.string.news));
		// Get our preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// Check if a feed exists
		if(!prefs.getBoolean("isSetup", false)){
			// Set the content view
			setContentView(R.layout.splash);
			// Detect if there's a connection issue or not
			ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			// If there's a connection problem
			if (conMgr.getActiveNetworkInfo() == null
					|| !conMgr.getActiveNetworkInfo().isConnected()
					|| !conMgr.getActiveNetworkInfo().isAvailable()) {
				// Display an alert to the user
				AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertBox));
				// Tell the user what happened
				builder.setMessage("Unable to reach server.\nPlease check your connectivity.")
				// Alert title
				.setTitle("Connection Error")
				// Can't exit via back button
				.setCancelable(false)
				// Create exit button
				.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// Exit the application
						finish();
					}
				});
				// Create dialog from builder
				AlertDialog alert = builder.create();
				// Show dialog
				alert.show();
				// Center the message of the dialog
				((TextView)alert.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
				// Center the title of the dialog
				((TextView)alert.findViewById((getResources().getIdentifier("alertTitle", "id", "android")))).setGravity(Gravity.CENTER);
			} else {
				// Change the feed
				new LoadRSSFeed(this,this, RSSFEEDURL).execute();
				//finish();
				//changeFeed(false, this);
			}
		} else {
			// Start the new activity
			startActivity(new Intent(this, ListActivity.class));
			// Kill this one
			finish();
		}
	}
}
