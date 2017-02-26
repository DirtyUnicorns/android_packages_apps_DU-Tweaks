package com.dirtyunicorns.dutweaks.fragments;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;

import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;

public class LEDsettings extends SettingsPreferenceFragment {

    private Preference mChargingLeds;
    private Preference mNotificationLeds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.ledsettings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mChargingLeds = (Preference) findPreference("charging_light");
        mNotificationLeds = (Preference) findPreference("notification_light");

        if (mChargingLeds != null
                && !getResources().getBoolean(
                        com.android.internal.R.bool.config_intrusiveBatteryLed)) {
            prefSet.removePreference(mChargingLeds);
        }
        if (mNotificationLeds != null
                && !getResources().getBoolean(
                        com.android.internal.R.bool.config_intrusiveNotificationLed)) {
            prefSet.removePreference(mNotificationLeds);
        }
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.DIRTYTWEAKS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
