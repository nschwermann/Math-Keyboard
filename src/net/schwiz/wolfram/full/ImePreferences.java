package net.schwiz.wolfram.full;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ImePreferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.ime_preferences);
	}
}
