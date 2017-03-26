package com.akshenkadakia.homeutility.newspaper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.medicine.DayListFragment;
import com.akshenkadakia.homeutility.medicine.FamilyFragment;
import com.akshenkadakia.homeutility.newspaper.parser.RSSFeed;
import com.akshenkadakia.homeutility.newspaper.parser.RSSItem;
import com.akshenkadakia.homeutility.newspaper.util.WriteObjectFile;

import java.util.List;
import java.util.Locale;

import de.visorapp.visor.VisorActivity;

import static com.akshenkadakia.homeutility.newspaper.parser.RSSUtil.getFeedName;


public class ListActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
	Menu menu;
	// Check if we refreshed
	private boolean isRefresh = false;
	// The adapter for the list
	private ListAdapter adapter;
	// The list to display it in
	private ListView list;
	// The RSSFeed of the site
	private RSSFeed feed;
    private TextToSpeech tts;
    private List<RSSItem> listData;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Create a new ViewGroup for the fragment
		setContentView(R.layout.feed_list);
       // tts = new TextToSpeech(this, (TextToSpeech.OnInitListener) this);
        // button on click event
		// If we're above Honeycomb
		getSupportActionBar().setTitle(getString(R.string.news));
		// Get feed from the passed bundle
		feed = (RSSFeed)new WriteObjectFile(this).readObject(getFeedName());

		// Find the ListView we're using
		list = (ListView)findViewById(R.id.listView);
		// Set the vertical edges to fade when scrolling
		list.setVerticalFadingEdgeEnabled(true);

		// Create a new adapter
		adapter = new ListAdapter(this, feed);
		// Set the adapter to the list
        //Log.d(adapter.getCount());
		list.setAdapter(adapter);
        listData=feed.getTitieString();
        tts=new TextToSpeech(this,this);
		// Set on item click listener to the ListView
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// Start the new activity and pass on the feed item
				//startActivity(new Intent(getBaseContext(), PostActivity.class).putExtra("pos", arg2));
                tts.speak(listData.get(arg2).getTitle(), TextToSpeech.QUEUE_FLUSH, null);
			}
		});
	}
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
               // btnSpeak.setEnabled(true);
                speakOut();
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut() {
        //int n = adapter.getCount();
        String text= listData.get(0).getTitle();
        Log.e("a",text);

    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Exit instead of going to splash screen
		adapter.notifyDataSetChanged();
	}

	@Override 
	public void onResume(){
		super.onResume();
		// This is awful, but don't change it until I work out another way
		if(isRefresh){
			feed = (RSSFeed)new WriteObjectFile(this).readObject(getFeedName());
			adapter = new ListAdapter(ListActivity.this, feed);
			list.setAdapter(adapter); 
			isRefresh = false;
		}
		tts=new TextToSpeech(this,this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.news_menu, menu);
		this.menu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.cam_app:
				tts=null;
				Intent intent=new Intent(this, VisorActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}