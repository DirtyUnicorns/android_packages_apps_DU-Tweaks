/*
 *  Copyright (C) 2013 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
/*
 * Copyright (C) 2013 The CyanogenMod project
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

import java.util.prefs.PreferenceChangeListener;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.Context;
import android.media.AudioSystem;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.ListPreference;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class ButtonSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String CATEGORY_VOLUME = "button_volume_keys";
    private static final String CATEGORY_KEYS = "button_keys";

    // volume rocker wake
    private static final String VOLUME_ROCKER_WAKE = "volume_rocker_wake";
    // volume rocker music control
    public static final String VOLUME_ROCKER_MUSIC_CONTROLS = "volume_rocker_music_controls";
    // volume rocker reorient
    private static final String SWAP_VOLUME_BUTTONS = "swap_volume_buttons";

//    private static final String VIRTUAL_KEY_HAPTIC_FEEDBACK = "virtual_key_haptic_feedback";
//    private static final String FORCE_SHOW_OVERFLOW_MENU = "force_show_overflow_menu";
    private static final String KEYS_BRIGHTNESS_KEY = "button_brightness";
//    private static final String KEYS_SHOW_NAVBAR_KEY = "navigation_bar_show";
//    private static final String KEYS_DISABLE_HW_KEY = "hardware_keys_disable";

    private SwitchPreference mVolumeRockerWake;
    private SwitchPreference mVolumeRockerMusicControl;
    private SwitchPreference mSwapVolumeButtons;
//    private ListPreference mVolumeDefault;
//    private CheckBoxPreference mHeadsetHookLaunchVoice;
//    private CheckBoxPreference mVirtualKeyHapticFeedback;
//    private CheckBoxPreference mForceShowOverflowMenu;
    private boolean mButtonBrightnessSupport;
//    private CheckBoxPreference mEnableNavBar;
//    private CheckBoxPreference mDisabkeHWKeys;
    private PreferenceScreen mButtonBrightness;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.button_settings);

        final ContentResolver resolver = getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        final Resources res = getResources();

        // volume rocker wake
        mVolumeRockerWake = (SwitchPreference) findPreference(VOLUME_ROCKER_WAKE);
        mVolumeRockerWake.setOnPreferenceChangeListener(this);
        int volumeRockerWake = Settings.System.getInt(getContentResolver(),
                VOLUME_ROCKER_WAKE, 0);
        mVolumeRockerWake.setChecked(volumeRockerWake != 0);
        mButtonBrightnessSupport = getResources().getBoolean(com.android.internal.R.bool.config_button_brightness_support);

        // volume rocker music control
        mVolumeRockerMusicControl = (SwitchPreference) findPreference(VOLUME_ROCKER_MUSIC_CONTROLS);
        mVolumeRockerMusicControl.setOnPreferenceChangeListener(this);
        int volumeRockerMusicControl = Settings.System.getInt(getContentResolver(),
                VOLUME_ROCKER_MUSIC_CONTROLS, 0);
        mVolumeRockerMusicControl.setChecked(volumeRockerMusicControl != 0);

        // volume rocker reorient
        mSwapVolumeButtons = (SwitchPreference) findPreference(SWAP_VOLUME_BUTTONS);
        mSwapVolumeButtons.setOnPreferenceChangeListener(this);
        int swapVolumeButtons = Settings.System.getInt(getContentResolver(),
                SWAP_VOLUME_BUTTONS, 0);
        mSwapVolumeButtons.setChecked(swapVolumeButtons != 0);

        final PreferenceCategory keysCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_KEYS);

        if (!res.getBoolean(R.bool.config_has_hardware_buttons)) {
            prefScreen.removePreference(keysCategory);
        } else {
//            mVirtualKeyHapticFeedback = (CheckBoxPreference) prefScreen.findPreference(
//                    VIRTUAL_KEY_HAPTIC_FEEDBACK);
//            mForceShowOverflowMenu = (CheckBoxPreference) prefScreen.findPreference(
//                    FORCE_SHOW_OVERFLOW_MENU);
//            mEnableNavBar = (CheckBoxPreference) prefScreen.findPreference(
//                    KEYS_SHOW_NAVBAR_KEY);
//            mDisabkeHWKeys = (CheckBoxPreference) prefScreen.findPreference(
//                    KEYS_DISABLE_HW_KEY);
            mButtonBrightness = (PreferenceScreen) prefScreen.findPreference(
                    KEYS_BRIGHTNESS_KEY);
//            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//            if (vibrator == null || !vibrator.hasVibrator()) {
//                removePreference(VIRTUAL_KEY_HAPTIC_FEEDBACK);
//            } else {
//                mVirtualKeyHapticFeedback.setChecked(Settings.System.getInt(resolver,
//                        Settings.System.VIRTUAL_KEYS_HAPTIC_FEEDBACK, 1) == 1);
//            }
//
//            boolean hasNavBar = getResources().getBoolean(
//                    com.android.internal.R.bool.config_showNavigationBar);
//            mForceShowOverflowMenu.setChecked(Settings.System.getInt(resolver,
//                    Settings.System.FORCE_SHOW_OVERFLOW_MENU, (!hasNavBar && hasMenuKey) ? 0 : 1) == 1);
//
//            boolean harwareKeysDisable = Settings.System.getInt(resolver,
//                        Settings.System.HARDWARE_KEYS_DISABLE, 0) == 1;
//            mDisabkeHWKeys.setChecked(harwareKeysDisable);
//
            if (!mButtonBrightnessSupport) {
                keysCategory.removePreference(mButtonBrightness);
            }
//            updateDisableHWKeyEnablement(harwareKeysDisable);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mVolumeRockerWake) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), VOLUME_ROCKER_WAKE,
                    value ? 1 : 0);
            return true;
        } else if (preference == mVolumeRockerMusicControl) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), VOLUME_ROCKER_MUSIC_CONTROLS,
                    value ? 1 : 0);
            return true;
        } else if (preference == mSwapVolumeButtons) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), SWAP_VOLUME_BUTTONS,
                    value ? 1 : 0);
            return true;
        }
        return false;
    }
}
