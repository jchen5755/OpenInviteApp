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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private EditText mFirstName, mLastName, mEmail;
    private Button mUpdateEmail, mDeleteAcct;
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
        mUpdateEmail = v.findViewById(R.id.updateEmailBtn);
        mDeleteAcct = v.findViewById(R.id.deleteAccountBtn);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mEmail.setText(user.getEmail());
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child((userId));
        currentUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("ree", snapshot.getValue().toString());
                mFirstName.setText(snapshot.child("firstName").getValue().toString());
                mLastName.setText(snapshot.child("lastName").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (mUpdateEmail != null) {
            mUpdateEmail.setOnClickListener(this);
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
        if (viewId == mUpdateEmail.getId()) {
            user.updateEmail(mEmail.getText().toString());
            Toast.makeText(getContext(), "Email Successfully Updated!", Toast.LENGTH_SHORT);
            user.sendEmailVerification();
        } else if (viewId == mDeleteAcct.getId()) {
            String userId = mAuth.getCurrentUser().getUid();
            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child((userId));
            currentUserDb.removeValue();
            user.delete();
            Toast.makeText(getContext(), "Account Destroyed!", Toast.LENGTH_SHORT);
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new MainFragment())
                    .commit();
        }
    }
}
