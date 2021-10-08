package com.example.openinvitetest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.zip.Inflater;

public class LoginFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        final Button loginBtn = v.findViewById(R.id.login_btn);
        if (loginBtn != null) {
            loginBtn.setOnClickListener(this);
        }
        return v;
    }


    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.login_btn) {
            CharSequence text = "Log In is not Implemented!";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }
}
