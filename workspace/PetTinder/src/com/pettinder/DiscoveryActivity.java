package com.pettinder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.AsyncTask;
import android.content.res.Resources;

import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class DiscoveryActivity extends ActionBarActivity {

	Intent matchesIntent, settingsIntent, viewProfileIntent;

	private Map<String, Boolean> userChoices;
	private ParseObject petProfile;
	private ParseUser currentUser, potentialUserMatch;
	private List<ParseUser> potentialMatches, allUsers;
	private ImageView profilePic;
	private ImageButton yes, no, profile;
	private TextView name, breed;

	private static final String TAG = "DiscoveryActivity";
	private String currentId = "";

	// Fetch the next discovery profile, updating the layout
	private void getProfile() {
		potentialUserMatch = potentialMatches.remove(0);
		currentId = potentialUserMatch.getObjectId();
		Log.d(TAG, currentId);
		// Get user's pet profile
		ParseObject petProfile = null;
		if (potentialUserMatch.has("myPetProfile")) {
			try {
				petProfile = potentialUserMatch.getParseObject("myPetProfile")
						.fetchIfNeeded();
				if (petProfile != null) {
					// Set view data for pet
					ParseFile profilePicture = petProfile
							.getParseFile("profilePicture");
					final Resources res = this.getResources();
					
					// Update the profile picture in the background
					new AsyncTask<ParseFile, Void, Void>() {
						BitmapDrawable imageBitmap;
						@Override
						protected void onPreExecute() {
							// Set loading image while the AsyncTask works
							profilePic.setImageResource(R.drawable.loading);
						}
						@Override
						protected Void doInBackground(ParseFile... files) {
							try {
								// Load data and render the profile pic
								byte[] data;
								data = files[0].getData();
								imageBitmap = new BitmapDrawable(res,
										BitmapFactory.decodeByteArray(data, 0,
												data.length));
							} catch (ParseException e) {
								Log.d(TAG, "Error loading image");
								e.printStackTrace();
							}
							return null;
						}
						@Override
						protected void onPostExecute(Void result) {
							// Set the image to the new loaded pic
							profilePic.setImageDrawable(imageBitmap);
						}
					}.execute(profilePicture);
					
					name.setText(petProfile.getString("petName"));
					breed.setText(petProfile.getString("petBreed"));
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
				Log.d(TAG,
						"Error: Could not retrieve potential match's pet profile");
			}
		}

	}

	// Processes the user's selection for the current discovery profile
	private void handleDiscoverySelection(boolean liked) {
		// Add the discovery selection to the current user's like-dislike history
		if (!userChoices.containsKey(currentId)) {
			userChoices.put(currentId, liked);
			currentUser.put("userChoices", userChoices);
			try {
				currentUser.save();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Log.d(TAG, currentId);
		} else if (liked) { // TODO ********** Should this really be an else?
			// Check for a mutual like
			ParseObject temp = null;
			try {
				Map<String, Boolean> likedUseruserChoices = potentialUserMatch
						.getMap("userChoices");
				String currentUsername = currentUser.getObjectId();
				if (likedUseruserChoices != null
						&& (likedUseruserChoices.containsKey(currentUsername) && likedUseruserChoices
								.get(currentUsername))) {
					// Create match records for these users
					temp = new ParseObject("matches");
					temp.put("User_1", currentId);
					temp.put("User_2", currentUsername);
					temp.save();
					temp = new ParseObject("matches");
					temp.put("User_1", currentUsername);
					temp.put("User_2", currentId);
					temp.save();
				}
			} catch (ParseException e) {
				Log.d(TAG, "Error: Could not retrieve potential match line 112");
			}
		}
		
		// Get next profile or load "out of profiles" content
		if (potentialMatches.size() > 0) {
			getProfile();
		} else {
			profilePic.setImageResource(R.drawable.mystery_doge);
			name.setText("No pets left in the area!");
			breed.setText("");
			currentId = "";
			yes.setVisibility(View.GONE);
			no.setVisibility(View.GONE);
			profile.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discovery);
		// define intents
		matchesIntent = new Intent(this, MatchesActivity.class);
		settingsIntent = new Intent(this, SettingsActivity.class);
		viewProfileIntent = new Intent(this, ViewProfileActivity.class);
		potentialMatches = new LinkedList<ParseUser>();

		// Find views
		profilePic = (ImageView) findViewById(R.id.discoveryImage);
		name = (TextView) findViewById(R.id.discoveryName);
		breed = (TextView) findViewById(R.id.discoveryBreed);
		
		// Register buttons
		no = (ImageButton) findViewById(R.id.discoveryNo);
		no.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				handleDiscoverySelection(false);
			}
		});

		yes = (ImageButton) findViewById(R.id.discoveryYes);
		yes.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				handleDiscoverySelection(true);
			}
		});

		profile = (ImageButton) findViewById(R.id.discoveryMore);
		profile.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {

				// probably to intent.putExtra(something) to know which profile
				// to load in ViewProfileActivity
				startActivity(viewProfileIntent);
			}
		});

		// Hide buttons while profiles load
		yes.setVisibility(View.GONE);
		no.setVisibility(View.GONE);
		profile.setVisibility(View.GONE);
		
		// Prepare choices map and possible matches in background
		new AsyncTask<Void, Void, Void>() {			
			@Override
			public Void doInBackground(Void...voids) {
				// Check for the currently logged in user and that it is linked to a
				// Facebook account
				currentUser = ParseUser.getCurrentUser();
				if ((currentUser == null) || !ParseFacebookUtils.isLinked(currentUser)) {
					Log.d(TAG, "Error: Cannot retrieve user");
				}

				// get the preferred radius
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());
				final int radius = Integer.parseInt(sp.getString("prefDiscoveryRange", "10"));

				// Set up list of profiles and obtain the first profile
				ParseQuery<ParseUser> query = ParseUser.getQuery();
				try {
					allUsers = query.find();
				} catch (ParseException e) {
					Log.d(TAG, "Error: cannot retrieve nearby users");
				}

				// Initialize userChoices to store data, setting its object ID to the
				// current user's username
				if (currentUser.has("userChoices")) {
					userChoices = currentUser.getMap("userChoices");
					Log.d(TAG, "currentUser has userChoices");
					if (userChoices == null) {
						userChoices = new HashMap<String, Boolean>();
						currentUser.put("userChoices", userChoices);
						Log.d(TAG, "userChoices was null");
					}
				} else {
					userChoices = new HashMap<String, Boolean>();
					currentUser.put("userChoices", userChoices);
					Log.d(TAG, "userChoices added for first time");
				}
				try {
					currentUser.save();
					currentUser.fetch();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				// Check each user to see if it is a possible match
				for (ParseUser user : allUsers) {
					double user_lat = currentUser.getNumber("Latitude").doubleValue();
					double user_long = currentUser.getNumber("Longitude").doubleValue();
					double match_lat = user.getNumber("Latitude").doubleValue();
					double match_long = user.getNumber("Longitude").doubleValue();
					Log.d(TAG, "radius = " + radius);
					Log.d(TAG, "Lat = " + Double.toString(user_lat - match_lat));
					Log.d(TAG, "Long = " + Double.toString(user_long - match_long));
					// Check that the user is in range
					if (Math.abs(user_lat - match_lat) <= radius
							&& Math.abs(user_long - match_long) <= radius) {
						// Check that the user is not the current user
						if (!currentUser.getString("username").equals(
								user.getString("username"))) {
							// Check that the user has not already been seen
							if (!userChoices.containsKey(user.getObjectId())) {
								petProfile = user.getParseObject("myPetProfile");
								try {
									// Check that the user has a pet profile
									if (petProfile != null) {
										// Add to list of possible matches
										petProfile = user
												.getParseObject("myPetProfile")
												.fetchIfNeeded();
										potentialMatches.add(user);
										Log.d(TAG, "potential match added");
									} else {
										Log.d(TAG, "petProfle is null");
									}
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				// Load first profile
				if (potentialMatches.size() > 0) {
					getProfile();
					yes.setVisibility(View.VISIBLE);
					no.setVisibility(View.VISIBLE);
					profile.setVisibility(View.VISIBLE);
				} else { // Load "out of profiles" content
					profilePic.setImageResource(R.drawable.mystery_doge);
					name.setText("No pets left in the area!");
					breed.setText("");
					currentId = "";
					yes.setVisibility(View.GONE);
					no.setVisibility(View.GONE);
					profile.setVisibility(View.GONE);
				}
			}
		}.execute();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		currentUser.saveInBackground();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.discovery_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.settings) {
			startActivity(settingsIntent);
			return true;
		} else if (id == R.id.matches) {
			startActivity(matchesIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
