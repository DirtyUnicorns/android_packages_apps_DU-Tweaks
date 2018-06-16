/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.dirtyunicorns.tweaks.preferences;

import android.app.ActivityManager;
import android.content.Context;
import android.content.om.IOverlayManager;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.ServiceManager;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.android.settings.R;
import com.android.settings.Utils;

import com.android.internal.statusbar.ThemeAccentUtils;

public class CategoryPreference extends Preference {

    private final View.OnClickListener mClickListener = v -> performClick(v);

    private boolean mAllowDividerAbove;
    private boolean mAllowDividerBelow;

    private IOverlayManager mOverlayManager;
    private int mCurrentUserId;

    public CategoryPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mOverlayManager = IOverlayManager.Stub.asInterface(
                ServiceManager.getService(Context.OVERLAY_SERVICE));
        mCurrentUserId = ActivityManager.getCurrentUser();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Preference);

        mAllowDividerAbove = TypedArrayUtils.getBoolean(a, R.styleable.Preference_allowDividerAbove,
                R.styleable.Preference_allowDividerAbove, false);
        mAllowDividerBelow = TypedArrayUtils.getBoolean(a, R.styleable.Preference_allowDividerBelow,
                R.styleable.Preference_allowDividerBelow, false);
        a.recycle();

        setLayoutResource(R.layout.category_preference);
    }

    public CategoryPreference(Context context, View view) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setOnClickListener(mClickListener);

        final boolean selectable = isSelectable();
        holder.itemView.setFocusable(selectable);
        holder.itemView.setClickable(selectable);
        holder.setDividerAllowedAbove(mAllowDividerAbove);
        holder.setDividerAllowedBelow(mAllowDividerBelow);

        ImageView imageview = (ImageView) holder.findViewById(android.R.id.icon);
        imageview.getDrawable().setColorFilter(getContext().getResources().getColor(
                  ThemeAccentUtils.isUsingWhiteAccent(mOverlayManager, mCurrentUserId) ? R.color.dirty_tweaks_light_category_icon_tint : R.color.dirty_tweaks_dark_category_icon_tint), PorterDuff.Mode.SRC_IN);
    }
}
