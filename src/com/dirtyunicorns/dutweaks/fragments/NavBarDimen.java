package com.dirtyunicorns.dutweaks.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.internal.util.slim.DeviceUtils;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;

public class NavBarDimen extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String TAG = "NavBarDimen";
    private static final String PREF_NAVIGATION_BAR_HEIGHT = "navigation_bar_height";
    private static final String PREF_NAVIGATION_BAR_HEIGHT_LANDSCAPE = "navigation_bar_height_landscape";
    private static final String PREF_NAVIGATION_BAR_WIDTH = "navigation_bar_width";
    private static final String KEY_DIMEN_OPTIONS = "navbar_dimen";

    ListPreference mNavigationBarHeight;
    ListPreference mNavigationBarHeightLandscape;
    ListPreference mNavigationBarWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.navigation_dimens);

        PreferenceScreen prefSet = getPreferenceScreen();

        mNavigationBarHeight =
            (ListPreference) findPreference(PREF_NAVIGATION_BAR_HEIGHT);
        mNavigationBarHeight.setOnPreferenceChangeListener(this);

        mNavigationBarHeightLandscape =
            (ListPreference) findPreference(PREF_NAVIGATION_BAR_HEIGHT_LANDSCAPE);

        if (DeviceUtils.isPhone(getActivity())) {
            prefSet.removePreference(mNavigationBarHeightLandscape);
            mNavigationBarHeightLandscape = null;
        } else {
            mNavigationBarHeightLandscape.setOnPreferenceChangeListener(this);
        }

        mNavigationBarWidth =
            (ListPreference) findPreference(PREF_NAVIGATION_BAR_WIDTH);

        if (!DeviceUtils.isPhone(getActivity())) {
            prefSet.removePreference(mNavigationBarWidth);
            mNavigationBarWidth = null;
        } else {
            mNavigationBarWidth.setOnPreferenceChangeListener(this);
        }

        updateDimensionValues();
    }

    private void updateDimensionValues() {
        int navigationBarHeight = Settings.System.getInt(getContentResolver(),
                Settings.System.NAVIGATION_BAR_HEIGHT, -1);
        if (navigationBarHeight == -1) {
            navigationBarHeight = (int) (getResources().getDimension(
                    com.android.internal.R.dimen.navigation_bar_height)
                    / getResources().getDisplayMetrics().density);
        }
        mNavigationBarHeight.setValue(String.valueOf(navigationBarHeight));
        mNavigationBarHeight.setSummary(mNavigationBarHeight.getEntry());

        if (mNavigationBarHeightLandscape != null) {
            int navigationBarHeightLandscape = Settings.System.getInt(getContentResolver(),
                                Settings.System.NAVIGATION_BAR_HEIGHT_LANDSCAPE, -1);
            if (navigationBarHeightLandscape == -1) {
                navigationBarHeightLandscape = (int) (getResources().getDimension(
                        com.android.internal.R.dimen.navigation_bar_height_landscape)
                        / getResources().getDisplayMetrics().density);
            }
            mNavigationBarHeightLandscape.setValue(String.valueOf(navigationBarHeightLandscape));
            mNavigationBarHeightLandscape.setSummary(mNavigationBarHeightLandscape.getEntry());
        }

        if (mNavigationBarWidth != null) {
            int navigationBarWidth = Settings.System.getInt(getContentResolver(),
                                Settings.System.NAVIGATION_BAR_WIDTH, -1);
            if (navigationBarWidth == -1) {
                navigationBarWidth = (int) (getResources().getDimension(
                        com.android.internal.R.dimen.navigation_bar_width)
                        / getResources().getDisplayMetrics().density);
            }
            mNavigationBarWidth.setValue(String.valueOf(navigationBarWidth));
            mNavigationBarWidth.setSummary(mNavigationBarWidth.getEntry());
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mNavigationBarWidth) {
            int index = mNavigationBarWidth.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.NAVIGATION_BAR_WIDTH, Integer.parseInt((String) newValue));
            updateDimensionValues();
            return true;
        } else if (preference == mNavigationBarHeight) {
            int index = mNavigationBarHeight.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.NAVIGATION_BAR_HEIGHT, Integer.parseInt((String) newValue));
            updateDimensionValues();
            return true;
        } else if (preference == mNavigationBarHeightLandscape) {
            int index = mNavigationBarHeightLandscape.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.NAVIGATION_BAR_HEIGHT_LANDSCAPE, Integer.parseInt((String) newValue));
            updateDimensionValues();
            return true;
        }
        return false;
    }
}
