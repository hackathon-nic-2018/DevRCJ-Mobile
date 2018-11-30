package com.hackcathon.nica.granestadia.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackcathon.nica.granestadia.R;


public class Fragment1 extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment1, container, false);



        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("Backs", "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("Backs", "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ChangeFragment(new MapFragment());
                    return true;
                }
                return false;
            }
        });


        return v;
    }

    private void ChangeFragment(Fragment fragment){
        FragmentManager fmanager = getFragmentManager();
        assert fmanager != null;
        FragmentTransaction ftransaction = fmanager.beginTransaction();
        ftransaction.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }

}