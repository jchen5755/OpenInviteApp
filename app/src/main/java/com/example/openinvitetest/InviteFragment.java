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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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

public class InviteFragment extends Fragment implements View.OnClickListener {
    private Button mDeleteInvite;
    private EditText mInviteTitle, mInviteDescription;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    private PermissionsManager mPermissionsManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_invite, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDeleteInvite = v.findViewById(R.id.deleteInviteButton);
        mInviteTitle = v.findViewById(R.id.inviteTitle);
        mInviteDescription = v.findViewById(R.id.inviteDesc);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (mDeleteInvite != null) {
            mDeleteInvite.setOnClickListener(this);
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

    private void requestLocation() {
        Timber.d("requestLocation()");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsManager.areLocationPermissionsGranted(getContext())) {
                int PERMISSION_REQUEST_LOCATION = 1;
//                mPermissionsManager = new PermissionsManager(this);
//                mPermissionsManager.requestLocationPermissions(getActivity());
            } else {
                doRequestLocation();
            }
        } else {
            doRequestLocation();
        }
    }

    private void doRequestLocation() {
        locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Log.e("TAG", "GPS is on");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            //This is what you need:
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(Context.LOCATION_SERVICE, 1000, 0, (LocationListener) this);
        }
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == mDeleteInvite.getId()) {
            final String title = mInviteTitle.getText().toString();
            final String description = mInviteDescription.getText().toString();
            doRequestLocation();
            String userId = mAuth.getCurrentUser().getUid();
            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Invites").child((userId));
            Map userInfo = new HashMap<>();
            userInfo.put("lat", latitude);
            userInfo.put("lng", longitude);
            userInfo.put("title", title);
            userInfo.put("description", description);
            currentUserDb.updateChildren(userInfo);
        }
    }
}
