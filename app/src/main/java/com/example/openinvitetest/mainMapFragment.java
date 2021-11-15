package com.example.openinvitetest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

//import com.google.android.gms.maps.MapView;
//import com.mapbox.mapboxsdk.Mapbox;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.Style;
//import com.mapbox.mapboxsdk.maps.UiSettings;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolDragListener;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolLongClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.mapboxsdk.utils.ColorUtils;

import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

import timber.log.Timber;

public class mainMapFragment extends Fragment implements LocationEngineCallback<LocationEngineResult>,
        PermissionsListener, View.OnClickListener, OnMapReadyCallback {
    private final String TAG = getClass().getSimpleName();

    private Button mCreateInvite, mUserInvite, mProfile, mMyLocation;
    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FragmentManager fm;
    private PermissionsManager mPermissionsManager;
    private LocationEngine mLocationEngine;

    private SymbolManager symbolManager;
    private Symbol symbol;

    private final String ID_ICON_MARKER = "marker";

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
                enableLocationComponent();
                addMarkerImageToStyle(style);

                GeoJsonOptions geoJsonOptions = new GeoJsonOptions().withTolerance(0.4f);
                symbolManager = new SymbolManager(mMapView, mMapboxMap, style, null, geoJsonOptions);

                //Symbol options stuff
                symbolManager.setIconAllowOverlap(true);
                symbolManager.setTextAllowOverlap(true);
            });
        });

        mAuth = FirebaseAuth.getInstance();
        mProfile = v.findViewById(R.id.profileBtn);
        mCreateInvite = v.findViewById(R.id.createInvite);
        mUserInvite = v.findViewById(R.id.userInvite);
        mMyLocation = v.findViewById(R.id.myLocationBtn);
        fm = getParentFragmentManager();

        if (mProfile != null) {
            mProfile.setOnClickListener(this);
        }
        if (mCreateInvite != null) {
            mCreateInvite.setOnClickListener(this);
        }
        if (mUserInvite != null) {
            mUserInvite.setOnClickListener(this);
        }if (mMyLocation != null) {
            mMyLocation.setOnClickListener(this);
        }
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

        mMapView.getMapAsync(this);

        Log.d(TAG, "On Create View portion of Fragment Lifecycle");
        return v;
    }


    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapboxMap = mMapboxMap;
        FirebaseDatabase.getInstance().getReference().child("Invites").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    JsonElement userId;
                    userId = new JsonParser().parse(child.getKey());
                    Double lat = (double) child.child("lat").getValue();
                    Double lng = (double) child.child("lng").getValue();
                    SymbolOptions symbolOptions = new SymbolOptions()
                            .withLatLng(new LatLng(lat, lng))
                            .withIconImage(ID_ICON_MARKER)
                            .withIconSize(1.3f)
                            .withSymbolSortKey(10.0f)
                            .withData(userId)
                            .withDraggable(false);
                    symbol = symbolManager.create(symbolOptions);
                }
                if (dataSnapshot.hasChildren()) {
                    symbolManager.addClickListener(new OnSymbolClickListener() {
                        @Override
                        public boolean onAnnotationClick(Symbol symbol) {
                            Toast.makeText(getContext(), symbol.getData().toString(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                }
            }
        });
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
            LocationComponent locationComponent = mMapboxMap.getLocationComponent();
            LocationComponentActivationOptions.Builder builder =
                    new LocationComponentActivationOptions.Builder(getContext(),
                            Objects.requireNonNull(mMapboxMap.getStyle()));
            builder.useDefaultLocationEngine(true);
            LocationComponentActivationOptions options = builder.build();
            locationComponent.activateLocationComponent(options);
        } else {
            mPermissionsManager = new PermissionsManager(this);
            mPermissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.profileBtn) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())
                    .addToBackStack("profile_fragment")
                    .commit();
        } else if (viewId == R.id.myLocationBtn) {
            requestLocation();
        } else if (viewId == R.id.createInvite) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new CreateInviteFragment())
                    .addToBackStack("profile_fragment")
                    .commit();
        } else if (viewId == R.id.userInvite) {
            FirebaseDatabase.getInstance().getReference().child("Invites").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                        Toast.makeText(getContext(), "You don't have an invite currently", Toast.LENGTH_LONG).show();
                    } else {
                        fm.beginTransaction()
                                .replace(R.id.fragment_container, new InviteFragment())
                                .addToBackStack("profile_fragment")
                                .commit();
                    }
                }
            });
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

    private void requestLocation() {
        Timber.d("requestLocation()");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsManager.areLocationPermissionsGranted(getContext())) {
                int PERMISSION_REQUEST_LOCATION = 1;
                mPermissionsManager = new PermissionsManager(this);
                mPermissionsManager.requestLocationPermissions(getActivity());
            } else {
                doRequestLocation();
            }
        } else {
            doRequestLocation();
        }
    }

    private void doRequestLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mLocationEngine = initializeLocationEngine();
        }
    }

    private void setCameraPosition(Location location) {
        mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private LocationEngine initializeLocationEngine() {
        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(getContext());
        LocationEngineRequest.Builder locRequestBuilder = new LocationEngineRequest.Builder(60000);
//        locRequestBuilder.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationEngineRequest locRequest = locRequestBuilder.build();

        if (hasLocationPermission()) {
            locationEngine.requestLocationUpdates(locRequest, this, Looper.getMainLooper());
        }

        return locationEngine;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean lacksLocationPermission() {
        return !PermissionsManager.areLocationPermissionsGranted(getContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasLocationPermission() {
        return PermissionsManager.areLocationPermissionsGranted(getContext());
    }

    @Override
    public void onSuccess(LocationEngineResult result) {
        Location location = result.getLastLocation();
        if (location != null) {
            setCameraPosition(location);
        }
    }

    private void addMarkerImageToStyle(Style style) {
        style.addImage(ID_ICON_MARKER,
                BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_marker)),
                true);
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private boolean lacksLocationPermission() {
//        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private boolean hasLocationPermission() {
//        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED;
//    }

    //TODO

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {

    }



    @Override
    public void onFailure(@NonNull Exception exception) {

    }

}
