package com.pettinder;

import android.preference.PreferenceFragment;
import android.os.Bundle;

public class DiscoveryPreferencesFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}