package com.example.openinvitetest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private EditText mFirstName, mLastName, mEmail;
    private Button mUpdateProfile, mDeleteAcct;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        fm = getParentFragmentManager();
        mFirstName = v.findViewById(R.id.profile_first_name);
        mLastName = v.findViewById(R.id.profile_last_name);
        mEmail = v.findViewById(R.id.profile_email);
        mUpdateProfile = v.findViewById(R.id.updateBtn);
        mDeleteAcct = v.findViewById(R.id.deleteAccountBtn);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mEmail.setHint(user.getEmail());
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child((userId));
        currentUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("ree", snapshot.getValue().toString());
                mFirstName.setHint(snapshot.child("firstName").getValue().toString());
                mLastName.setHint(snapshot.child("lastName").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (mUpdateProfile != null) {
            mUpdateProfile.setOnClickListener(this);
        }
        if (mDeleteAcct != null) {
            mDeleteAcct.setOnClickListener(this);
        }
//        mFirstName.setText((CharSequence) currentUserDb.child("lastName"));
//        mFirstName.setText(user.getEmail());
        return v;
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child((userId));
        Map userInfo = new HashMap<>();
        if (viewId == mUpdateProfile.getId()) {
            Log.d(TAG, "TEST: "+mEmail.getText().toString());
            if (!mEmail.getText().toString().isEmpty()) {
                user.updateEmail(mEmail.getText().toString());
                user.sendEmailVerification();
            }
            if (!mFirstName.getText().toString().isEmpty()) {
                userInfo.put("firstName", mFirstName.getText().toString());
            }
            if (!mLastName.getText().toString().isEmpty()) {
                userInfo.put("lastName", mLastName.getText().toString());
            }
            currentUserDb.updateChildren(userInfo);
            Toast.makeText(getContext(), "Email Successfully Updated!", Toast.LENGTH_SHORT);
//            fm.beginTransaction()
//                    .replace(R.id.fragment_container, new ProfileFragment())
//                    .commit();
        } else if (viewId == mDeleteAcct.getId()) {
            currentUserDb.removeValue();
            user.delete();
            Toast.makeText(getContext(), "Account Destroyed!", Toast.LENGTH_SHORT);
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new MainFragment())
                    .commit();
        }
    }
}
