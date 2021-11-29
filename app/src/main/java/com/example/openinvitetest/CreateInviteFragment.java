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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.android.core.permissions.PermissionsManager;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class CreateInviteFragment extends Fragment implements View.OnClickListener {
    private Button mCreateInvite;
    private EditText mInviteTitle, mInviteDescription;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    private PermissionsManager mPermissionsManager;
    private FragmentManager fm;
    private int nullLocation = 999;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_invite, container, false);
        fm = getParentFragmentManager();
        mAuth = FirebaseAuth.getInstance();
        mCreateInvite = v.findViewById(R.id.createInviteButton);
        mInviteTitle = v.findViewById(R.id.inviteTitle);
        mInviteDescription = v.findViewById(R.id.inviteDesc);
        latitude = 9999;
        longitude = 9999;
        if (mCreateInvite != null) {
            mCreateInvite.setOnClickListener(this);
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
        return v;
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
//            locationManager.requestLocationUpdates(Context.LOCATION_SERVICE, 1000, 0, (LocationListener) this);
            Toast.makeText(getContext(), "We couldn't create your invite due to a location services error!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Invites").child((userId));
        if (viewId == mCreateInvite.getId()) {
            final String title = mInviteTitle.getText().toString();
            final String description = mInviteDescription.getText().toString();
            doRequestLocation();
            Map userInfo = new HashMap<>();
            if (latitude < 500 && longitude < 500) {
                userInfo.put("lat", latitude);
                userInfo.put("lng", longitude);
                userInfo.put("title", title);
                userInfo.put("description", description);
                currentUserDb.updateChildren(userInfo, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        try {
                            wait(50);
                        } catch (Exception e) {

                        }
                        ;
                        fm.beginTransaction()
                                .replace(R.id.fragment_container, new mainMapFragment())
                                .addToBackStack("main_fragment")
                                .commit();
                    }
                });
            }
        }
    }
}
