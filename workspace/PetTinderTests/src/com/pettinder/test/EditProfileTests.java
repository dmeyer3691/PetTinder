package com.pettinder.test;

import android.test.ActivityInstrumentationTestCase2;
import android.app.Instrumentation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.view.KeyEvent;

import com.pettinder.EditProfileActivity;
import com.pettinder.R;

public class EditProfileTests extends
		ActivityInstrumentationTestCase2<EditProfileActivity> {
	private EditProfileActivity activity;
	private Instrumentation instr = null;
	private ImageButton profileButton;
	private EditText petBio, petName, petAge;
	private Spinner genderSelection, breedSelection;
	
	public EditProfileTests() {
		super(EditProfileActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		instr = getInstrumentation();
		activity = getActivity();
		profileButton = (ImageButton) activity.findViewById(R.id.profile_picture_edit);
        petBio = (EditText) activity.findViewById(R.id.profile_bio_edit);
        petName = (EditText) activity.findViewById(R.id.pet_name_edit);
        petAge = (EditText) activity.findViewById(R.id.pet_age_edit);
        genderSelection = (Spinner) activity.findViewById(R.id.gender_toggle);
        breedSelection = (Spinner) activity.findViewById(R.id.breed_toggle);
	}
	
	public void testPreconditions() {
		assertNotNull(activity);
		assertNotNull(profileButton);
		assertNotNull(petBio);
		assertNotNull(petName);
		assertNotNull(petAge);
		assertNotNull(genderSelection);
		assertNotNull(breedSelection);
	}
	
	public void testUIPetName() {
		System.out.println("Thread ID in testUIPetName:" + Thread.currentThread().getId());
		instr.waitForIdleSync();
		instr.runOnMainSync(new Runnable() {
			public void run() {
				System.out.println("Thread Id in TestUIPetName.run:" + Thread.currentThread().getId());
				petName.clearFocus();
				petName.requestFocus();
			}
		});
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_A);
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_B);
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_C);
		instr.waitForIdleSync();
		//this.sendKeys(KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_B, KeyEvent.KEYCODE_C);
		assertEquals("abc", petName.getText().toString());
	}
	
	public void testUIPetBio() {
		System.out.println("Thread ID in testUIPetBio:" + Thread.currentThread().getId());
		instr.waitForIdleSync();
		instr.runOnMainSync(new Runnable() {
			public void run() {
				System.out.println("Thread Id in TestUIPetBio.run:" + Thread.currentThread().getId());
				petBio.clearFocus();
				petBio.requestFocus();
			}
		});
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_A);
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_B);
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_C);
		instr.waitForIdleSync();
		//this.sendKeys(KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_B, KeyEvent.KEYCODE_C);
		assertEquals("abc", petBio.getText().toString());
	}
	
	public void testUIPetAge() {
		System.out.println("Thread ID in testUIPetAge:" + Thread.currentThread().getId());
		instr.waitForIdleSync();
		instr.runOnMainSync(new Runnable() {
			public void run() {
				System.out.println("Thread Id in TestUIPetAge.run:" + Thread.currentThread().getId());
				petAge.clearFocus();
				petAge.requestFocus();
			}
		});
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_2);
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_5);
		instr.waitForIdleSync();
		assertEquals("25", petAge.getText().toString());
	}
	
	public void testUIPetBreed() {
		final int INITIAL_POSITION = 0;
		System.out.println("Thread ID in testUIPetBreed:" + Thread.currentThread().getId());
		instr.waitForIdleSync();
		instr.runOnMainSync(new Runnable() {
			public void run() {
				System.out.println("Thread Id in TestUIPetBreed.run:" + Thread.currentThread().getId());
				breedSelection.clearFocus();
				breedSelection.requestFocus();
				breedSelection.setSelection(INITIAL_POSITION);
			}
		});
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
		instr.waitForIdleSync();
		String targetItem = (String) breedSelection.getItemAtPosition(2);
		String result = (String) breedSelection.getSelectedItem();
		assertEquals(targetItem, result);
	}
	
	public void testUIPetGender() {
		final int INITIAL_POSITION = 1;
		System.out.println("Thread ID in testUIPetGender:" + Thread.currentThread().getId());
		instr.waitForIdleSync();
		instr.runOnMainSync(new Runnable() {
			public void run() {
				System.out.println("Thread Id in TestUIPetGender.run:" + Thread.currentThread().getId());
				genderSelection.clearFocus();
				genderSelection.requestFocus();
				genderSelection.setSelection(INITIAL_POSITION);
			}
		});
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
		instr.waitForIdleSync();
		instr.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
		instr.waitForIdleSync();
		String targetItem = (String) genderSelection.getItemAtPosition(0);
		String result = (String) genderSelection.getSelectedItem();
		assertEquals(targetItem, result);
	}
	
	protected void tearDown() throws Exception {
		activity.finish();
	}
}
