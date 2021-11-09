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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

//import com.google.android.gms.maps.MapView;
//import com.mapbox.mapboxsdk.Mapbox;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.Style;
//import com.mapbox.mapboxsdk.maps.UiSettings;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;

import java.util.zip.Inflater;

public class mainMapFragment extends Fragment implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    private Button mCreateInvite, mUserInvite;
    private ImageButton mProfile;
    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main_map, container, false);

        mMapView = v.findViewById(R.id.mainMapContainer);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(mapboxMap -> {
            mMapboxMap = mapboxMap;
            mMapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
                UiSettings uiSettings = mMapboxMap.getUiSettings();
                uiSettings.setCompassEnabled(true);
                uiSettings.setZoomGesturesEnabled(true);
//                enableLocationComponent();
            });
        });

        mAuth = FirebaseAuth.getInstance();
        mProfile = v.findViewById(R.id.profileBtn);
        mCreateInvite = v.findViewById(R.id.createInvite);
        mUserInvite = v.findViewById(R.id.userInvite);
        fm = getParentFragmentManager();

        if (mProfile != null) {
            mProfile.setOnClickListener(this);
        }
        if (mCreateInvite != null) {
            mCreateInvite.setOnClickListener(this);
        }
        if (mUserInvite != null) {
            mUserInvite.setOnClickListener(this);
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
        if (viewId == R.id.profileBtn) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())
                    .addToBackStack("profile_fragment")
                    .commit();
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
