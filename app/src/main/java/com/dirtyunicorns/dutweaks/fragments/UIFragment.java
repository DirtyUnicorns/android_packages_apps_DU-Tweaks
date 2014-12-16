package com.dirtyunicorns.dutweaks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dirtyunicorns.dutweaks.R;

/**
 * Created by mazwoz on 12/16/14.
 */
public class UIFragment extends Fragment {

    public UIFragment(){}


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ui, null);

        return  view;
    }
}
