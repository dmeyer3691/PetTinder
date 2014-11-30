package com.pettinder.test;

import android.test.ActivityInstrumentationTestCase2;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.app.Instrumentation;
import android.test.UiThreadTest;
import com.pettinder.DiscoveryActivity;

public class DiscoveryActivityTests extends
		ActivityInstrumentationTestCase2<DiscoveryActivity> {
	private DiscoveryActivity activity;
	private Instrumentation instr = null;
	
	public DiscoveryActivityTests() {
		super(DiscoveryActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		instr = getInstrumentation();
		activity = getActivity();
	}
	
	public void testPreconditions() {
		assertNotNull(activity);
	}
	
	public void testRotate() {
		int orientation = activity.getResources().getConfiguration().orientation;
		if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		// assert something
		assertNotNull(activity);
		assertNotNull(activity.findViewById(com.pettinder.R.id.discoveryImage));
	}
	
	protected void tearDown() throws Exception {
		activity.finish();
	}
}
