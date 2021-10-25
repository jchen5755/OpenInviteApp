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
//        mAuth = FirebaseAuth.getInstance();
        mRegister = (Button) v.findViewById(R.id.register);
        mEmail = (EditText) v.findViewById(R.id.email);
        mPassword = (EditText) v.findViewById(R.id.password);
        mPasswordCheck = (EditText) v.findViewById(R.id.confirm_password);
        mFirstName = (EditText) v.findViewById(R.id.first_name);
        mLastName = (EditText) v.findViewById(R.id.last_name);
        mExisting = v.findViewById(R.id.existing);
        fm = getParentFragmentManager();
        if (mRegister != null) {
            mRegister.setOnClickListener(this);
        }
        if (mExisting != null) {
            mExisting.setOnClickListener(this);
        }
//        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user !=  null && user.isEmailVerified()) {
//                    Intent i = new Intent(RegisterFragment.this, ProfileActivity.class);
//                    startActivity(i);
//                    finish();
//                    return;
//                }
//            }
//        };
        Log.d(TAG, "On Create View for REGISTER");
        return v;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mAuth = FirebaseAuth.getInstance();
////        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
////            @Override
////            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
////                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
////                if (user !=  null && user.isEmailVerified()) {
////                    Intent i = new Intent(RegisterFragment.this, ProfileActivity.class);
////                    startActivity(i);
////                    finish();
////                    return;
////                }
////            }
////        };
////        existing.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Intent i = new Intent(RegisterFragment.this, LoginActivity.class);
////                startActivity(i);
////                finish();
////                return;
////            }
////        });
////        mRegister = (Button) findViewById(R.id.register);
////        mEmail = (EditText) findViewById(R.id.email);
////        mPassword = (EditText) findViewById(R.id.password);
////        mPasswordCheck = (EditText) findViewById(R.id.confirm_password);
////        mFirstName = (EditText) findViewById(R.id.first_name);
////        mLastName = (EditText) findViewById(R.id.last_name);
//        mRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final String email = mEmail.getText().toString();
//                final String password = mPassword.getText().toString();
//                final String passwordCheck = mPasswordCheck.getText().toString();
//                final String firstName = mFirstName.getText().toString();
//                final String lastName = mLastName.getText().toString();
//
//                if (checkInputs(email, password, passwordCheck, firstName, lastName)) {
//                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterFragment.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (!task.isSuccessful()) {
//                                Toast.makeText(RegisterFragment.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            } else {
//                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(RegisterFragment.this, "Registered successfully." + " Please check your email for verification", Toast.LENGTH_SHORT).show();
//                                            String userId = mAuth.getCurrentUser().getUid();
//                                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child((userId));
//                                            Map userInfo = new HashMap<>();
//                                            userInfo.put("name", firstName + " " + lastName);
//                                            userInfo.put("profileImageUrl", "default");
//                                            currentUserDb.updateChildren(userInfo);
//
//                                            mEmail.setText("");
//                                            mFirstName.setText("");
//                                            mFirstName.setText("");
//                                            mPassword.setText("");
//                                            mPasswordCheck.setText("");
//                                            Intent i = new Intent(RegisterFragment.this, MainActivity.class);
//                                            startActivity(i);
//                                            finish();
//                                            return;
//                                        } else {
//                                            Toast.makeText(RegisterFragment.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
//                }
//            }
//        });
//    }

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
//        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
//        mAuth.removeAuthStateListener(firebaseAuthStateListener);
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

//            if (checkInputs(email, password, passwordCheck, firstName, lastName)) {
//                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        } else {
//                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(getContext(), "Registered successfully." + " Please check your email for verification", Toast.LENGTH_SHORT).show();
//                                        String userId = mAuth.getCurrentUser().getUid();
//                                        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child((userId));
//                                        Map userInfo = new HashMap<>();
//                                        userInfo.put("name", firstName + " " + lastName);
//                                        userInfo.put("profileImageUrl", "default");
//                                        currentUserDb.updateChildren(userInfo);
//
//                                        mEmail.setText("");
//                                        mFirstName.setText("");
//                                        mFirstName.setText("");
//                                        mPassword.setText("");
//                                        mPasswordCheck.setText("");
//                                        return;
//                                    } else {
//                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }
//                    }
//                });
//            }



            fm.beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())
                    .addToBackStack("profile_fragment")
                    .commit();
        } else if (viewId == mExisting.getId()) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .addToBackStack("login_fragment")
                    .commit();
        }
    }
}