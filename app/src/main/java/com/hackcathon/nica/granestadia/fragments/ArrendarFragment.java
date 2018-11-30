package com.hackcathon.nica.granestadia.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackcathon.nica.granestadia.MainActivity;
import com.hackcathon.nica.granestadia.R;



public class ArrendarFragment extends Fragment {

    private View root;


    public ArrendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_arrendar, container, false);

        ((MainActivity) getActivity()).setActionBarTitle("Arrendar");

        return root;
    }

}
