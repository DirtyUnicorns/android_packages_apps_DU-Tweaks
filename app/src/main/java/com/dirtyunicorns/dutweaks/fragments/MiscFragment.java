package com.dirtyunicorns.dutweaks.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dirtyunicorns.dutweaks.R;

/**
 * Created by mazwoz on 12/16/14.
 */
public class MiscFragment extends Fragment {
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_misc, null);
        return  view;
    }
}
