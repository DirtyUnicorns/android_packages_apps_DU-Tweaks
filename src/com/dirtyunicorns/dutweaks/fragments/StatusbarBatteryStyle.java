/*
 *  Copyright (C) 2016 Dirty Unicorns
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
package com.dirtyunicorns.dutweaks.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v14.preference.SwitchPreference;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.Utils;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import java.util.List;
import java.util.ArrayList;

public class StatusbarBatteryStyle extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {
    private static final String TAG = "StatusbarBatteryStyle";

    private static final String STATUS_BAR_BATTERY_STYLE = "status_bar_battery_style";
    private static final String STATUS_BAR_SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";
    private static final String BATTERY_TILE_STYLE = "battery_tile_style";
    private static final String FORCE_CHARGE_BATTERY_TEXT = "force_charge_battery_text";
    private static final String TEXT_CHARGING_SYMBOL = "text_charging_symbol";

    private static final int STATUS_BAR_BATTERY_STYLE_PORTRAIT = 0;
    private static final int STATUS_BAR_BATTERY_STYLE_HIDDEN = 4;
    private static final int STATUS_BAR_BATTERY_STYLE_TEXT = 6;

    private int mBatteryTileStyleValue;
    private int mStatusBarBatteryValue;
    private int mStatusBarBatteryShowPercentValue;
    private int mTextChargingSymbolValue;

    private ListPreference mBatteryTileStyle;
    private ListPreference mStatusBarBattery;
    private ListPreference mStatusBarBatteryShowPercent;
    private ListPreference mTextChargingSymbol;
    private SwitchPreference mForceChargeBatteryText;

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.DIRTYTWEAKS;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.statusbar_battery_style);

        ContentResolver resolver = getActivity().getContentResolver();

        mForceChargeBatteryText = (SwitchPreference) findPreference(FORCE_CHARGE_BATTERY_TEXT);
        mForceChargeBatteryText.setChecked((Settings.Secure.getInt(resolver,
                Settings.Secure.FORCE_CHARGE_BATTERY_TEXT, 0) == 1));
        mForceChargeBatteryText.setOnPreferenceChangeListener(this);

        mStatusBarBattery = (ListPreference) findPreference(STATUS_BAR_BATTERY_STYLE);
        mStatusBarBatteryValue = Settings.Secure.getInt(resolver,
                Settings.Secure.STATUS_BAR_BATTERY_STYLE, 0);
        mStatusBarBattery.setValue(Integer.toString(mStatusBarBatteryValue));
        mStatusBarBattery.setSummary(mStatusBarBattery.getEntry());
        mStatusBarBattery.setOnPreferenceChangeListener(this);

        mStatusBarBatteryShowPercent =
                (ListPreference) findPreference(STATUS_BAR_SHOW_BATTERY_PERCENT);
        mStatusBarBatteryShowPercentValue = Settings.Secure.getInt(resolver,
                Settings.Secure.STATUS_BAR_SHOW_BATTERY_PERCENT, 0);
        mStatusBarBatteryShowPercent.setValue(Integer.toString(mStatusBarBatteryShowPercentValue));
        mStatusBarBatteryShowPercent.setSummary(mStatusBarBatteryShowPercent.getEntry());
        mStatusBarBatteryShowPercent.setOnPreferenceChangeListener(this);

        mTextChargingSymbol = (ListPreference) findPreference(TEXT_CHARGING_SYMBOL);
        mTextChargingSymbolValue = Settings.Secure.getInt(resolver,
                Settings.Secure.TEXT_CHARGING_SYMBOL, 0);
        mTextChargingSymbol.setValue(Integer.toString(mTextChargingSymbolValue));
        mTextChargingSymbol.setSummary(mTextChargingSymbol.getEntry());
        mTextChargingSymbol.setOnPreferenceChangeListener(this);

        mBatteryTileStyle = (ListPreference) findPreference(BATTERY_TILE_STYLE);
        mBatteryTileStyleValue = Settings.Secure.getInt(resolver,
                Settings.Secure.BATTERY_TILE_STYLE, 0);
        mBatteryTileStyle.setValue(Integer.toString(mBatteryTileStyleValue));
        mBatteryTileStyle.setSummary(mBatteryTileStyle.getEntry());
        mBatteryTileStyle.setOnPreferenceChangeListener(this);

        enableStatusBarBatteryDependents();
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mStatusBarBattery) {
            mStatusBarBatteryValue = Integer.valueOf((String) newValue);
            int index = mStatusBarBattery.findIndexOfValue((String) newValue);
            mStatusBarBattery.setSummary(
                    mStatusBarBattery.getEntries()[index]);
            Settings.Secure.putInt(resolver,
                    Settings.Secure.STATUS_BAR_BATTERY_STYLE, mStatusBarBatteryValue);
            enableStatusBarBatteryDependents();
            return true;
        } else if (preference == mStatusBarBatteryShowPercent) {
            mStatusBarBatteryShowPercentValue = Integer.valueOf((String) newValue);
            int index = mStatusBarBatteryShowPercent.findIndexOfValue((String) newValue);
            mStatusBarBatteryShowPercent.setSummary(
                    mStatusBarBatteryShowPercent.getEntries()[index]);
            Settings.Secure.putInt(resolver,
                    Settings.Secure.STATUS_BAR_SHOW_BATTERY_PERCENT, mStatusBarBatteryShowPercentValue);
            enableStatusBarBatteryDependents();
            return true;
        } else if  (preference == mForceChargeBatteryText) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.Secure.putInt(resolver,
                    Settings.Secure.FORCE_CHARGE_BATTERY_TEXT, checked ? 1:0);
            return true;
        } else if (preference == mTextChargingSymbol) {
            mTextChargingSymbolValue = Integer.valueOf((String) newValue);
            int index = mTextChargingSymbol.findIndexOfValue((String) newValue);
            mTextChargingSymbol.setSummary(
                    mTextChargingSymbol.getEntries()[index]);
            Settings.Secure.putInt(resolver,
                    Settings.Secure.TEXT_CHARGING_SYMBOL, mTextChargingSymbolValue);
            return true;
        } else if (preference == mBatteryTileStyle) {
            mBatteryTileStyleValue = Integer.valueOf((String) newValue);
            int index = mBatteryTileStyle.findIndexOfValue((String) newValue);
            mBatteryTileStyle.setSummary(
                    mBatteryTileStyle.getEntries()[index]);
            Settings.Secure.putInt(resolver,
                    Settings.Secure.BATTERY_TILE_STYLE, mBatteryTileStyleValue);
            return true;
        }
        return false;
    }

    private void enableStatusBarBatteryDependents() {
        if (mStatusBarBatteryValue == STATUS_BAR_BATTERY_STYLE_HIDDEN) {
            mStatusBarBatteryShowPercent.setEnabled(false);
            mForceChargeBatteryText.setEnabled(false);
            mTextChargingSymbol.setEnabled(false);
        } else if (mStatusBarBatteryValue == STATUS_BAR_BATTERY_STYLE_TEXT) {
            mStatusBarBatteryShowPercent.setEnabled(false);
            mForceChargeBatteryText.setEnabled(false);
            mTextChargingSymbol.setEnabled(true);
        } else if (mStatusBarBatteryValue == STATUS_BAR_BATTERY_STYLE_PORTRAIT) {
            mStatusBarBatteryShowPercent.setEnabled(true);
            mForceChargeBatteryText.setEnabled(mStatusBarBatteryShowPercentValue == 2 ? false : true);
            mTextChargingSymbol.setEnabled(true);
        } else {
            mStatusBarBatteryShowPercent.setEnabled(true);
            mForceChargeBatteryText.setEnabled(mStatusBarBatteryShowPercentValue == 2 ? false : true);
            mTextChargingSymbol.setEnabled(true);
        }
    }

    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();

                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.statusbar_battery_style;
                    result.add(sir);

                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    ArrayList<String> result = new ArrayList<String>();
                    return result;
                }
            };
}
