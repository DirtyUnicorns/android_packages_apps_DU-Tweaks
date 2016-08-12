/*
 * Copyright (C) 2015 The Dirty Unicorns Project
 * Copyright (C) 2015 SlimRoms Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dirtyunicorns.dutweaks.fragments;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.CustomSeekBarPreference;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import com.android.internal.logging.MetricsLogger;

import com.android.settings.DialogCreatable;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.util.du.DeviceUtils;
import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class Recents extends SettingsPreferenceFragment implements DialogCreatable,
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "Recents";

    private static final String USE_SLIM_RECENTS = "slim_recents";
    private static final String ONLY_SHOW_RUNNING_TASKS = "only_show_running_tasks";
    private static final String RECENTS_MAX_APPS = "max_apps";
    private static final String RECENT_PANEL_SHOW_TOPMOST = "recent_panel_show_topmost";
    private static final String RECENT_PANEL_LEFTY_MODE = "recent_panel_lefty_mode";
    private static final String RECENT_PANEL_SCALE = "recent_panel_scale";
    private static final String RECENT_PANEL_EXPANDED_MODE = "recent_panel_expanded_mode";
    private static final String RECENT_PANEL_BG_COLOR = "recent_panel_bg_color";
    private static final String RECENT_CARD_BG_COLOR = "recent_card_bg_color";
    private static final String RECENT_CARD_TEXT_COLOR = "recent_card_text_color";
    private static final String SHOW_CLEAR_ALL_RECENTS = "show_clear_all_recents";
    private static final String RECENTS_SHOW_SEARCH_BAR = "recents_show_search_bar";
    private static final String RECENTS_CLEAR_ALL_LOCATION = "recents_clear_all_location";
    private static final String RECENTS_USE_OMNISWITCH = "recents_use_omniswitch";
    private static final String OMNISWITCH_START_SETTINGS = "omniswitch_start_settings";
    public static final String OMNISWITCH_PACKAGE_NAME = "org.omnirom.omniswitch";
    public static Intent INTENT_OMNISWITCH_SETTINGS = new Intent(Intent.ACTION_MAIN)
            .setClassName(OMNISWITCH_PACKAGE_NAME, OMNISWITCH_PACKAGE_NAME + ".SettingsActivity");
    private static final String CATEGORY_STOCK_RECENTS = "stock_recents";
    private static final String CATEGORY_OMNI_RECENTS = "omni_recents";
    private static final String CATEGORY_SLIM_RECENTS = "slim_recents_category";
    private static final String IMMERSIVE_RECENTS = "immersive_recents";

    private Preference mOmniSwitchSettings;
    private boolean mOmniSwitchInitCalled;
    private PreferenceCategory mOmniSwitch;
    private PreferenceCategory mOmniRecents;
    private PreferenceCategory mStockRecents;
    private PreferenceCategory mSlimRecents;
    private CustomSeekBarPreference mMaxApps;
    private CustomSeekBarPreference mRecentPanelScale;
    private SwitchPreference mUseSlimRecents;
    private SwitchPreference mShowRunningTasks;
    private SwitchPreference mRecentsShowTopmost;
    private SwitchPreference mRecentPanelLeftyMode;
    private SwitchPreference mRecentsClearAll;
    private SwitchPreference mRecentsSearchBar;
    private SwitchPreference mRecentsUseOmniSwitch;
    private ListPreference mRecentPanelExpandedMode;
    private ListPreference mRecentsClearAllLocation;
    private ListPreference mImmersiveRecents;
    private ColorPickerPreference mRecentPanelBgColor;
    private ColorPickerPreference mRecentCardBgColor;
    private ColorPickerPreference mRecentCardTextColor;

    private static final int MENU_RESET = Menu.FIRST;
    private static final int DEFAULT_BACKGROUND_COLOR = 0x00ffffff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.recents);
        initializeAllPreferences();

        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mRecentsUseOmniSwitch = (SwitchPreference)
                prefSet.findPreference(RECENTS_USE_OMNISWITCH);

        try {
            mRecentsUseOmniSwitch.setChecked(Settings.System.getInt(resolver,
                    Settings.System.RECENTS_USE_OMNISWITCH) == 1);
            mOmniSwitchInitCalled = true;
        } catch(SettingNotFoundException e){
            // if the settings value is unset
        }
        mRecentsUseOmniSwitch.setOnPreferenceChangeListener(this);

        mOmniSwitchSettings = (Preference)
                prefSet.findPreference(OMNISWITCH_START_SETTINGS);
        mOmniSwitchSettings.setEnabled(mRecentsUseOmniSwitch.isChecked());

        mOmniSwitch = (PreferenceCategory) findPreference(CATEGORY_OMNI_RECENTS);
        if (!Utils.isPackageInstalled(getActivity(), OMNISWITCH_PACKAGE_NAME)) {
            prefSet.removePreference(mOmniSwitch);
        }

        mStockRecents = (PreferenceCategory) findPreference(CATEGORY_STOCK_RECENTS);
        mSlimRecents = (PreferenceCategory) findPreference(CATEGORY_SLIM_RECENTS);
        mOmniRecents = (PreferenceCategory) findPreference(CATEGORY_OMNI_RECENTS);

        mRecentsClearAll = (SwitchPreference) prefSet.findPreference(SHOW_CLEAR_ALL_RECENTS);
        mRecentsSearchBar = (SwitchPreference) prefSet.findPreference(RECENTS_SHOW_SEARCH_BAR);

        mRecentsClearAllLocation = (ListPreference) prefSet.findPreference(RECENTS_CLEAR_ALL_LOCATION);
        int location = Settings.System.getIntForUser(resolver,
                Settings.System.RECENTS_CLEAR_ALL_LOCATION, 3, UserHandle.USER_CURRENT);
        mRecentsClearAllLocation.setValue(String.valueOf(location));
        mRecentsClearAllLocation.setSummary(mRecentsClearAllLocation.getEntry());
        mRecentsClearAllLocation.setOnPreferenceChangeListener(this);

        mImmersiveRecents = (ListPreference) findPreference(IMMERSIVE_RECENTS);
        mImmersiveRecents.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.IMMERSIVE_RECENTS, 0)));
        mImmersiveRecents.setSummary(mImmersiveRecents.getEntry());
        mImmersiveRecents.setOnPreferenceChangeListener(this);
        updateRecents();
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.DIRTYTWEAKS;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mOmniSwitchSettings){
            startActivity(INTENT_OMNISWITCH_SETTINGS);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mRecentsClearAllLocation) {
            int location = Integer.valueOf((String) newValue);
            int index = mRecentsClearAllLocation.findIndexOfValue((String) newValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.RECENTS_CLEAR_ALL_LOCATION, location, UserHandle.USER_CURRENT);
            mRecentsClearAllLocation.setSummary(mRecentsClearAllLocation.getEntries()[index]);
            return true;
        } else if (preference == mImmersiveRecents) {
            Settings.System.putInt(getContentResolver(), Settings.System.IMMERSIVE_RECENTS,
                    Integer.valueOf((String) newValue));
            mImmersiveRecents.setValue(String.valueOf(newValue));
            mImmersiveRecents.setSummary(mImmersiveRecents.getEntry());
            return true;
        } else if (preference == mUseSlimRecents) {
            Settings.System.putInt(getContentResolver(), Settings.System.USE_SLIM_RECENTS,
                    ((Boolean) newValue) ? 1 : 0);
            updateRecents();
            return true;
        } else if (preference == mShowRunningTasks) {
            Settings.System.putInt(getContentResolver(), Settings.System.RECENT_SHOW_RUNNING_TASKS,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
        } else if (preference == mRecentPanelScale) {
            int val = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.RECENT_PANEL_SCALE_FACTOR, val * 1);
            return true;
        } else if (preference == mRecentPanelExpandedMode) {
            int value = Integer.parseInt((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.RECENT_PANEL_EXPANDED_MODE, value);
            return true;
        } else if (preference == mRecentPanelBgColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            if (hex.equals("#00ffffff")) {
                preference.setSummary(R.string.default_string);
            } else {
                preference.setSummary(hex);
            }
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.RECENT_PANEL_BG_COLOR,
                    intHex);
            return true;
        } else if (preference == mRecentCardBgColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            if (hex.equals("#00ffffff")) {
                preference.setSummary(R.string.default_string);
            } else {
                preference.setSummary(hex);
            }
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.RECENT_CARD_BG_COLOR,
                    intHex);
            return true;
        } else if (preference == mRecentCardTextColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            if (hex.equals("#00ffffff")) {
                preference.setSummary(R.string.default_string);
            } else {
                preference.setSummary(hex);
            }
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.RECENT_CARD_TEXT_COLOR,
                    intHex);
            return true;
        } else if (preference == mRecentPanelLeftyMode) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.RECENT_PANEL_GRAVITY,
                    ((Boolean) newValue) ? Gravity.LEFT : Gravity.RIGHT);
            return true;
        } else if (preference == mRecentsShowTopmost) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.RECENT_PANEL_SHOW_TOPMOST,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
        } else if (preference == mMaxApps) {
            int val = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.RECENTS_MAX_APPS, val * 1);
            return true;
        } else if (preference == mRecentsUseOmniSwitch) {
            boolean value = (Boolean) newValue;

            if (value && !mOmniSwitchInitCalled){
                openOmniSwitchFirstTimeWarning();
                mOmniSwitchInitCalled = true;
            }

            Settings.System.putInt(
                    resolver, Settings.System.RECENTS_USE_OMNISWITCH, value ? 1 : 0);
            mOmniSwitchSettings.setEnabled(value);
            updateRecents();
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecentPanelPreferences();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.reset)
                .setIcon(com.android.internal.R.drawable.ic_menu_refresh)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                resetToDefault();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void resetToDefault() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.shortcut_action_reset);
        alertDialog.setMessage(R.string.reset_message);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                resetValues();
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, null);
        alertDialog.create().show();
    }

    private void resetValues() {
        Settings.System.putInt(getContentResolver(),
                Settings.System.RECENT_PANEL_BG_COLOR, DEFAULT_BACKGROUND_COLOR);
        mRecentPanelBgColor.setNewPreviewColor(DEFAULT_BACKGROUND_COLOR);
        mRecentPanelBgColor.setSummary(R.string.default_string);
        Settings.System.putInt(getContentResolver(),
                Settings.System.RECENT_CARD_BG_COLOR, DEFAULT_BACKGROUND_COLOR);
        mRecentCardBgColor.setNewPreviewColor(DEFAULT_BACKGROUND_COLOR);
        mRecentCardBgColor.setSummary(R.string.default_string);
        Settings.System.putInt(getContentResolver(),
                Settings.System.RECENT_CARD_TEXT_COLOR, DEFAULT_BACKGROUND_COLOR);
        mRecentCardTextColor.setNewPreviewColor(DEFAULT_BACKGROUND_COLOR);
        mRecentCardTextColor.setSummary(R.string.default_string);
    }

    private void updateRecentPanelPreferences() {
        final boolean recentLeftyMode = Settings.System.getInt(getContentResolver(),
                Settings.System.RECENT_PANEL_GRAVITY, Gravity.RIGHT) == Gravity.LEFT;
        mRecentPanelLeftyMode.setChecked(recentLeftyMode);

        final int recentScale = Settings.System.getInt(getContentResolver(),
                Settings.System.RECENT_PANEL_SCALE_FACTOR, 100);
        mRecentPanelScale.setValue(recentScale);

        final int recentExpandedMode = Settings.System.getInt(getContentResolver(),
                Settings.System.RECENT_PANEL_EXPANDED_MODE, 0);
        mRecentPanelExpandedMode.setValue(recentExpandedMode + "");
    }

    private void initializeAllPreferences() {
        mUseSlimRecents = (SwitchPreference) findPreference(USE_SLIM_RECENTS);
        mUseSlimRecents.setOnPreferenceChangeListener(this);

        mShowRunningTasks = (SwitchPreference) findPreference(ONLY_SHOW_RUNNING_TASKS);
        mShowRunningTasks.setOnPreferenceChangeListener(this);

        mMaxApps = (CustomSeekBarPreference) findPreference(RECENTS_MAX_APPS);
        mMaxApps.setOnPreferenceChangeListener(this);
        int maxApps = Settings.System.getInt(getContentResolver(),
                Settings.System.RECENTS_MAX_APPS, 5);
        mMaxApps.setValue(maxApps);

        // Recent panel background color
        mRecentPanelBgColor =
                (ColorPickerPreference) findPreference(RECENT_PANEL_BG_COLOR);
        mRecentPanelBgColor.setOnPreferenceChangeListener(this);
        final int intColor = Settings.System.getInt(getContentResolver(),
                Settings.System.RECENT_PANEL_BG_COLOR, 0x00ffffff);
        String hexColor = String.format("#%08x", (0x00ffffff & intColor));
        if (hexColor.equals("#00ffffff")) {
            mRecentPanelBgColor.setSummary(R.string.default_string);
        } else {
            mRecentPanelBgColor.setSummary(hexColor);
        }
        mRecentPanelBgColor.setNewPreviewColor(intColor);

        // Recent card background color
        mRecentCardBgColor =
                (ColorPickerPreference) findPreference(RECENT_CARD_BG_COLOR);
        mRecentCardBgColor.setOnPreferenceChangeListener(this);
        final int intColorCard = Settings.System.getInt(getContentResolver(),
                Settings.System.RECENT_CARD_BG_COLOR, 0x00ffffff);
        String hexColorCard = String.format("#%08x", (0x00ffffff & intColorCard));
        if (hexColorCard.equals("#00ffffff")) {
            mRecentCardBgColor.setSummary(R.string.default_string);
        } else {
            mRecentCardBgColor.setSummary(hexColorCard);
        }
        mRecentCardBgColor.setNewPreviewColor(intColorCard);

        // Recent card text color
        mRecentCardTextColor =
                (ColorPickerPreference) findPreference(RECENT_CARD_TEXT_COLOR);
        mRecentCardTextColor.setOnPreferenceChangeListener(this);
        final int intColorText = Settings.System.getInt(getContentResolver(),
                Settings.System.RECENT_CARD_TEXT_COLOR, 0x00ffffff);
        String hexColorText = String.format("#%08x", (0x00ffffff & intColorText));
        if (hexColorText.equals("#00ffffff")) {
            mRecentCardTextColor.setSummary(R.string.default_string);
        } else {
            mRecentCardTextColor.setSummary(hexColorText);
        }
        mRecentCardTextColor.setNewPreviewColor(intColorText);

        // Enable options menu for color reset
        setHasOptionsMenu(true);

            boolean enableRecentsShowTopmost = Settings.System.getInt(getContentResolver(),
                                      Settings.System.RECENT_PANEL_SHOW_TOPMOST, 0) == 1;
        mRecentsShowTopmost = (SwitchPreference) findPreference(RECENT_PANEL_SHOW_TOPMOST);
        mRecentsShowTopmost.setChecked(enableRecentsShowTopmost);
        mRecentsShowTopmost.setOnPreferenceChangeListener(this);

        mRecentPanelLeftyMode =
                (SwitchPreference) findPreference(RECENT_PANEL_LEFTY_MODE);
        mRecentPanelLeftyMode.setOnPreferenceChangeListener(this);

        mRecentPanelScale = (CustomSeekBarPreference) findPreference(RECENT_PANEL_SCALE);
        mRecentPanelScale.setOnPreferenceChangeListener(this);
        int recentsPanelScale = Settings.System.getInt(getContentResolver(),
                Settings.System.RECENT_PANEL_SCALE_FACTOR, 100);
        mRecentPanelScale.setValue(recentsPanelScale);



        mRecentPanelExpandedMode =
                (ListPreference) findPreference(RECENT_PANEL_EXPANDED_MODE);
        mRecentPanelExpandedMode.setOnPreferenceChangeListener(this);
    }

    private void updateRecents() {
        boolean slimRecent = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.USE_SLIM_RECENTS, 0) == 1;
        boolean omniRecents = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.RECENTS_USE_OMNISWITCH, 0) == 1;

        if (slimRecent) {
            mSlimRecents.setEnabled(true);
            mStockRecents.setEnabled(false);
            mOmniRecents.setEnabled(false);
            mShowRunningTasks.setEnabled(true);
        } else if (omniRecents) {
            mOmniRecents.setEnabled(true);
            mStockRecents.setEnabled(false);
            mSlimRecents.setEnabled(false);
            mShowRunningTasks.setEnabled(false);
        } else {
           mSlimRecents.setEnabled(true);
           mStockRecents.setEnabled(true);
           mOmniSwitch.setEnabled(true);
           mShowRunningTasks.setEnabled(true);
        }
    }

    private void openOmniSwitchFirstTimeWarning() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.omniswitch_first_time_title))
                .setMessage(getResources().getString(R.string.omniswitch_first_time_message))
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
        }).show();
    }
}
