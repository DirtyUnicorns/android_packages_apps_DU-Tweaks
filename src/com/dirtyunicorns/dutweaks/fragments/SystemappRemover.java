/*=========================================================================
 *
 *  PROJECT:  SlimRoms
 *            Team Slimroms (http://www.slimroms.net)
 *
 *  COPYRIGHT Copyright (C) 2013 Slimroms http://www.slimroms.net
 *            Copyright (C) 2014 Dirty Unicorns
 *            All rights reserved
 *
 *  LICENSE   http://www.gnu.org/licenses/gpl-2.0.html GNU/GPL
 *
 *  AUTHORS:     fronti90
 *  DESCRIPTION: SlimSizer: manage your apps
 *
 *  MODS: Dirty Unicorns
 *        Team D.I.R.T.
 *        Added priv-app and odex files support
 *
 *=========================================================================
 */
package com.dirtyunicorns.dutweaks.fragments;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ListIterator;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.settings.R;

public class SystemappRemover extends Fragment {
    private final int DELETE_DIALOG = 1;

    protected ArrayAdapter<String> adapter;
    private ArrayList<String> mSysApp;
    public final String systemPath = "/system/app/";
    public final String systemPrivPath = "/system/priv-app/";
    protected Process superUser;
    protected DataOutputStream dos;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_remover, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageButton fabButton = (ImageButton) getView().findViewById(R.id.fab_delete);

        final ArrayList<String> safetyList = new ArrayList<String>();
        safetyList.add("CertInstaller.apk");
        safetyList.add("DrmProvider.apk");
        safetyList.add("PackageInstaller.apk");
        safetyList.add("TelephonyProvider.apk");
        safetyList.add("ContactsProvider.apk");
        safetyList.add("DefaultContainerService.apk");
        safetyList.add("Dialer.apk");
        safetyList.add("DownloadProvider.apk");
        safetyList.add("FusedLocation.apk");
        safetyList.add("Keyguard.apk");
        safetyList.add("MediaProvider.apk");
        safetyList.add("ProxyHandler.apk");
        safetyList.add("Settings.apk");
        safetyList.add("SettingsProvider.apk");
        safetyList.add("SystemUI.apk");
        safetyList.add("TeleService.apk");

        File system = new File(systemPath);
        File systemPriv = new File(systemPrivPath);
        String[] sysappArray = combine(system.list(), systemPriv.list());
        mSysApp = new ArrayList<String>(
                Arrays.asList(sysappArray));
        filterOdex();

        mSysApp.removeAll(safetyList);
        Collections.sort(mSysApp);

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice, mSysApp);

        final ListView lv = (ListView) getView().findViewById(R.string.listsystem);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(adapter);

        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.empty_list_entry_footer, lv, false);
        lv.addFooterView(footer);
        lv.setFooterDividersEnabled(false);
        footer.setOnClickListener(null);

        fabButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String item = null;
                SparseBooleanArray checked = lv.getCheckedItemPositions();
                for (int i = lv.getCount() - 1; i > 0; i--) {
                    if (checked.get(i)) {
                        item = mSysApp.get(i);
                    }
                }
                if (item == null) {
                    toast(getResources().getString(
                            R.string.message_noselect));
                    return;
                } else {
                    showDialog(DELETE_DIALOG, item, adapter);
                }
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void toast(String text) {
        Toast toast = Toast.makeText(getView().getContext(), text,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showDialog(int id, final String item,
                            final ArrayAdapter<String> adapter) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        if (id == DELETE_DIALOG) {
            alert.setMessage(R.string.message_delete)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    final ListView lv = (ListView) getView().findViewById(R.string.listsystem);
                                    ArrayList<String> itemsList = new ArrayList<String>();
                                    SparseBooleanArray checked = lv.getCheckedItemPositions();
                                    for (int i = lv.getCount() - 1; i > 0; i--) {
                                        if (checked.get(i)) {
                                            String appName = mSysApp.get(i);
                                            itemsList.add(appName);
                                            lv.setItemChecked(i, false);
                                            adapter.remove(appName);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                    new SystemappRemover.Deleter().execute(itemsList.toArray(new String[itemsList.size()]));
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
        }
        alert.show();
    }

    private String[] combine(String[] a, String[] b) {
        int length = a.length + b.length;
        String[] result = new String[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    private void filterOdex() {
        ListIterator<String> it = mSysApp.listIterator();
        while ( it.hasNext() ) {
            String str = it.next();
            if ( str.endsWith(".odex") ) {
                it.remove();
            }
        }
    }

    public class Deleter extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dos == null) {
                try {
                    superUser = new ProcessBuilder("su", "-c", "/system/xbin/ash").start();
                    dos = new DataOutputStream(superUser.getOutputStream());
                    dos.writeBytes("\n" + "mount -o remount,rw /system" + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        protected Void doInBackground(String... params) {
            for (String appName : params) {
                String odexAppName = appName.replaceAll(".apk$", ".odex");
                String basePath = systemPath;
                File app = new File(systemPath);

                if( ! app.exists() )
                    basePath = systemPrivPath;

                try {
                    dos.writeBytes("\n" + "rm -rf '" + basePath + "*" + appName + "'\n");
                    File odex = new File(basePath + odexAppName);
                    if( odex.exists() )
                        dos.writeBytes("\n" + "rm -rf '" + basePath + odexAppName + "'\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            super.onPreExecute();
            try {
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
