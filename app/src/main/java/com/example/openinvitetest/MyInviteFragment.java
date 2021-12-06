package com.example.openinvitetest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class MyInviteFragment extends Fragment implements View.OnClickListener {
    private Button mDeleteInvite;
    private EditText mInviteTitle, mInviteDescription;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ImageButton mMapBtn;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    private PermissionsManager mPermissionsManager;
    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_invite, container, false);
        fm = getParentFragmentManager();
        mAuth = FirebaseAuth.getInstance();
        mDeleteInvite = v.findViewById(R.id.deleteInviteButton);
        mInviteTitle = v.findViewById(R.id.inviteTitle);
        mInviteDescription = v.findViewById(R.id.inviteDesc);
        mMapBtn = v.findViewById(R.id.mapBtn);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (mDeleteInvite != null) {
            mDeleteInvite.setOnClickListener(this);
        }
        if (mMapBtn != null) {
            mMapBtn.setOnClickListener(this);
        }
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Invites").child((userId));
        currentUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mInviteTitle.setText(snapshot.child("title").getValue().toString());
                mInviteDescription.setText(snapshot.child("description").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
        return v;
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == mDeleteInvite.getId()) {
            String userId = mAuth.getCurrentUser().getUid();
            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Invites").child((userId));
            currentUserDb.removeValue();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new mainMapFragment())
                    .addToBackStack("main_fragment")
                    .commit();
            return;
        } else if (viewId == mMapBtn.getId()) {
            fm.popBackStackImmediate();
        }
    }
}
