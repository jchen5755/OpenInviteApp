package com.example.openinvitetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_login_and_reg, container, false);
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

    @Override
    public void onClick(View view) {
        final Activity activity = requireActivity();
        final int viewId = view.getId();
        FragmentManager fm = getParentFragmentManager();
        Fragment fragment = new LoginFragment();

        if (viewId == R.id.login) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("login_fragment")
                    .commit();
        } else if (viewId == R.id.register) {
            startActivity(new Intent(activity, RegisterActivity.class));
        }
    }
}
