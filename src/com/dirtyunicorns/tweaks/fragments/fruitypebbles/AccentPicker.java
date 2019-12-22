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

import static android.os.UserHandle.USER_SYSTEM;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.om.IOverlayManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.du.ThemesUtils;
import com.android.internal.util.du.Utils;

import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

import com.dirtyunicorns.tweaks.fragments.fruitypebbles.AccentPickerPreferenceController;

import java.util.List;

public class AccentPicker extends InstrumentedDialogFragment implements OnClickListener {

    private static final String TAG_ACCENT_PICKER = "accent_picker";

    private View mView;

    private IOverlayManager mOverlayManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOverlayManager = IOverlayManager.Stub.asInterface(
                ServiceManager.getService(Context.OVERLAY_SERVICE));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.accent_picker, null);

        if (mView != null) {
            initView();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mView)
                .setNegativeButton(R.string.cancel, this)
                .setNeutralButton(R.string.theme_accent_picker_default, this)
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void initView() {
        Button spaceAccent = mView.findViewById(R.id.spaceAccent);
        setAccent("com.android.theme.color.space", spaceAccent);
        setForegroundDrawable("com.android.theme.color.space", spaceAccent);

        Button purpleAccent = mView.findViewById(R.id.purpleAccent);
        setAccent("com.android.theme.color.purple", purpleAccent);
        setForegroundDrawable("com.android.theme.color.purple", purpleAccent);

        Button orchidAccent = mView.findViewById(R.id.orchidAccent);
        setAccent("com.android.theme.color.orchid", orchidAccent);
        setForegroundDrawable("com.android.theme.color.orchid", orchidAccent);

        Button oceanAccent = mView.findViewById(R.id.oceanAccent);
        setAccent("com.android.theme.color.ocean", oceanAccent);
        setForegroundDrawable("com.android.theme.color.ocean", oceanAccent);

        Button greenAccent = mView.findViewById(R.id.greenAccent);
        setAccent("com.android.theme.color.green", greenAccent);
        setForegroundDrawable("com.android.theme.color.green", greenAccent);

        Button cinnamonAccent = mView.findViewById(R.id.cinnamonAccent);
        setAccent("com.android.theme.color.cinnamon", cinnamonAccent);
        setForegroundDrawable("com.android.theme.color.cinnamon", cinnamonAccent);

        Button amberAccent = mView.findViewById(R.id.amberAccent);
        setAccent("com.android.theme.color.amber", amberAccent);
        setForegroundDrawable("com.android.theme.color.amber", amberAccent);

        Button blueAccent = mView.findViewById(R.id.blueAccent);
        setAccent("com.android.theme.color.blue", blueAccent);
        setForegroundDrawable("com.android.theme.color.blue", blueAccent);

        Button blueGreyAccent = mView.findViewById(R.id.blueGreyAccent);
        setAccent("com.android.theme.color.bluegrey", blueGreyAccent);
        setForegroundDrawable("com.android.theme.color.bluegrey", blueGreyAccent);

        Button brownAccent = mView.findViewById(R.id.brownAccent);
        setAccent("com.android.theme.color.brown", brownAccent);
        setForegroundDrawable("com.android.theme.color.brown", brownAccent);

        Button cyanAccent = mView.findViewById(R.id.cyanAccent);
        setAccent("com.android.theme.color.cyan", cyanAccent);
        setForegroundDrawable("com.android.theme.color.cyan", cyanAccent);

        Button deepOrangeAccent = mView.findViewById(R.id.deepOrangeAccent);
        setAccent("com.android.theme.color.deeporange", deepOrangeAccent);
        setForegroundDrawable("com.android.theme.color.deeporange", deepOrangeAccent);

        Button deepPurpleAccent = mView.findViewById(R.id.deepPurpleAccent);
        setAccent("com.android.theme.color.deeppurple", deepPurpleAccent);
        setForegroundDrawable("com.android.theme.color.deeppurple", deepPurpleAccent);

        Button greyAccent = mView.findViewById(R.id.greyAccent);
        setAccent("com.android.theme.color.grey", greyAccent);
        setForegroundDrawable("com.android.theme.color.grey", greyAccent);

        Button indigoAccent = mView.findViewById(R.id.indigoAccent);
        setAccent("com.android.theme.color.indigo", indigoAccent);
        setForegroundDrawable("com.android.theme.color.indigo", indigoAccent);

        Button lightBlueAccent = mView.findViewById(R.id.lightBlueAccent);
        setAccent("com.android.theme.color.lightblue", lightBlueAccent);
        setForegroundDrawable("com.android.theme.color.lightblue", lightBlueAccent);

        Button lightGreenAccent = mView.findViewById(R.id.lightGreenAccent);
        setAccent("com.android.theme.color.lightgreen", lightGreenAccent);
        setForegroundDrawable("com.android.theme.color.lightgreen", lightGreenAccent);

        Button limeAccent = mView.findViewById(R.id.limeAccent);
        setAccent("com.android.theme.color.lime", limeAccent);
        setForegroundDrawable("com.android.theme.color.lime", limeAccent);

        Button orangeAccent = mView.findViewById(R.id.orangeAccent);
        setAccent("com.android.theme.color.orange", orangeAccent);
        setForegroundDrawable("com.android.theme.color.orange", orangeAccent);

        Button pinkAccent = mView.findViewById(R.id.pinkAccent);
        setAccent("com.android.theme.color.pink", pinkAccent);
        setForegroundDrawable("com.android.theme.color.pink", pinkAccent);

        Button redAccent = mView.findViewById(R.id.redAccent);
        setAccent("com.android.theme.color.red", redAccent);
        setForegroundDrawable("com.android.theme.color.red", redAccent);

        Button tealAccent = mView.findViewById(R.id.tealAccent);
        setAccent("com.android.theme.color.teal", tealAccent);
        setForegroundDrawable("com.android.theme.color.teal", tealAccent);

        Button yellowAccent = mView.findViewById(R.id.yellowAccent);
        setAccent("com.android.theme.color.yellow", yellowAccent);
        setForegroundDrawable("com.android.theme.color.yellow", yellowAccent);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == AlertDialog.BUTTON_NEGATIVE) {
            dismiss();
        }
        if (which == AlertDialog.BUTTON_NEUTRAL) {
            for (int i = 0; i < ThemesUtils.ACCENTS.length; i++) {
                String accent = ThemesUtils.ACCENTS[i];
                try {
                    mOverlayManager.setEnabled(accent, false, USER_SYSTEM);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            dismiss();
        }
    }

    public static void show(Fragment parent) {
        if (!parent.isAdded()) return;

        final AccentPicker dialog = new AccentPicker();
        dialog.setTargetFragment(parent, 0);
        dialog.show(parent.getFragmentManager(), TAG_ACCENT_PICKER);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DIRTYTWEAKS;
    }

    private void setForegroundDrawable(String packagename, Button buttonAccent) {
        if (Utils.isThemeEnabled(packagename)) {
            buttonAccent.setForeground(getActivity().getResources().getDrawable(
                    R.drawable.accent_picker_checkmark, null));
        } else {
            buttonAccent.setForeground(null);
        }
    }

    private void setAccent(final String accent, final Button buttonAccent) {
        if (buttonAccent != null) {
            buttonAccent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        for (int i = 0; i < ThemesUtils.ACCENTS.length; i++) {
                            String accent = ThemesUtils.ACCENTS[i];
                            try {
                                mOverlayManager.setEnabled(accent, false, USER_SYSTEM);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        mOverlayManager.setEnabled(accent, true, USER_SYSTEM);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    dismiss();
                }
            });
        }
    }
}
