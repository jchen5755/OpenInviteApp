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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
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

public class OtherInviteFragment extends Fragment implements View.OnClickListener{
    private final String TAG = getClass().getSimpleName();
    private EditText mInviteTitle, mInviteDescription;
    private TextView mInviteUser;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ImageButton mMapBtn;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    private PermissionsManager mPermissionsManager;
    private FragmentManager fm;

    private userIdViewModel vm;
    private String resultUserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_other_invite, container, false);

        vm = new ViewModelProvider(requireActivity()).get(userIdViewModel.class);
        resultUserID = vm.returnID();
        mMapBtn = v.findViewById(R.id.mapBtn);
        fm = getParentFragmentManager();
        mAuth = FirebaseAuth.getInstance();
        mInviteTitle = v.findViewById(R.id.otherInviteTitle);
        mInviteDescription = v.findViewById(R.id.otherInviteDesc);
        mInviteUser = v.findViewById(R.id.otherInviteUser);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = mAuth.getCurrentUser().getUid();
        if (mMapBtn != null) {
            mMapBtn.setOnClickListener(this);
        }
        FirebaseDatabase.getInstance().getReference().child("Users").child((resultUserID)).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("firstName").getValue().toString() + " " + dataSnapshot.child("lastName").getValue().toString();
                mInviteUser.setText(name + "'s Invite");
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Invites").child((resultUserID)).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                mInviteTitle.setText(dataSnapshot.child("title").getValue().toString());
                mInviteDescription.setText(dataSnapshot.child("description").getValue().toString());
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
        if (viewId == mMapBtn.getId()) {
            fm.popBackStackImmediate();
        }
    }
}
