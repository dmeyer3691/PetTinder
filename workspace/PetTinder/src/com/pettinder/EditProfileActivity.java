package com.pettinder;

import android.support.v7.app.ActionBarActivity;
import android.content.ClipData.Item;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;



public class EditProfileActivity extends ActionBarActivity implements OnItemSelectedListener {

	Intent settingsIntent;
	ImageButton profileButton;
	Spinner genderSelection;
	
	View.OnClickListener profile = (new View.OnClickListener() {
		public void onClick(View v){
			// todo - make this open dialog to select picture from phones camera roll
			
		}
	});
    
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //define intents
        settingsIntent = new Intent(this, SettingsActivity.class);
        // make image button display users profile picture
        profileButton = (ImageButton) findViewById(R.id.profile_picture_edit);
        profileButton.setOnClickListener(profile);
        profileButton.setImageResource(R.drawable.loudnoises);
        // profileButton.setImageBitmap(bm); //need to define bm
        genderSelection = (Spinner) findViewById(R.id.gender_toggle);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSelection.setAdapter(adapter);
        genderSelection.setOnItemSelectedListener(this);
    }

    
    @Override
    protected void onStart(){
    	super.onStart();
    }
    
    @Override
    protected void onRestart(){
    	super.onRestart();
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
    }

    @Override
    protected void onDestroy(){
    	super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       
    	//shoulnd't need this here
    	//getMenuInflater().inflate(R.menu.view_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       
    	//shouldn't need this here
    	/*
    	int id = item.getItemId();
        if (id == R.id.settings) {
        	startActivity(settingsIntent);
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }
    

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String selection = (String) parent.getItemAtPosition(pos);
		//store setting based on selection
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		//nothing to do here
		
	}
    
}
