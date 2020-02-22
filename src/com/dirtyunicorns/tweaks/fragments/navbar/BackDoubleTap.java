package com.dirtyunicorns.tweaks.fragments.navbar;

import android.provider.Settings;
import android.view.View;
import android.widget.ListView;

import com.dirtyunicorns.support.preferences.AppPicker;

public class BackDoubleTap extends AppPicker {

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String mAppString = applist.get(position).packageName;
        String mFriendlyAppString = (String) applist.get(position).loadLabel(packageManager);

        Settings.System.putString(
                getContentResolver(), Settings.System.KEY_BACK_DOUBLE_TAP_CUSTOM_APP, mAppString);
        Settings.System.putString(
                getContentResolver(), Settings.System.KEY_BACK_DOUBLE_TAP_CUSTOM_APP_FR_NAME,
                mFriendlyAppString);
        finish();
    }
}
