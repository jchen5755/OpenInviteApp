package com.example.openinvitetest;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainFragment extends Fragment implements View.OnClickListener{
    private Button mLogin, mRegister;
    private LayoutInflater mInflater;
    private ViewGroup mContainer;
    private Bundle mSavedInstanceState;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        mContainer = container;
        mSavedInstanceState = savedInstanceState;
        View v = initializeUserInterface(mInflater, mContainer, mSavedInstanceState);
        final Button loginButton = v.findViewById(R.id.login);
        if (loginButton != null) {
            loginButton.setOnClickListener(this);
        }
        final Button registerButton = v.findViewById(R.id.register);
        if (registerButton != null) {
            registerButton.setOnClickListener(this);
        }
        return v;
    }

    public View initializeUserInterface(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;

        // Get the screen orientation.
        int orientation = getActivity().getResources().getConfiguration().orientation;

        // Inflate the appropriate layout based on the screen orientation.
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            view = inflater.inflate(R.layout.fragment_choose_login_and_reg, container, false);
        }
        else { // orientation == Configuration.ORIENTATION_LANDSCAPE
            view = inflater.inflate(R.layout.fragment_choose_login_and_reg_horizontal, container, false);
        }

        return view;
    }

    /**
     * This is called when the user rotates the device.
     * @param newConfig Configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Create the new layout.
        View view = initializeUserInterface(mInflater, mContainer, mSavedInstanceState);

        // Call the default method to cover our bases.
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View view) {
        final Activity activity = requireActivity();
        final int viewId = view.getId();
        FragmentManager fm = getParentFragmentManager();

        if (viewId == R.id.login) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .addToBackStack("login_fragment")
                    .commit();
        } else if (viewId == R.id.register) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new RegisterFragment())
                    .addToBackStack("register_fragment")
                    .commit();
            Log.d("ree", "register button clicked in main fragment");
        }
    }
}
