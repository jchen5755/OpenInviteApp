package com.example.openinvitetest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.zip.Inflater;

public class LoginFragment extends Fragment implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    private Button mLogin;
    private EditText mEmail, mPassword;
    private TextView mForgotPassword;
    private boolean loginButtonClicked;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        loginButtonClicked = false;
        mAuth = FirebaseAuth.getInstance();
        mLogin = v.findViewById(R.id.login_btn);
        mEmail = v.findViewById(R.id.email);
        mPassword = v.findViewById(R.id.password);
        mForgotPassword = v.findViewById(R.id.password_reset);
        fm = getParentFragmentManager();

        if (mLogin != null) {
            mLogin.setOnClickListener(this);
        }
        if (mForgotPassword != null) {
            mForgotPassword.setOnClickListener(this);
        }
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };
        Log.d(TAG, "On Create View portion of Fragment Lifecycle");
        return v;
    }


    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        loginButtonClicked = true;
        if (viewId == R.id.login_btn) {
            final String email = mEmail.getText().toString();
            final String password = mPassword.getText().toString();

            if (email.equals("") || password.equals("")) {
                Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            fm.beginTransaction()
                                    .replace(R.id.fragment_container, new ProfileFragment())
                                    .commit();
                            return;
                        }
                    }
                });
            }
        } else if (viewId == R.id.password_reset) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new MainFragment())
                    .addToBackStack("main_fragment")
                    .commit();
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
