/*
 * Copyright (C) 2016 The DirtyUnicorns Project
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
 *
 * Display installed icons packs that we are able to parse and get an icon from
 */

package com.dirtyunicorns.dutweaks;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.android.settings.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class IconPickerActivity extends Activity {
    private static final int DIALOG_ICON_PACK = 1;
    private static final int ICON_PACK_ICON_RESULT = 69;
    public static final String INTENT_ICON_PICKER = "intent_icon_picker";
    IconPackageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new IconPackageAdapter(this);
        mAdapter.load();
        createDialog(this, DIALOG_ICON_PACK).show();
    }

    public Dialog createDialog(final Context context, final int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Dialog dialog;

        final DialogInterface.OnClickListener l = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (id) {
                    case DIALOG_ICON_PACK:
                        ResolveInfo info = mAdapter.getItem(item);
                        String packageName = info.activityInfo.packageName;
                        Intent intent = new Intent();
                        intent.setClassName("com.android.settings", "com.dirtyunicorns.dutweaks.IconPackGridActivity");
                        intent.putExtra("icon_package_name", packageName);
                        dialog.dismiss();
                        startActivityForResult(intent, ICON_PACK_ICON_RESULT);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid dialog type "
                                + id + " in IconPackIconPickerActivity dialog.");
                }
            }
        };

        final DialogInterface.OnCancelListener cancel = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                sendCancelResultAndFinish();
            }
        };

        final DialogInterface.OnClickListener cancelClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                cancel.onCancel(dialog);
            }
        };

        switch (id) {
            case DIALOG_ICON_PACK:
                dialog = builder.setTitle(getString(R.string.icon_pack_picker_dialog_title))
                        .setAdapter(mAdapter, l)
                        .setOnCancelListener(cancel)
                        .setNegativeButton(getString(android.R.string.cancel), cancelClickListener)
                        .create();
                break;
            default:
                throw new IllegalArgumentException("Invalid dialog type "
                        + id + " in ActionPicker dialog.");
        }

        return dialog;
    }

    private void sendCancelResultAndFinish() {
        Intent intent = new Intent(INTENT_ICON_PICKER);
        intent.putExtra("result", Activity.RESULT_CANCELED);
        sendBroadcastAsUser(intent, UserHandle.CURRENT);
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ICON_PACK_ICON_RESULT) {
            if (resultCode == RESULT_OK) {
                String iconType = data.getStringExtra("icon_data_type");
                String iconPackage = data.getStringExtra("icon_data_package");
                String iconName = data.getStringExtra("icon_data_name");
                Intent resultIntent = new Intent();
                resultIntent.setAction(INTENT_ICON_PICKER);
                resultIntent.putExtra("result", Activity.RESULT_OK);
                resultIntent.putExtra("icon_data_type", iconType);
                resultIntent.putExtra("icon_data_package", iconPackage);
                resultIntent.putExtra("icon_data_name", iconName);
                sendBroadcastAsUser(resultIntent, UserHandle.CURRENT);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                sendCancelResultAndFinish();
            }
        }
    }

    private class IconPackageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private PackageManager mPM;
        List<ResolveInfo> mIconPackage = new ArrayList<ResolveInfo>();

        public IconPackageAdapter(Context ctx) {
            mInflater = LayoutInflater.from(ctx);
            mPM = ctx.getPackageManager();
        }

        public void load() {
            List<ResolveInfo> tmpList = new ArrayList<ResolveInfo>();

            Intent i = new Intent("org.adw.launcher.THEMES");
            i.addCategory(Intent.CATEGORY_DEFAULT);
            tmpList.addAll(mPM.queryIntentActivities(i,
                    PackageManager.GET_META_DATA));

            /*
            i = new Intent(Intent.ACTION_MAIN);
            i.addCategory("com.anddoes.launcher.THEME");
            tmpList.addAll(mPM.queryIntentActivities(i,
                    PackageManager.GET_META_DATA));

            i = new Intent("com.dlto.atom.launcher.THEME");
            tmpList.addAll(mPM.queryIntentActivities(i,
                    PackageManager.GET_META_DATA));

            i = new Intent("com.gtp.nextlauncher.theme");
            i.addCategory(Intent.CATEGORY_DEFAULT);
            tmpList.addAll(mPM.queryIntentActivities(i,
                    PackageManager.GET_META_DATA));

            i = new Intent("com.tsf.shell.themes");
            i.addCategory(Intent.CATEGORY_DEFAULT);
            tmpList.addAll(mPM.queryIntentActivities(i,
                    PackageManager.GET_META_DATA));

            i = new Intent("com.phonemetra.turbo.launcher.icons.ACTION_PICK_ICON");
            i.addCategory(Intent.CATEGORY_DEFAULT);
            tmpList.addAll(mPM.queryIntentActivities(i,
                    PackageManager.GET_META_DATA));

            i = new Intent("ginlemon.smartlauncher.THEMES");
            i.addCategory(Intent.CATEGORY_DEFAULT);
            tmpList.addAll(mPM.queryIntentActivities(i,
                    PackageManager.GET_META_DATA));

            i = new Intent("com.gridappsinc.launcher.theme.apk_action");
            i.addCategory(Intent.CATEGORY_DEFAULT);
            tmpList.addAll(mPM.queryIntentActivities(i,
                    PackageManager.GET_META_DATA));
                                
            i = new Intent("com.gau.go.launcherex.theme");
            tmpList.addAll(mPM.queryIntentActivities(i,
                    PackageManager.GET_META_DATA));

            i = new Intent(Intent.ACTION_MAIN);
            i.addCategory("com.teslacoilsw.launcher.THEME");
            tmpList.addAll(mPM.queryIntentActivities(i,
                    PackageManager.GET_META_DATA));

            i = new Intent(Intent.ACTION_MAIN);
            i.addCategory("com.fede.launcher.THEME_ICONPACK");
            tmpList.addAll(mPM.queryIntentActivities(i,
                    PackageManager.GET_META_DATA));
            
            for (ResolveInfo inf : tmpList) {
                String s = inf.activityInfo.packageName;
                Log.i("IconPackIconPicker", "IconPackIconPicker resolved " + s);
            } */

            mIconPackage.clear();
            mIconPackage.addAll(tmpList);

            
            List<ResolveInfo> toRemove = new ArrayList<ResolveInfo>();
            for (ResolveInfo info : mIconPackage) {
                String packageName = info.activityInfo.packageName;
                Resources packageRes;
                try {
                    packageRes = mPM.getResourcesForApplication(packageName);
                    InputStream appfilterstream = packageRes.getAssets().open("drawable.xml");
                    appfilterstream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    toRemove.add(info);
                }
            }
            mIconPackage.removeAll(toRemove);

            Collections.sort(mIconPackage, new Comparator<ResolveInfo>() {
                @Override
                public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                    return (lhs.loadLabel(mPM).toString()).compareTo(rhs.loadLabel(mPM).toString());
                }

            });
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mIconPackage.size();
        }

        @Override
        public ResolveInfo getItem(int position) {
            return mIconPackage.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.preference_icon, null, false);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.title = (TextView) convertView.findViewById(com.android.internal.R.id.title);
                holder.summary = (TextView) convertView
                        .findViewById(com.android.internal.R.id.summary);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            }

            ResolveInfo info = (ResolveInfo) getItem(position);
            holder.title.setText(info.loadLabel(mPM).toString());
            holder.icon.setImageDrawable(info.loadIcon(mPM));
            holder.summary.setVisibility(View.GONE);

            return convertView;
        }
    }

    private static class ViewHolder {
        TextView title;
        TextView summary;
        ImageView icon;
    }

}
