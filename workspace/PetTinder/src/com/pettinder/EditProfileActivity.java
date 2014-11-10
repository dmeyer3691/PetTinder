package com.pettinder;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.ClipData.Item;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;



public class EditProfileActivity extends ActionBarActivity implements OnItemSelectedListener {

	Intent settingsIntent;
	ImageButton profileButton;
	Spinner genderSelection;
	EditText petBio, petName, petAge, petZip;
	ParseUser currentUser;
	ParseObject petProfile;
	ParseFile profilePicture;
	BitmapDrawable imageBitmap;
	private final String TAG = "EditProfileActivity";
    private static final int GET_FROM_GALLERY = 1;

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    imageBitmap = new BitmapDrawable(this.getResources(), bitmap);
                    profileButton.setImageDrawable(imageBitmap);
            } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
        }
    }
	
    
	private void saveChanges(){
		if (!currentUser.has("myPetProfile")){
			petProfile = new ParseObject("myPetProfile");
		}
		if (petName.getText().toString().equals("")){
			petProfile.put("petName", petName.getHint().toString());
			Log.d(TAG, petName.getText().toString());
		}else{
			petProfile.put("petName", petName.getText().toString());
		}
		if (petAge.getText().toString().equals("")){
			petProfile.put("petAge", petAge.getHint().toString());
		}else{
			petProfile.put("petAge", petAge.getText().toString());
		}
		if (petZip.getText().toString().equals("")){
			petProfile.put("petZip", petZip.getHint());
		}else{
			petProfile.put("petZip", petZip.getText().toString());
		}
		if (petBio.getText().toString().equals("")){
			petProfile.put("petBio", petBio.getHint().toString());
		} else {
			petProfile.put("petBio", petBio.getText().toString());
		}
		
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    imageBitmap.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        if (bytes != null){
            profilePicture = new ParseFile("profilePicture.jpg", bytes);
        }

		try {
			profilePicture.save();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG,"picture failed saving");
		}
		petProfile.put("profilePicture", profilePicture);
		petProfile.saveInBackground();
		currentUser.put("myPetProfile", petProfile);
		currentUser.saveInBackground();
		Log.d(TAG, "SAVED");
		Toast.makeText(this, "Profile Saved", 0).show();

		
	}
	
	private void getParseUserData(){


		if (currentUser.has("myPetProfile")){
			
			if(petProfile.has("petName")){
				petName.setHint(petProfile.getString("petName"));
			}
			if(petProfile.has("petAge")){
				petAge.setHint(petProfile.getString("petAge"));
			}
			if(petProfile.has("petZip")){
				petZip.setHint(petProfile.getString("petZip"));
			}
			if(petProfile.has("petBio")){
				petBio.setHint(petProfile.getString("petBio"));
			}
			if(petProfile.has("petGender")){
				String gender = (String) petProfile.get("petGender");
				if (gender.equals("Male")){
					genderSelection.setSelection(0);
				} else if (gender.equals("Female")){
					genderSelection.setSelection(1);
				}
			}
			if(petProfile.has("profilePicture")){
				profilePicture = petProfile.getParseFile("profilePicture");
				byte[] data;
				try {
					data = profilePicture.getData();
					imageBitmap = new BitmapDrawable(this.getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
					profileButton.setImageDrawable(imageBitmap);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					Log.d(TAG, "Error loading image");
					e.printStackTrace();
				}
			}
			Log.d(TAG, "Profile Loaded");
		} else {
			Log.d(TAG, "no profile available");
		}
		
	}
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Parse.initialize(this, "bl9sFBxmrkDhNWSDxnlvbLIbeFrQ9kHUGEbBRI4a", "tCzPn6RbPx2ZJUmGc7AMb2eBoetXgO02A4jefTHp");
        
        
        //define intents
        settingsIntent = new Intent(this, SettingsActivity.class);
        // make image button display users profile picture
        profileButton = (ImageButton) findViewById(R.id.profile_picture_edit);
        imageBitmap = (BitmapDrawable) getResources().getDrawable(R.drawable.loudnoises);
        profileButton.setImageDrawable(imageBitmap);
        // profileButton.setImageBitmap(bm); //need to define bm
        petBio = (EditText) findViewById(R.id.profile_bio_edit);
        petName = (EditText) findViewById(R.id.pet_name_edit);
        petAge = (EditText) findViewById(R.id.pet_age_edit);
        petZip = (EditText) findViewById(R.id.pet_zip_edit);

        
        //populate gender selection spinner options
        genderSelection = (Spinner) findViewById(R.id.gender_toggle);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSelection.setAdapter(adapter);
        genderSelection.setOnItemSelectedListener(this);
        profileButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
				
			}
		});
        
        
        //user specific stuff
        currentUser = ParseUser.getCurrentUser();
        try {
			petProfile = currentUser.getParseObject("myPetProfile").fetchIfNeeded();
			profilePicture = petProfile.getParseFile("profilePicture");
			Log.d(TAG, "retrieved petProfile");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG,"error fetching");
		}

        
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
    	if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
        	//user logged in through facebook
        	getParseUserData();
		} else {
			Log.d(TAG,"user not logged in");
		}
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	//saveChanges();
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
    	getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       

    	int id = item.getItemId();
        if (id == R.id.save) {
        	saveChanges();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String selection = (String) parent.getItemAtPosition(pos);
		Log.d(TAG, selection);
		petProfile.put("petGender", selection);
		//store setting based on selection
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		//nothing to do here
		
	}
    
}
