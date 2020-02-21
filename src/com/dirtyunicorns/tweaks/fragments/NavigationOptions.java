/*
 * Copyright (C) 2018-2020 The Dirty Unicorns Project
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

package com.dirtyunicorns.tweaks.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import androidx.preference.PreferenceCategory;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.du.Utils;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import com.dirtyunicorns.support.preferences.SystemSettingSwitchPreference;

import java.util.ArrayList;
import java.util.List;

@SearchIndexable
public class NavigationOptions extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener, Indexable {

    private static final String KEY_NAVIGATION_BAR_ENABLED = "force_show_navbar";
    private static final String KEY_LAYOUT_SETTINGS = "layout_settings";
    private static final String KEY_NAVIGATION_BAR_ARROWS = "navigation_bar_menu_arrow_keys";
    private static final String KEY_SWAP_NAVIGATION_KEYS = "swap_navigation_keys";
    private static final String KEY_GESTURE_SYSTEM = "gesture_system_navigation";

    private static final String KEY_BACK_LONG_PRESS_ACTION = "back_key_long_press";
    private static final String KEY_BACK_DOUBLE_TAP_ACTION = "back_key_double_tap";
    private static final String KEY_HOME_LONG_PRESS_ACTION = "home_key_long_press";
    private static final String KEY_HOME_DOUBLE_TAP_ACTION = "home_key_double_tap";
    private static final String KEY_APP_SWITCH_LONG_PRESS = "app_switch_key_long_press";
    private static final String KEY_APP_SWITCH_DOUBLE_TAP = "app_switch_key_double_tap";
    private static final String KEY_MENU_LONG_PRESS_ACTION = "menu_key_long_press";
    private static final String KEY_MENU_DOUBLE_TAP_ACTION = "menu_key_double_tap";
    private static final String KEY_CAMERA_LONG_PRESS_ACTION = "camera_key_long_press";
    private static final String KEY_CAMERA_DOUBLE_TAP_ACTION = "camera_key_double_tap";
    private static final String KEY_ASSIST_LONG_PRESS_ACTION = "assist_key_long_press";
    private static final String KEY_ASSIST_DOUBLE_TAP_ACTION = "assist_key_double_tap";

    private static final String KEY_CATEGORY_HOME          = "home_key";
    private static final String KEY_CATEGORY_BACK          = "back_key";
    private static final String KEY_CATEGORY_MENU          = "menu_key";
    private static final String KEY_CATEGORY_ASSIST        = "assist_key";
    private static final String KEY_CATEGORY_APP_SWITCH    = "app_switch_key";
    private static final String KEY_CATEGORY_CAMERA        = "camera_key";

    private static final int KEY_MASK_HOME = 0x01;
    private static final int KEY_MASK_BACK = 0x02;
    private static final int KEY_MASK_MENU = 0x04;
    private static final int KEY_MASK_ASSIST = 0x08;
    private static final int KEY_MASK_APP_SWITCH = 0x10;
    private static final int KEY_MASK_CAMERA = 0x20;

    private ListPreference mBackLongPress;
    private ListPreference mBackDoubleTap;
    private ListPreference mHomeLongPress;
    private ListPreference mHomeDoubleTap;
    private ListPreference mAppSwitchLongPress;
    private ListPreference mAppSwitchDoubleTap;
    private ListPreference mMenuLongPress;
    private ListPreference mMenuDoubleTap;
    private ListPreference mCameraLongPress;
    private ListPreference mCameraDoubleTap;
    private ListPreference mAssistLongPress;
    private ListPreference mAssistDoubleTap;
    private Preference mGestureSystemNavigation;
    private Preference mLayoutSettings;
    private PreferenceCategory mHomeCategory;
    private PreferenceCategory mBackCategory;
    private PreferenceCategory mMenuCategory;
    private PreferenceCategory mAssistCategory;
    private PreferenceCategory mAppSwitchCategory;
    private PreferenceCategory mCameraCategory;
    private SwitchPreference mNavigationBar;
    private SystemSettingSwitchPreference mNavigationArrows;
    private SystemSettingSwitchPreference mSwapHardwareKeys;

    private int deviceKeys;

    private boolean defaultToNavigationBar;
    private boolean navigationBarEnabled;
    private boolean mIsNavSwitchingMode = false;

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.navigation_options);

        final PreferenceScreen prefSet = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        defaultToNavigationBar = getResources().getBoolean(
                com.android.internal.R.bool.config_showNavigationBar);
        navigationBarEnabled = Settings.System.getIntForUser(
                resolver, Settings.System.FORCE_SHOW_NAVBAR,
                defaultToNavigationBar ? 1 : 0, UserHandle.USER_CURRENT) != 0;

        deviceKeys = getResources().getInteger(
                com.android.internal.R.integer.config_deviceHardwareKeys);

        int backKeyLongPress = getResources().getInteger(
                com.android.internal.R.integer.config_longPressOnBackKeyBehavior);
        int backKeyDoubleTap = getResources().getInteger(
                com.android.internal.R.integer.config_doubleTapOnBackKeyBehavior);
        int homeKeyLongPress = getResources().getInteger(
                com.android.internal.R.integer.config_longPressOnHomeKeyBehavior);
        int homeKeyDoubleTap = getResources().getInteger(
                com.android.internal.R.integer.config_doubleTapOnHomeKeyBehavior);
        int AppSwitchKeyLongPress = getResources().getInteger(
                com.android.internal.R.integer.config_longPressOnAppSwitchKeyBehavior);
        int AppSwitchKeyDoubleTap = getResources().getInteger(
                com.android.internal.R.integer.config_doubleTapOnAppSwitchKeyBehavior);
        int MenuKeyLongPress = getResources().getInteger(
                com.android.internal.R.integer.config_longPressOnMenuKeyBehavior);
        int MenuKeyDoubleTap = getResources().getInteger(
                com.android.internal.R.integer.config_doubleTapOnMenuKeyBehavior);
        int CameraKeyLongPress = getResources().getInteger(
                com.android.internal.R.integer.config_longPressOnCameraKeyBehavior);
        int CameraKeyDoubleTap = getResources().getInteger(
                com.android.internal.R.integer.config_doubleTapOnCameraKeyBehavior);
        int AssistKeyLongPress = getResources().getInteger(
                com.android.internal.R.integer.config_longPressOnAssistKeyBehavior);
        int AssistKeyDoubleTap = getResources().getInteger(
                com.android.internal.R.integer.config_doubleTapOnAssistKeyBehavior);

        boolean hasHome = (deviceKeys & KEY_MASK_HOME) != 0 || navigationBarEnabled;
        boolean hasMenu = (deviceKeys & KEY_MASK_MENU) != 0;
        boolean hasBack = (deviceKeys & KEY_MASK_BACK) != 0 || navigationBarEnabled;
        boolean hasAssist = (deviceKeys & KEY_MASK_ASSIST) != 0;
        boolean hasAppSwitch = (deviceKeys & KEY_MASK_APP_SWITCH) != 0 || navigationBarEnabled;
        boolean hasCamera = (deviceKeys & KEY_MASK_CAMERA) != 0;

        mNavigationBar = (SwitchPreference) findPreference(KEY_NAVIGATION_BAR_ENABLED);
        mNavigationBar.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.FORCE_SHOW_NAVBAR,
                defaultToNavigationBar ? 1 : 0) == 1));
        mNavigationBar.setOnPreferenceChangeListener(this);

        mLayoutSettings = (Preference) findPreference(KEY_LAYOUT_SETTINGS);
        if (Utils.isThemeEnabled("com.android.internal.systemui.navbar.twobutton")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_nopill")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_wide_back")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_extra_wide_back")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_extra_wide_back_nopill")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_narrow_back")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_narrow_back_nopill")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_wide_back_nopill")) {
            prefSet.removePreference(mLayoutSettings);
        }

        mGestureSystemNavigation = (Preference) findPreference(KEY_GESTURE_SYSTEM);

        mNavigationArrows = (SystemSettingSwitchPreference) findPreference(KEY_NAVIGATION_BAR_ARROWS);
        if (Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_nopill")) {
            prefSet.removePreference(mNavigationArrows);
        }

        mSwapHardwareKeys = (SystemSettingSwitchPreference) findPreference(KEY_SWAP_NAVIGATION_KEYS);

        mHomeCategory = (PreferenceCategory) prefSet.findPreference(KEY_CATEGORY_HOME);
        mBackCategory = (PreferenceCategory) prefSet.findPreference(KEY_CATEGORY_BACK);
        mMenuCategory = (PreferenceCategory) prefSet.findPreference(KEY_CATEGORY_MENU);
        mAssistCategory = (PreferenceCategory) prefSet.findPreference(KEY_CATEGORY_ASSIST);
        mAppSwitchCategory = (PreferenceCategory) prefSet.findPreference(KEY_CATEGORY_APP_SWITCH);
        mCameraCategory = (PreferenceCategory) prefSet.findPreference(KEY_CATEGORY_CAMERA);

        mBackLongPress = (ListPreference) findPreference(KEY_BACK_LONG_PRESS_ACTION);
        int backlongpress = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_BACK_LONG_PRESS_ACTION, backKeyLongPress, UserHandle.USER_CURRENT);
        mBackLongPress.setValue(String.valueOf(backlongpress));
        mBackLongPress.setSummary(mBackLongPress.getEntry());
        mBackLongPress.setOnPreferenceChangeListener(this);

        mBackDoubleTap = (ListPreference) findPreference(KEY_BACK_DOUBLE_TAP_ACTION);
        int backdoubletap = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_BACK_DOUBLE_TAP_ACTION, backKeyDoubleTap, UserHandle.USER_CURRENT);
        mBackDoubleTap.setValue(String.valueOf(backdoubletap));
        mBackDoubleTap.setSummary(mBackDoubleTap.getEntry());
        mBackDoubleTap.setOnPreferenceChangeListener(this);

        mHomeLongPress = (ListPreference) findPreference(KEY_HOME_LONG_PRESS_ACTION);
        int homelongpress = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_HOME_LONG_PRESS_ACTION, homeKeyLongPress, UserHandle.USER_CURRENT);
        mHomeLongPress.setValue(String.valueOf(homelongpress));
        mHomeLongPress.setSummary(mHomeLongPress.getEntry());
        mHomeLongPress.setOnPreferenceChangeListener(this);

        mHomeDoubleTap = (ListPreference) findPreference(KEY_HOME_DOUBLE_TAP_ACTION);
        int homedoubletap = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_HOME_DOUBLE_TAP_ACTION, homeKeyDoubleTap, UserHandle.USER_CURRENT);
        mHomeDoubleTap.setValue(String.valueOf(homedoubletap));
        mHomeDoubleTap.setSummary(mHomeDoubleTap.getEntry());
        mHomeDoubleTap.setOnPreferenceChangeListener(this);

        mAppSwitchLongPress = (ListPreference) findPreference(KEY_APP_SWITCH_LONG_PRESS);
        int appswitchlongpress = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_APP_SWITCH_LONG_PRESS_ACTION, AppSwitchKeyLongPress, UserHandle.USER_CURRENT);
        mAppSwitchLongPress.setValue(String.valueOf(appswitchlongpress));
        mAppSwitchLongPress.setSummary(mAppSwitchLongPress.getEntry());
        mAppSwitchLongPress.setOnPreferenceChangeListener(this);

        mAppSwitchDoubleTap = (ListPreference) findPreference(KEY_APP_SWITCH_DOUBLE_TAP);
        int appswitchdoubletap = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_APP_SWITCH_DOUBLE_TAP_ACTION, AppSwitchKeyDoubleTap, UserHandle.USER_CURRENT);
        mAppSwitchDoubleTap.setValue(String.valueOf(appswitchdoubletap));
        mAppSwitchDoubleTap.setSummary(mAppSwitchDoubleTap.getEntry());
        mAppSwitchDoubleTap.setOnPreferenceChangeListener(this);

        mMenuLongPress = (ListPreference) findPreference(KEY_MENU_LONG_PRESS_ACTION);
        int menulongpress = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_MENU_LONG_PRESS_ACTION, MenuKeyLongPress, UserHandle.USER_CURRENT);
        mMenuLongPress.setValue(String.valueOf(menulongpress));
        mMenuLongPress.setSummary(mMenuLongPress.getEntry());
        mMenuLongPress.setOnPreferenceChangeListener(this);

        mMenuDoubleTap = (ListPreference) findPreference(KEY_MENU_DOUBLE_TAP_ACTION);
        int menudoubletap = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_MENU_DOUBLE_TAP_ACTION, MenuKeyDoubleTap, UserHandle.USER_CURRENT);
        mMenuDoubleTap.setValue(String.valueOf(menudoubletap));
        mMenuDoubleTap.setSummary(mMenuDoubleTap.getEntry());
        mMenuDoubleTap.setOnPreferenceChangeListener(this);

        mCameraLongPress = (ListPreference) findPreference(KEY_CAMERA_LONG_PRESS_ACTION);
        int cameralongpress = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_CAMERA_LONG_PRESS_ACTION, CameraKeyLongPress, UserHandle.USER_CURRENT);
        mCameraLongPress.setValue(String.valueOf(cameralongpress));
        mCameraLongPress.setSummary(mCameraLongPress.getEntry());
        mCameraLongPress.setOnPreferenceChangeListener(this);

        mCameraDoubleTap = (ListPreference) findPreference(KEY_CAMERA_DOUBLE_TAP_ACTION);
        int cameradoubletap = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_CAMERA_DOUBLE_TAP_ACTION, CameraKeyDoubleTap, UserHandle.USER_CURRENT);
        mCameraDoubleTap.setValue(String.valueOf(cameradoubletap));
        mCameraDoubleTap.setSummary(mCameraDoubleTap.getEntry());
        mCameraDoubleTap.setOnPreferenceChangeListener(this);

        mAssistLongPress = (ListPreference) findPreference(KEY_ASSIST_LONG_PRESS_ACTION);
        int assistlongpress = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_ASSIST_LONG_PRESS_ACTION, AssistKeyLongPress, UserHandle.USER_CURRENT);
        mAssistLongPress.setValue(String.valueOf(assistlongpress));
        mAssistLongPress.setSummary(mAssistLongPress.getEntry());
        mAssistLongPress.setOnPreferenceChangeListener(this);

        mAssistDoubleTap = (ListPreference) findPreference(KEY_ASSIST_DOUBLE_TAP_ACTION);
        int assistdoubletap = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.KEY_ASSIST_DOUBLE_TAP_ACTION, AssistKeyDoubleTap, UserHandle.USER_CURRENT);
        mAssistDoubleTap.setValue(String.valueOf(assistdoubletap));
        mAssistDoubleTap.setSummary(mAssistDoubleTap.getEntry());
        mAssistDoubleTap.setOnPreferenceChangeListener(this);

        if (!hasMenu && mMenuCategory != null) {
            prefSet.removePreference(mMenuCategory);
        }

        if (!hasAssist && mAssistCategory != null) {
            prefSet.removePreference(mAssistCategory);
        }

        if (!hasCamera && mCameraCategory != null) {
            prefSet.removePreference(mCameraCategory);
        }

        if (deviceKeys == 0) {
            prefSet.removePreference(mSwapHardwareKeys);
            prefSet.removePreference(mMenuCategory);
            prefSet.removePreference(mAssistCategory);
            prefSet.removePreference(mCameraCategory);
        }

        mHandler = new Handler();

        navbarCheck();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mNavigationBar) {
            boolean value = (Boolean) objValue;
            if (mIsNavSwitchingMode) {
                return false;
            }
            mIsNavSwitchingMode = true;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.FORCE_SHOW_NAVBAR, value ? 1 : 0);
            navbarCheck();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIsNavSwitchingMode = false;
                }
            }, 1500);
            return true;
        } else if (preference == mBackLongPress) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_BACK_LONG_PRESS_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mBackLongPress.findIndexOfValue((String) objValue);
            mBackLongPress.setSummary(
                    mBackLongPress.getEntries()[index]);
            return true;
        } else if (preference == mBackDoubleTap) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_BACK_DOUBLE_TAP_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mBackDoubleTap.findIndexOfValue((String) objValue);
            mBackDoubleTap.setSummary(
                    mBackDoubleTap.getEntries()[index]);
            return true;
        } else if (preference == mHomeLongPress) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_HOME_LONG_PRESS_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mHomeLongPress.findIndexOfValue((String) objValue);
            mHomeLongPress.setSummary(
                    mHomeLongPress.getEntries()[index]);
            return true;
        } else if (preference == mHomeDoubleTap) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_HOME_DOUBLE_TAP_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mHomeDoubleTap.findIndexOfValue((String) objValue);
            mHomeDoubleTap.setSummary(
                    mHomeDoubleTap.getEntries()[index]);
            return true;
        } else if (preference == mAppSwitchLongPress) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_APP_SWITCH_LONG_PRESS_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mAppSwitchLongPress.findIndexOfValue((String) objValue);
            mAppSwitchLongPress.setSummary(
                    mAppSwitchLongPress.getEntries()[index]);
            return true;
        } else if (preference == mAppSwitchDoubleTap) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_APP_SWITCH_DOUBLE_TAP_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mAppSwitchDoubleTap.findIndexOfValue((String) objValue);
            mAppSwitchDoubleTap.setSummary(
                    mAppSwitchDoubleTap.getEntries()[index]);
            return true;
        } else if (preference == mMenuLongPress) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_MENU_LONG_PRESS_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mMenuLongPress.findIndexOfValue((String) objValue);
            mMenuLongPress.setSummary(
                    mMenuLongPress.getEntries()[index]);
            return true;
        } else if (preference == mMenuDoubleTap) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_MENU_DOUBLE_TAP_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mMenuDoubleTap.findIndexOfValue((String) objValue);
            mMenuDoubleTap.setSummary(
                    mMenuDoubleTap.getEntries()[index]);
            return true;
        } else if (preference == mCameraLongPress) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_CAMERA_LONG_PRESS_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mCameraLongPress.findIndexOfValue((String) objValue);
            mCameraLongPress.setSummary(
                    mCameraLongPress.getEntries()[index]);
            return true;
        } else if (preference == mCameraDoubleTap) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_CAMERA_DOUBLE_TAP_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mCameraDoubleTap.findIndexOfValue((String) objValue);
            mCameraDoubleTap.setSummary(
                    mCameraDoubleTap.getEntries()[index]);
            return true;
        } else if (preference == mAssistLongPress) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_ASSIST_LONG_PRESS_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mAssistLongPress.findIndexOfValue((String) objValue);
            mAssistLongPress.setSummary(
                    mAssistLongPress.getEntries()[index]);
            return true;
        } else if (preference == mAssistDoubleTap) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.KEY_ASSIST_DOUBLE_TAP_ACTION, value,
                    UserHandle.USER_CURRENT);
            int index = mAssistDoubleTap.findIndexOfValue((String) objValue);
            mAssistDoubleTap.setSummary(
                    mAssistDoubleTap.getEntries()[index]);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DIRTYTWEAKS;
    }

    @Override
    public void onResume() {
        super.onResume();
        navbarCheck();
    }

    @Override
    public void onPause() {
        super.onPause();
        navbarCheck();
    }

    private void navbarCheck() {
        boolean navigationBar = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.FORCE_SHOW_NAVBAR, 1) == 1;

        if (deviceKeys == 0) {
            if (navigationBar) {
                mHomeCategory.setEnabled(true);
                mBackCategory.setEnabled(true);
                mMenuCategory.setEnabled(true);
                mAssistCategory.setEnabled(true);
                mAppSwitchCategory.setEnabled(true);
                mCameraCategory.setEnabled(true);
                mNavigationArrows.setEnabled(true);
            } else {
                mHomeCategory.setEnabled(false);
                mBackCategory.setEnabled(false);
                mMenuCategory.setEnabled(false);
                mAssistCategory.setEnabled(false);
                mAppSwitchCategory.setEnabled(false);
                mCameraCategory.setEnabled(false);
                mNavigationArrows.setEnabled(false);
            }
        } else {
            if (navigationBar) {
                mHomeCategory.setEnabled(true);
                mBackCategory.setEnabled(true);
                mMenuCategory.setEnabled(true);
                mAssistCategory.setEnabled(true);
                mAppSwitchCategory.setEnabled(true);
                mCameraCategory.setEnabled(true);
                mNavigationArrows.setEnabled(true);
                mSwapHardwareKeys.setEnabled(false);
            } else {
                mHomeCategory.setEnabled(true);
                mBackCategory.setEnabled(true);
                mMenuCategory.setEnabled(true);
                mAssistCategory.setEnabled(true);
                mAppSwitchCategory.setEnabled(true);
                mCameraCategory.setEnabled(true);
                mNavigationArrows.setEnabled(false);
                mSwapHardwareKeys.setEnabled(true);
            }
        }

        if (Utils.isThemeEnabled("com.android.internal.systemui.navbar.twobutton")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_nopill")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_wide_back")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_extra_wide_back")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_extra_wide_back_nopill")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_narrow_back")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_narrow_back_nopill")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_wide_back_nopill")) {
            mHomeCategory.setEnabled(false);
            mBackCategory.setEnabled(false);
            mMenuCategory.setEnabled(false);
            mAssistCategory.setEnabled(false);
            mAppSwitchCategory.setEnabled(false);
            mCameraCategory.setEnabled(false);
        }

        if (Utils.isThemeEnabled("com.android.internal.systemui.navbar.threebutton")) {
            mGestureSystemNavigation.setSummary(getString(R.string.legacy_navigation_title));
        } else if (Utils.isThemeEnabled("com.android.internal.systemui.navbar.twobutton")) {
            mGestureSystemNavigation.setSummary(getString(R.string.swipe_up_to_switch_apps_title));
        } else if (Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_nopill")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_wide_back")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_extra_wide_back")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_extra_wide_back_nopill")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_narrow_back")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_narrow_back_nopill")
                || Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_wide_back_nopill")) {
            mGestureSystemNavigation.setSummary(getString(R.string.edge_to_edge_navigation_title));
        }
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                                                                            boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();

                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.navigation_options;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    List<String> keys = super.getNonIndexableKeys(context);
                    int deviceKeys = context.getResources().getInteger(
                            com.android.internal.R.integer.config_deviceHardwareKeys);
                    boolean hasMenu = (deviceKeys & KEY_MASK_MENU) != 0;
                    boolean hasAssist = (deviceKeys & KEY_MASK_ASSIST) != 0;
                    boolean hasCamera = (deviceKeys & KEY_MASK_CAMERA) != 0;

                    if (deviceKeys == 0) {
                        keys.add(KEY_SWAP_NAVIGATION_KEYS);
                    }
                    if (!hasMenu) {
                        keys.add(KEY_CATEGORY_MENU);
                        keys.add(KEY_MENU_LONG_PRESS_ACTION);
                        keys.add(KEY_MENU_DOUBLE_TAP_ACTION);
                    }
                    if (!hasAssist) {
                        keys.add(KEY_CATEGORY_ASSIST);
                        keys.add(KEY_ASSIST_LONG_PRESS_ACTION);
                        keys.add(KEY_ASSIST_DOUBLE_TAP_ACTION);
                    }
                    if (!hasCamera) {
                        keys.add(KEY_CATEGORY_CAMERA);
                        keys.add(KEY_CAMERA_LONG_PRESS_ACTION);
                        keys.add(KEY_CAMERA_DOUBLE_TAP_ACTION);
                    }
                    return keys;
                }
            };
}
