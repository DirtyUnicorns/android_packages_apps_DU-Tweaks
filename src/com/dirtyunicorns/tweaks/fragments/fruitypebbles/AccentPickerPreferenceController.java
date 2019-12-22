/*
 * Copyright (C) 2018-2019 The Dirty Unicorns Project
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

package com.dirtyunicorns.tweaks.fragments.fruitypebbles;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceScreen;

import com.android.internal.util.du.Utils;

import com.android.settings.core.PreferenceControllerMixin;

import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnResume;

import com.dirtyunicorns.tweaks.fragments.fruitypebbles.AccentPicker;

import java.util.List;

public class AccentPickerPreferenceController extends AbstractPreferenceController
        implements PreferenceControllerMixin, LifecycleObserver, OnResume {

    private static final String KEY_ACCENT_PICKER_FRAGMENT_PREF = "accent_picker";

    private Context mContext;
    private final Fragment mParent;
    private Preference mAccentPickerPref;

    public AccentPickerPreferenceController(Context context, Lifecycle lifecycle, Fragment parent) {
        super(context);
        mParent = parent;
        mContext = context;
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        mAccentPickerPref  = (Preference) screen.findPreference(KEY_ACCENT_PICKER_FRAGMENT_PREF);
        mAccentPickerPref.setEnabled(true);
    }

    @Override
    public void onResume() {
        updateEnableState();
        updateSummary();
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getPreferenceKey() {
        return KEY_ACCENT_PICKER_FRAGMENT_PREF;
    }

    private void updateEnableState() {
        if (mAccentPickerPref == null) {
            return;
        }

        mAccentPickerPref.setOnPreferenceClickListener(
                new OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        AccentPicker.show(mParent);
                        return true;
                    }
                });
    }

    private void updateSummary() {
        if (mAccentPickerPref != null) {
            if (Utils.isThemeEnabled("com.android.theme.color.space")) {
                mAccentPickerPref.setSummary("Space");
            } else if (Utils.isThemeEnabled("com.android.theme.color.purple")) {
                mAccentPickerPref.setSummary("Purple");
            } else if (Utils.isThemeEnabled("com.android.theme.color.orchid")) {
                mAccentPickerPref.setSummary("Orchid");
            } else if (Utils.isThemeEnabled("com.android.theme.color.ocean")) {
                mAccentPickerPref.setSummary("Ocean");
            } else if (Utils.isThemeEnabled("com.android.theme.color.green")) {
                mAccentPickerPref.setSummary("Green");
            } else if (Utils.isThemeEnabled("com.android.theme.color.cinnamon")) {
                mAccentPickerPref.setSummary("Cinnamon");
            } else if (Utils.isThemeEnabled("com.android.theme.color.amber")) {
                mAccentPickerPref.setSummary("Amber");
            } else if (Utils.isThemeEnabled("com.android.theme.color.blue")) {
                mAccentPickerPref.setSummary("Blue");
            } else if (Utils.isThemeEnabled("com.android.theme.color.bluegrey")) {
                mAccentPickerPref.setSummary("Blue Grey");
            } else if (Utils.isThemeEnabled("com.android.theme.color.brown")) {
                mAccentPickerPref.setSummary("Brown");
            } else if (Utils.isThemeEnabled("com.android.theme.color.cyan")) {
                mAccentPickerPref.setSummary("Cyan");
            } else if (Utils.isThemeEnabled("com.android.theme.color.deeporange")) {
                mAccentPickerPref.setSummary("Deep Orange");
            } else if (Utils.isThemeEnabled("com.android.theme.color.deeppurple")) {
                mAccentPickerPref.setSummary("Deep Purple");
            } else if (Utils.isThemeEnabled("com.android.theme.color.grey")) {
                mAccentPickerPref.setSummary("Grey");
            } else if (Utils.isThemeEnabled("com.android.theme.color.indigo")) {
                mAccentPickerPref.setSummary("Indigo");
            } else if (Utils.isThemeEnabled("com.android.theme.color.lightblue")) {
                mAccentPickerPref.setSummary("Light Blue");
            } else if (Utils.isThemeEnabled("com.android.theme.color.lightgreen")) {
                mAccentPickerPref.setSummary("Light Green");
            } else if (Utils.isThemeEnabled("com.android.theme.color.lime")) {
                mAccentPickerPref.setSummary("Lime");
            } else if (Utils.isThemeEnabled("com.android.theme.color.orange")) {
                mAccentPickerPref.setSummary("Orange");
            } else if (Utils.isThemeEnabled("com.android.theme.color.pink")) {
                mAccentPickerPref.setSummary("Pink");
            } else if (Utils.isThemeEnabled("com.android.theme.color.red")) {
                mAccentPickerPref.setSummary("Red");
            } else if (Utils.isThemeEnabled("com.android.theme.color.teal")) {
                mAccentPickerPref.setSummary("Teal");
            } else if (Utils.isThemeEnabled("com.android.theme.color.yellow")) {
                mAccentPickerPref.setSummary("Yellow");
            } else {
                mAccentPickerPref.setSummary(mContext.getString(
                        com.android.settings.R.string.default_theme));
            }
        }
    }
}
