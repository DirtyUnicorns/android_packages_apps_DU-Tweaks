/*
 * Copyright (C) 2018 The Dirty Unicorns Project
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

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.om.IOverlayManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.statusbar.ThemeAccentUtils;

import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class QsTileStyles extends InstrumentedDialogFragment implements OnClickListener {

    private static final String TAG_QS_TILE_STYLES = "qs_tile_style";

    private View mView;

    private IOverlayManager mOverlayManager;
    private int mCurrentUserId;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOverlayManager = IOverlayManager.Stub.asInterface(
                ServiceManager.getService(Context.OVERLAY_SERVICE));
        mCurrentUserId = ActivityManager.getCurrentUser();
        mContext = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.qs_styles_main, null);

        if (mView != null) {
            initView();
            setAlpha(mContext.getResources());
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
        LinearLayout square = mView.findViewById(R.id.QsTileStyleSquare);
        setLayout("1", square);

        LinearLayout roundedsquare = mView.findViewById(R.id.QsTileStyleRoundedSquare);
        setLayout("2", roundedsquare);

        LinearLayout squircle = mView.findViewById(R.id.QsTileStyleSquircle);
        setLayout("3", squircle);

        LinearLayout teardrop = mView.findViewById(R.id.QsTileStyleTearDrop);
        setLayout("4", teardrop);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (which == AlertDialog.BUTTON_NEGATIVE) {
            dismiss();
        }
        if (which == AlertDialog.BUTTON_NEUTRAL) {
            Settings.System.putIntForUser(resolver,
                    Settings.System.QS_TILE_STYLE, 0, mCurrentUserId);
            dismiss();
        }
    }

    public static void show(Fragment parent) {
        if (!parent.isAdded()) return;

        final QsTileStyles dialog = new QsTileStyles();
        dialog.setTargetFragment(parent, 0);
        dialog.show(parent.getFragmentManager(), TAG_QS_TILE_STYLES);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DIRTYTWEAKS;
    }

    private void setLayout(final String style, final LinearLayout layout) {
        final ContentResolver resolver = getActivity().getContentResolver();
        if (layout != null) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.QS_TILE_STYLE, Integer.parseInt(style), mCurrentUserId);
                    dismiss();
                }
            });
        }
    }

    private void setAlpha(Resources res) {
        LinearLayout square = mView.findViewById(R.id.QsTileStyleSquare);
        LinearLayout roundedsquare = mView.findViewById(R.id.QsTileStyleRoundedSquare);
        LinearLayout squircle = mView.findViewById(R.id.QsTileStyleSquircle);
        LinearLayout teardrop = mView.findViewById(R.id.QsTileStyleTearDrop);

        TypedValue typedValue = new TypedValue();
        res.getValue(R.dimen.qs_styles_layout_opacity, typedValue, true);
        float mLayoutOpacity = typedValue.getFloat();

        if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 1 )) {
            square.setAlpha((float) 1.0);
            roundedsquare.setAlpha(mLayoutOpacity);
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 2 )) {
            square.setAlpha(mLayoutOpacity);
            roundedsquare.setAlpha((float) 1.0);
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 3 )) {
            square.setAlpha(mLayoutOpacity);
            roundedsquare.setAlpha(mLayoutOpacity);
            squircle.setAlpha((float) 1.0);
            teardrop.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 4 )) {
            square.setAlpha(mLayoutOpacity);
            roundedsquare.setAlpha(mLayoutOpacity);
            squircle.setAlpha(mLayoutOpacity);
            teardrop.setAlpha((float) 1.0);
        } else {
            square.setAlpha((float) 1.0);
            roundedsquare.setAlpha((float) 1.0);
            squircle.setAlpha((float) 1.0);
            teardrop.setAlpha((float) 1.0);
        }
    }
}
