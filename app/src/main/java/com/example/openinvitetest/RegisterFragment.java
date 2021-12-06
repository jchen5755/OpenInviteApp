package com.example.openinvitetest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private Button mRegister;
    private EditText mEmail, mPassword, mPasswordCheck, mFirstName, mLastName, mBudget;
    private TextView mExisting;
    private FragmentManager fm;

    private RadioGroup mRadioGroup;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
    private static final String TAG = "RegisterActivity";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        mAuth = FirebaseAuth.getInstance();
        mRegister = v.findViewById(R.id.register);
        mEmail = v.findViewById(R.id.email);
        mPassword = v.findViewById(R.id.password);
        mPasswordCheck = v.findViewById(R.id.confirm_password);
        mFirstName = v.findViewById(R.id.first_name);
        mLastName = v.findViewById(R.id.last_name);
        mExisting = v.findViewById(R.id.existing);
        fm = getParentFragmentManager();
        if (mRegister != null) {
            mRegister.setOnClickListener(this);
        }
        if (mExisting != null) {
            mExisting.setOnClickListener(this);
        }
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user !=  null && user.isEmailVerified()) {
//                    Intent i = new Intent(RegisterFragment.this, ProfileActivity.class);
//                    startActivity(i);
//                    finish();
//                    return;
//                }
            }
        };
        Log.d(TAG, "On Create View for REGISTER");
        return v;
    }


    private boolean checkInputs(String email, String password, String passwordCheck, String firstName, String lastName) {
        if (email.equals("") || password.equals("") || passwordCheck.equals("") || firstName.equals("") || lastName.equals("")) {
            Toast.makeText(getContext(), "All fields must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.matches(emailPattern)) {
            Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password == passwordCheck) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == mRegister.getId()) {

            final String email = mEmail.getText().toString();
            final String password = mPassword.getText().toString();
            final String passwordCheck = mPasswordCheck.getText().toString();
            final String firstName = mFirstName.getText().toString();
            final String lastName = mLastName.getText().toString();

            if (checkInputs(email, password, passwordCheck, firstName, lastName)) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getContext(), "Registered successfully." + " Please check your email for verification", Toast.LENGTH_SHORT).show();
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child((userId));
                            Map userInfo = new HashMap<>();
                            userInfo.put("firstName", firstName);
                            userInfo.put("lastName", lastName);
                            userInfo.put("profileImageUrl", "default");
                            currentUserDb.updateChildren(userInfo);

                            mEmail.setText("");
                            mFirstName.setText("");
                            mFirstName.setText("");
                            mPassword.setText("");
                            mPasswordCheck.setText("");
                            return;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
            }
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .commit();
        } else if (viewId == mExisting.getId()) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .commit();
        }
    }
}