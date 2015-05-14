package com.example.rcos.gomueller.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rcos.gomueller.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MuellerActivityFragment extends Fragment {

    public MuellerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mueller, container, false);
    }
}
