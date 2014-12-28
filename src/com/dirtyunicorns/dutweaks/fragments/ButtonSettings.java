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
//import org.omnirom.omnigears.preference.SystemCheckBoxPreference;

public class ButtonSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String CATEGORY_VOLUME = "button_volume_keys";
    private static final String CATEGORY_KEYS = "button_keys";
    private static final String CATEGORY_BACK = "button_keys_back";
    private static final String CATEGORY_HOME = "button_keys_home";
    private static final String CATEGORY_MENU = "button_keys_menu";
    private static final String CATEGORY_ASSIST = "button_keys_assist";
    private static final String CATEGORY_APPSWITCH = "button_keys_appSwitch";

//    private static final String BUTTON_VOLUME_WAKE = "button_volume_wake_screen";
//    private static final String BUTTON_VOLUME_DEFAULT = "button_volume_default_screen";
//    private static final String BUTTON_VOLUME_MUSIC_CONTROL = "button_volume_music_control";
//    private static final String VOLUME_KEY_CURSOR_CONTROL = "volume_key_cursor_control";
//    private static final String SWAP_VOLUME_BUTTONS = "swap_volume_buttons";
//    private static final String CATEGORY_HEADSETHOOK = "button_headsethook";
//    private static final String BUTTON_HEADSETHOOK_LAUNCH_VOICE = "button_headsethook_launch_voice";

    private static final String KEYS_CATEGORY_BINDINGS = "keys_bindings";
    private static final String KEYS_ENABLE_CUSTOM = "keys_enable_custom";
    private static final String KEYS_BACK_PRESS = "keys_back_press";
    private static final String KEYS_BACK_LONG_PRESS = "keys_back_long_press";
    private static final String KEYS_HOME_PRESS = "keys_home_press";
    private static final String KEYS_HOME_LONG_PRESS = "keys_home_long_press";
    private static final String KEYS_HOME_DOUBLE_TAP = "keys_home_double_tap";
    private static final String KEYS_MENU_PRESS = "keys_menu_press";
    private static final String KEYS_MENU_LONG_PRESS = "keys_menu_long_press";
    private static final String KEYS_ASSIST_PRESS = "keys_assist_press";
    private static final String KEYS_ASSIST_LONG_PRESS = "keys_assist_long_press";
    private static final String KEYS_APP_SWITCH_PRESS = "keys_app_switch_press";
    private static final String KEYS_APP_SWITCH_LONG_PRESS = "keys_app_switch_long_press";

//    private static final String VIRTUAL_KEY_HAPTIC_FEEDBACK = "virtual_key_haptic_feedback";
//    private static final String FORCE_SHOW_OVERFLOW_MENU = "force_show_overflow_menu";
//    private static final String KEYS_BRIGHTNESS_KEY = "button_brightness";
//    private static final String KEYS_SHOW_NAVBAR_KEY = "navigation_bar_show";
//    private static final String KEYS_DISABLE_HW_KEY = "hardware_keys_disable";

    // Available custom actions to perform on a key press.
    private static final int ACTION_NOTHING = 0;
    private static final int ACTION_MENU = 1;
    private static final int ACTION_APP_SWITCH = 2;
    private static final int ACTION_SEARCH = 3;
    private static final int ACTION_VOICE_SEARCH = 4;
    private static final int ACTION_IN_APP_SEARCH = 5;
    private static final int ACTION_HOME = 6;
    private static final int ACTION_BACK = 7;
    private static final int ACTION_LAST_APP = 8;
    private static final int ACTION_KILL_APP = 9;
//    private static final int ACTION_SLEEP = 10;

    // Masks for checking presence of hardware keys.
    // Must match values in frameworks/base/core/res/res/values/config.xml
    private static final int KEY_MASK_HOME = 0x01;
    private static final int KEY_MASK_BACK = 0x02;
    private static final int KEY_MASK_MENU = 0x04;
    private static final int KEY_MASK_ASSIST = 0x08;
    private static final int KEY_MASK_APP_SWITCH = 0x10;

//    private CheckBoxPreference mVolumeWake;
//    private CheckBoxPreference mVolumeMusicControl;
//    private CheckBoxPreference mSwapVolumeButtons;
//    private ListPreference mVolumeKeyCursorControl;
    private SwitchPreference mEnableCustomBindings;
    private ListPreference mBackPressAction;
    private ListPreference mBackLongPressAction;
    private ListPreference mHomePressAction;
    private ListPreference mHomeLongPressAction;
    private ListPreference mHomeDoubleTapAction;
    private ListPreference mMenuPressAction;
    private ListPreference mMenuLongPressAction;
    private ListPreference mAssistPressAction;
    private ListPreference mAssistLongPressAction;
    private ListPreference mAppSwitchPressAction;
    private ListPreference mAppSwitchLongPressAction;
    private Map<String, Integer> mKeySettings = new HashMap<String, Integer>();
//    private ListPreference mVolumeDefault;
//    private CheckBoxPreference mHeadsetHookLaunchVoice;
//    private CheckBoxPreference mVirtualKeyHapticFeedback;
//    private CheckBoxPreference mForceShowOverflowMenu;
//    private boolean mButtonBrightnessSupport;
//    private CheckBoxPreference mEnableNavBar;
//    private CheckBoxPreference mDisabkeHWKeys;
    private PreferenceScreen mButtonBrightness;
    private PreferenceCategory mKeysBackCategory;
    private PreferenceCategory mKeysHomeCategory;
    private PreferenceCategory mKeysMenuCategory;
    private PreferenceCategory mKeysAppSwitchCategory;
    private PreferenceCategory mKeysAssistCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.button_settings);

        final ContentResolver resolver = getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        final Resources res = getResources();

//        final PreferenceCategory volumeCategory =
//                (PreferenceCategory) prefScreen.findPreference(CATEGORY_VOLUME);

//        mButtonBrightnessSupport = getResources().getBoolean(com.android.internal.R.bool.config_button_brightness_support);

//        if (hasVolumeRocker()) {
//            mVolumeWake = (CheckBoxPreference) findPreference(BUTTON_VOLUME_WAKE);
//            mVolumeDefault = (ListPreference) findPreference(BUTTON_VOLUME_DEFAULT);
//            if (!res.getBoolean(R.bool.config_show_volumeRockerWake)) {
//                volumeCategory.removePreference(mVolumeWake);
//            } else {
//                mVolumeWake.setChecked(Settings.System.getInt(resolver,
//                    Settings.System.VOLUME_WAKE_SCREEN, 0) != 0);
//            }
//          int currentDefault = Settings.System.getInt(resolver, Settings.System.VOLUME_KEYS_DEFAULT,
//                  Utils.isVoiceCapable(getActivity()) ? AudioSystem.STREAM_RING : AudioSystem.STREAM_MUSIC);
//
//          if (!Utils.isVoiceCapable(getActivity())) {
//              removeListEntry(mVolumeDefault, Integer.toString(AudioSystem.STREAM_RING));
//          }
//
//          mVolumeDefault.setValue(Integer.toString(currentDefault));
//          mVolumeDefault.setOnPreferenceChangeListener(this);
//
//          mVolumeMusicControl = (CheckBoxPreference) findPreference(BUTTON_VOLUME_MUSIC_CONTROL);
//          mVolumeMusicControl.setChecked(Settings.System.getInt(resolver,
//                  Settings.System.VOLUME_MUSIC_CONTROL, 0) != 0);
//
//          mSwapVolumeButtons = (SystemCheckBoxPreference) findPreference(SWAP_VOLUME_BUTTONS);
//          mSwapVolumeButtons.setChecked(Settings.System.getInt(resolver,
//                  Settings.System.SWAP_VOLUME_BUTTONS, 0) != 0);
//
//          mVolumeKeyCursorControl = (ListPreference) findPreference(VOLUME_KEY_CURSOR_CONTROL);
//          if (mVolumeKeyCursorControl != null) {
//              mVolumeKeyCursorControl.setValue(Integer.toString(Settings.System.getInt(
//                      getContentResolver(), Settings.System.VOLUME_KEY_CURSOR_CONTROL, 0)));
//              mVolumeKeyCursorControl.setSummary(mVolumeKeyCursorControl.getEntry());
//              mVolumeKeyCursorControl.setOnPreferenceChangeListener(this);
//          }
//
//        } else {
//            prefScreen.removePreference(volumeCategory);
//        }

        final int deviceKeys = getResources().getInteger(
                com.android.internal.R.integer.config_deviceHardwareKeys);
        final boolean hasBackKey = (deviceKeys & KEY_MASK_BACK) != 0;
        final boolean hasHomeKey = (deviceKeys & KEY_MASK_HOME) != 0;
        final boolean hasMenuKey = (deviceKeys & KEY_MASK_MENU) != 0;
        final boolean hasAssistKey = (deviceKeys & KEY_MASK_ASSIST) != 0;
        final boolean hasAppSwitchKey = (deviceKeys & KEY_MASK_APP_SWITCH) != 0;

        final PreferenceCategory keysCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_KEYS);
        mKeysBackCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_BACK);
        mKeysHomeCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_HOME);
        mKeysMenuCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_MENU);
        mKeysAssistCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_ASSIST);
        mKeysAppSwitchCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_APPSWITCH);

        if (!res.getBoolean(R.bool.config_has_hardware_buttons)) {
            prefScreen.removePreference(keysCategory);
            prefScreen.removePreference(mKeysBackCategory);
            prefScreen.removePreference(mKeysHomeCategory);
            prefScreen.removePreference(mKeysMenuCategory);
            prefScreen.removePreference(mKeysAssistCategory);
            prefScreen.removePreference(mKeysAppSwitchCategory);
        } else {
            mEnableCustomBindings = (SwitchPreference) prefScreen.findPreference(
                    KEYS_ENABLE_CUSTOM);
            mBackPressAction = (ListPreference) prefScreen.findPreference(
                    KEYS_BACK_PRESS);
            mBackLongPressAction = (ListPreference) prefScreen.findPreference(
                    KEYS_BACK_LONG_PRESS);
            mHomePressAction = (ListPreference) prefScreen.findPreference(
                    KEYS_HOME_PRESS);
            mHomeLongPressAction = (ListPreference) prefScreen.findPreference(
                    KEYS_HOME_LONG_PRESS);
            mHomeDoubleTapAction = (ListPreference) prefScreen.findPreference(
                    KEYS_HOME_DOUBLE_TAP);
            mMenuPressAction = (ListPreference) prefScreen.findPreference(
                    KEYS_MENU_PRESS);
            mMenuLongPressAction = (ListPreference) prefScreen.findPreference(
                    KEYS_MENU_LONG_PRESS);
            mAssistPressAction = (ListPreference) prefScreen.findPreference(
                    KEYS_ASSIST_PRESS);
            mAssistLongPressAction = (ListPreference) prefScreen.findPreference(
                    KEYS_ASSIST_LONG_PRESS);
            mAppSwitchPressAction = (ListPreference) prefScreen.findPreference(
                    KEYS_APP_SWITCH_PRESS);
            mAppSwitchLongPressAction = (ListPreference) prefScreen.findPreference(
                    KEYS_APP_SWITCH_LONG_PRESS);
//            mVirtualKeyHapticFeedback = (CheckBoxPreference) prefScreen.findPreference(
//                    VIRTUAL_KEY_HAPTIC_FEEDBACK);
//            mForceShowOverflowMenu = (CheckBoxPreference) prefScreen.findPreference(
//                    FORCE_SHOW_OVERFLOW_MENU);
//            mEnableNavBar = (CheckBoxPreference) prefScreen.findPreference(
//                    KEYS_SHOW_NAVBAR_KEY);
//            mDisabkeHWKeys = (CheckBoxPreference) prefScreen.findPreference(
//                    KEYS_DISABLE_HW_KEY);
//            mButtonBrightness = (PreferenceScreen) prefScreen.findPreference(
//                    KEYS_BRIGHTNESS_KEY);

            if (hasBackKey) {
                int backPressAction = Settings.System.getInt(resolver,
                        Settings.System.KEY_BACK_ACTION, ACTION_BACK);

                mBackPressAction.setValue(Integer.toString(backPressAction));
                mBackPressAction.setSummary(mBackPressAction.getEntry());
                mBackPressAction.setOnPreferenceChangeListener(this);

                mKeySettings.put(Settings.System.KEY_BACK_ACTION, backPressAction);

                int backLongPressAction = Settings.System.getInt(resolver,
                        Settings.System.KEY_BACK_LONG_PRESS_ACTION, ACTION_NOTHING);

                mBackLongPressAction.setValue(Integer.toString(backLongPressAction));
                mBackLongPressAction.setSummary(mBackLongPressAction.getEntry());
                mBackLongPressAction.setOnPreferenceChangeListener(this);

                mKeySettings.put(Settings.System.KEY_BACK_LONG_PRESS_ACTION, backLongPressAction);
            } else {
                prefScreen.removePreference(mKeysBackCategory);
            }

            if (hasHomeKey) {
                int homePressAction = Settings.System.getInt(resolver,
                        Settings.System.KEY_HOME_ACTION, ACTION_HOME);

                mHomePressAction.setValue(Integer.toString(homePressAction));
                mHomePressAction.setSummary(mHomePressAction.getEntry());
                mHomePressAction.setOnPreferenceChangeListener(this);

                mKeySettings.put(Settings.System.KEY_HOME_ACTION, homePressAction);

                int homeLongPressAction;
                int longPressOnHomeBehavior = getResources().getInteger(
                        com.android.internal.R.integer.config_longPressOnHomeBehavior);

                if (longPressOnHomeBehavior == 1) {
                    longPressOnHomeBehavior = ACTION_APP_SWITCH;
                } else if (longPressOnHomeBehavior == 2) {
                    longPressOnHomeBehavior = ACTION_SEARCH;
                } else {
                    longPressOnHomeBehavior = ACTION_NOTHING;
                }

                if (hasAppSwitchKey) {
                    homeLongPressAction = Settings.System.getInt(resolver,
                            Settings.System.KEY_HOME_LONG_PRESS_ACTION, ACTION_NOTHING);
                } else {
                    int defaultAction = ACTION_NOTHING;
                    homeLongPressAction = Settings.System.getInt(resolver,
                            Settings.System.KEY_HOME_LONG_PRESS_ACTION, longPressOnHomeBehavior);
                }
                mHomeLongPressAction.setValue(Integer.toString(homeLongPressAction));
                mHomeLongPressAction.setSummary(mHomeLongPressAction.getEntry());
                mHomeLongPressAction.setOnPreferenceChangeListener(this);

                mKeySettings.put(Settings.System.KEY_HOME_LONG_PRESS_ACTION, homeLongPressAction);

                int doubleTapOnHomeBehavior = getResources().getInteger(
                        com.android.internal.R.integer.config_doubleTapOnHomeBehavior);

                int homeDoubleTapAction = Settings.System.getInt(resolver,
                            Settings.System.KEY_HOME_DOUBLE_TAP_ACTION,
                            doubleTapOnHomeBehavior == 1 ? ACTION_APP_SWITCH : ACTION_NOTHING);

                mHomeDoubleTapAction.setValue(Integer.toString(homeDoubleTapAction));
                mHomeDoubleTapAction.setSummary(mHomeDoubleTapAction.getEntry());
                mHomeDoubleTapAction.setOnPreferenceChangeListener(this);

                mKeySettings.put(Settings.System.KEY_HOME_DOUBLE_TAP_ACTION, homeDoubleTapAction);
            } else {
                prefScreen.removePreference(mKeysHomeCategory);
            }

            if (hasMenuKey) {
                int menuPressAction = Settings.System.getInt(resolver,
                        Settings.System.KEY_MENU_ACTION, ACTION_MENU);
                mMenuPressAction.setValue(Integer.toString(menuPressAction));
                mMenuPressAction.setSummary(mMenuPressAction.getEntry());
                mMenuPressAction.setOnPreferenceChangeListener(this);

                mKeySettings.put(Settings.System.KEY_MENU_ACTION, menuPressAction);

                int menuLongPressAction = ACTION_NOTHING;
                if (!hasAssistKey) {
                    menuLongPressAction = ACTION_SEARCH;
                }

                menuLongPressAction = Settings.System.getInt(resolver,
                            Settings.System.KEY_MENU_LONG_PRESS_ACTION, menuLongPressAction);

                mMenuLongPressAction.setValue(Integer.toString(menuLongPressAction));
                mMenuLongPressAction.setSummary(mMenuLongPressAction.getEntry());
                mMenuLongPressAction.setOnPreferenceChangeListener(this);

                mKeySettings.put(Settings.System.KEY_MENU_LONG_PRESS_ACTION, menuLongPressAction);
            } else {
                prefScreen.removePreference(mKeysMenuCategory);
            }

            if (hasAssistKey) {
                int assistPressAction = Settings.System.getInt(resolver,
                        Settings.System.KEY_ASSIST_ACTION, ACTION_SEARCH);
                mAssistPressAction.setValue(Integer.toString(assistPressAction));
                mAssistPressAction.setSummary(mAssistPressAction.getEntry());
                mAssistPressAction.setOnPreferenceChangeListener(this);

                mKeySettings.put(Settings.System.KEY_ASSIST_ACTION, assistPressAction);

                int assistLongPressAction = Settings.System.getInt(resolver,
                        Settings.System.KEY_ASSIST_LONG_PRESS_ACTION, ACTION_VOICE_SEARCH);
                mAssistLongPressAction.setValue(Integer.toString(assistLongPressAction));
                mAssistLongPressAction.setSummary(mAssistLongPressAction.getEntry());
                mAssistLongPressAction.setOnPreferenceChangeListener(this);

                mKeySettings.put(Settings.System.KEY_ASSIST_LONG_PRESS_ACTION, assistLongPressAction);
            } else {
                prefScreen.removePreference(mKeysAssistCategory);
            }

            if (hasAppSwitchKey) {
                int appSwitchPressAction = Settings.System.getInt(resolver,
                        Settings.System.KEY_APP_SWITCH_ACTION, ACTION_APP_SWITCH);
                mAppSwitchPressAction.setValue(Integer.toString(appSwitchPressAction));
                mAppSwitchPressAction.setSummary(mAppSwitchPressAction.getEntry());
                mAppSwitchPressAction.setOnPreferenceChangeListener(this);

                mKeySettings.put(Settings.System.KEY_APP_SWITCH_ACTION, appSwitchPressAction);

                int appSwitchLongPressAction = Settings.System.getInt(resolver,
                        Settings.System.KEY_APP_SWITCH_LONG_PRESS_ACTION, ACTION_NOTHING);
                mAppSwitchLongPressAction.setValue(Integer.toString(appSwitchLongPressAction));
                mAppSwitchLongPressAction.setSummary(mAppSwitchLongPressAction.getEntry());
                mAppSwitchLongPressAction.setOnPreferenceChangeListener(this);

                mKeySettings.put(Settings.System.KEY_APP_SWITCH_LONG_PRESS_ACTION, appSwitchLongPressAction);
            } else {
                prefScreen.removePreference(mKeysAppSwitchCategory);
            }

            mEnableCustomBindings.setChecked((Settings.System.getInt(resolver,
                    Settings.System.HARDWARE_KEY_REBINDING, 0) == 1));
            mEnableCustomBindings.setOnPreferenceChangeListener(this);

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
//            if (!mButtonBrightnessSupport) {
//                keysCategory.removePreference(mButtonBrightness);
//            }
//            updateDisableHWKeyEnablement(harwareKeysDisable);
        }

//        final PreferenceCategory headsethookCategory =
//                (PreferenceCategory) prefScreen.findPreference(CATEGORY_HEADSETHOOK);

//        mHeadsetHookLaunchVoice = (CheckBoxPreference) findPreference(BUTTON_HEADSETHOOK_LAUNCH_VOICE);
//        mHeadsetHookLaunchVoice.setChecked(Settings.System.getInt(resolver,
//                Settings.System.HEADSETHOOK_LAUNCH_VOICE, 1) == 1);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
//        if (preference == mVolumeWake) {
//            boolean checked = ((CheckBoxPreference)preference).isChecked();
//            Settings.System.putInt(getContentResolver(),
//                    Settings.System.VOLUME_WAKE_SCREEN, checked ? 1:0);
//            return true;
//        }
//        else if (preference == mHeadsetHookLaunchVoice) {
//            boolean checked = ((CheckBoxPreference)preference).isChecked();
//            Settings.System.putInt(getContentResolver(),
//                    Settings.System.HEADSETHOOK_LAUNCH_VOICE, checked ? 1:0);
//
//            return true;
//        } else if (preference == mVirtualKeyHapticFeedback) {
//            boolean checked = ((CheckBoxPreference)preference).isChecked();
//            Settings.System.putInt(getContentResolver(),
//                    Settings.System.VIRTUAL_KEYS_HAPTIC_FEEDBACK, checked ? 1:0);
//            return true;
//        } else if (preference == mVolumeMusicControl) {
//            boolean checked = ((CheckBoxPreference)preference).isChecked();
//            Settings.System.putInt(getActivity().getContentResolver(),
//                    Settings.System.VOLUME_MUSIC_CONTROL, checked ? 1:0);
//            return true;
//        } else if (preference == mForceShowOverflowMenu) {
//            boolean checked = ((CheckBoxPreference)preference).isChecked();
//            Settings.System.putInt(getContentResolver(),
//                    Settings.System.FORCE_SHOW_OVERFLOW_MENU, checked ? 1:0);
//            return true;
//        } else if (preference == mDisabkeHWKeys) {
//            boolean checked = ((CheckBoxPreference)preference).isChecked();
//            Settings.System.putInt(getContentResolver(),
//                    Settings.System.HARDWARE_KEYS_DISABLE, checked ? 1:0);
//            updateDisableHWKeyEnablement(checked);
//        } else if (preference == mSwapVolumeButtons) {
//            boolean checked = ((CheckBoxPreference)preference).isChecked();
//            Settings.System.putInt(getContentResolver(),
//                    Settings.System.SWAP_VOLUME_BUTTONS, checked ? 1:0);
//            return true;
//        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mEnableCustomBindings) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), Settings.System.HARDWARE_KEY_REBINDING,
                    value ? 1 : 0);
//            boolean harwareKeysDisable = Settings.System.getInt(getContentResolver(),
//                    Settings.System.HARDWARE_KEYS_DISABLE, 0) == 1;
//            updateDisableHWKeyEnablement(harwareKeysDisable);
            return true;
//        } else if (preference == mVolumeKeyCursorControl) {
//            String volumeKeyCursorControl = (String) newValue;
//            int val = Integer.parseInt(volumeKeyCursorControl);
//            Settings.System.putInt(getContentResolver(),
//                    Settings.System.VOLUME_KEY_CURSOR_CONTROL, val);
//            int index = mVolumeKeyCursorControl.findIndexOfValue(volumeKeyCursorControl);
//            mVolumeKeyCursorControl.setSummary(mVolumeKeyCursorControl.getEntries()[index]);
//            return true;
        } else if (preference == mBackPressAction) {
            int value = Integer.valueOf((String) newValue);
            int index = mBackPressAction.findIndexOfValue((String) newValue);
            mBackPressAction.setSummary(
                    mBackPressAction.getEntries()[index]);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.KEY_BACK_ACTION, value);
            mKeySettings.put(Settings.System.KEY_BACK_ACTION, value);
            checkForHomeKey();
            return true;
        } else if (preference == mBackLongPressAction) {
            int value = Integer.valueOf((String) newValue);
            int index = mBackLongPressAction.findIndexOfValue((String) newValue);
            mBackLongPressAction.setSummary(
                    mBackLongPressAction.getEntries()[index]);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.KEY_BACK_LONG_PRESS_ACTION, value);
            mKeySettings.put(Settings.System.KEY_BACK_LONG_PRESS_ACTION, value);
            checkForHomeKey();
            return true;
        } else if (preference == mHomePressAction) {
            int value = Integer.valueOf((String) newValue);
            int index = mHomePressAction.findIndexOfValue((String) newValue);
            mHomePressAction.setSummary(
                    mHomePressAction.getEntries()[index]);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.KEY_HOME_ACTION, value);
            mKeySettings.put(Settings.System.KEY_HOME_ACTION, value);
            checkForHomeKey();
            return true;
        } else if (preference == mHomeLongPressAction) {
            int value = Integer.valueOf((String) newValue);
            int index = mHomeLongPressAction.findIndexOfValue((String) newValue);
            mHomeLongPressAction.setSummary(
                    mHomeLongPressAction.getEntries()[index]);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.KEY_HOME_LONG_PRESS_ACTION, value);
            mKeySettings.put(Settings.System.KEY_HOME_LONG_PRESS_ACTION, value);
            checkForHomeKey();
            return true;
        } else if (preference == mHomeDoubleTapAction) {
            int value = Integer.valueOf((String) newValue);
            int index = mHomeDoubleTapAction.findIndexOfValue((String) newValue);
            mHomeDoubleTapAction.setSummary(
                    mHomeDoubleTapAction.getEntries()[index]);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.KEY_HOME_DOUBLE_TAP_ACTION, value);
            mKeySettings.put(Settings.System.KEY_HOME_DOUBLE_TAP_ACTION, value);
            checkForHomeKey();
            return true;
        } else if (preference == mMenuPressAction) {
            int value = Integer.valueOf((String) newValue);
            int index = mMenuPressAction.findIndexOfValue((String) newValue);
            mMenuPressAction.setSummary(
                    mMenuPressAction.getEntries()[index]);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.KEY_MENU_ACTION, value);
            mKeySettings.put(Settings.System.KEY_MENU_ACTION, value);
            checkForHomeKey();
            return true;
        } else if (preference == mMenuLongPressAction) {
            int value = Integer.valueOf((String) newValue);
            int index = mMenuLongPressAction.findIndexOfValue((String) newValue);
            mMenuLongPressAction.setSummary(
                    mMenuLongPressAction.getEntries()[index]);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.KEY_MENU_LONG_PRESS_ACTION, value);
            mKeySettings.put(Settings.System.KEY_MENU_LONG_PRESS_ACTION, value);
            checkForHomeKey();
            return true;
        } else if (preference == mAssistPressAction) {
            int value = Integer.valueOf((String) newValue);
            int index = mAssistPressAction.findIndexOfValue((String) newValue);
            mAssistPressAction.setSummary(
                    mAssistPressAction.getEntries()[index]);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.KEY_ASSIST_ACTION, value);
            mKeySettings.put(Settings.System.KEY_ASSIST_ACTION, value);
            checkForHomeKey();
            return true;
        } else if (preference == mAssistLongPressAction) {
            int value = Integer.valueOf((String) newValue);
            int index = mAssistLongPressAction.findIndexOfValue((String) newValue);
            mAssistLongPressAction.setSummary(
                    mAssistLongPressAction.getEntries()[index]);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.KEY_ASSIST_LONG_PRESS_ACTION, value);
            mKeySettings.put(Settings.System.KEY_ASSIST_LONG_PRESS_ACTION, value);
            checkForHomeKey();
            return true;
        } else if (preference == mAppSwitchPressAction) {
            int value = Integer.valueOf((String) newValue);
            int index = mAppSwitchPressAction.findIndexOfValue((String) newValue);
            mAppSwitchPressAction.setSummary(
                    mAppSwitchPressAction.getEntries()[index]);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.KEY_APP_SWITCH_ACTION, value);
            mKeySettings.put(Settings.System.KEY_APP_SWITCH_ACTION, value);
            checkForHomeKey();
            return true;
        } else if (preference == mAppSwitchLongPressAction) {
            int value = Integer.valueOf((String) newValue);
            int index = mAppSwitchLongPressAction.findIndexOfValue((String) newValue);
            mAppSwitchLongPressAction.setSummary(
                    mAppSwitchLongPressAction.getEntries()[index]);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.KEY_APP_SWITCH_LONG_PRESS_ACTION, value);
            mKeySettings.put(Settings.System.KEY_APP_SWITCH_LONG_PRESS_ACTION, value);
            checkForHomeKey();
            return true;
//        } else if (preference == mVolumeDefault) {
//            int value = Integer.valueOf((String) newValue);
//            Settings.System.putInt(getContentResolver(), Settings.System.VOLUME_KEYS_DEFAULT, value);
//            return true;
        }
        return false;
    }

//    private boolean hasVolumeRocker() {
//        return getActivity().getResources().getBoolean(R.bool.config_has_volume_rocker);
//    }

    private boolean hasHomeKey() {
        Iterator<Integer> nextAction = mKeySettings.values().iterator();
        while (nextAction.hasNext()) {
            int action = nextAction.next();
            if (action == ACTION_HOME) {
                return true;
            }
        }
        return false;
    }

    private void checkForHomeKey() {
        if (!hasHomeKey()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(R.string.dialog_no_home_key_title);
            alertDialogBuilder
                .setMessage(R.string.no_home_key)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    }
                  });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

//    public void removeListEntry(ListPreference list, String valuetoRemove) {
//        ArrayList<CharSequence> entries = new ArrayList<CharSequence>();
//        ArrayList<CharSequence> values = new ArrayList<CharSequence>();
//
//        for (int i = 0; i < list.getEntryValues().length; i++) {
//            if (list.getEntryValues()[i].toString().equals(valuetoRemove)) {
//                continue;
//            } else {
//                entries.add(list.getEntries()[i]);
//                values.add(list.getEntryValues()[i]);
//            }
//        }
//
//        list.setEntries(entries.toArray(new CharSequence[entries.size()]));
//        list.setEntryValues(values.toArray(new CharSequence[values.size()]));
//    }

//    private void updateDisableHWKeyEnablement(boolean harwareKeysDisable) {
//        boolean enableHWKeyRebinding = Settings.System.getInt(getContentResolver(),
//                    Settings.System.HARDWARE_KEY_REBINDING, 0) == 1;
//
//        mVirtualKeyHapticFeedback.setEnabled(!harwareKeysDisable);
//        mForceShowOverflowMenu.setEnabled(!harwareKeysDisable);
//        mButtonBrightness.setEnabled(!harwareKeysDisable);
//        mKeysHomeCategory.setEnabled(!harwareKeysDisable && enableHWKeyRebinding);
//        mKeysBackCategory.setEnabled(!harwareKeysDisable && enableHWKeyRebinding);
//        mKeysMenuCategory.setEnabled(!harwareKeysDisable && enableHWKeyRebinding);
//        mKeysAppSwitchCategory.setEnabled(!harwareKeysDisable && enableHWKeyRebinding);
//        mKeysAssistCategory.setEnabled(!harwareKeysDisable && enableHWKeyRebinding);
//    }
}
