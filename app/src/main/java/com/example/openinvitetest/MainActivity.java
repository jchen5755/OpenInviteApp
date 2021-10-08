package com.example.openinvitetest;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import timber.log.Timber;

public class MainActivity extends SingleFragmentActivity {
    private final String TAG = getClass().getSimpleName();

    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Intent i = new Intent(MainActivity.this, Choose_Login_And_Reg.class);
//        startActivity(i);
//        finish();
        Timber.tag(TAG).e("On Create");
        Log.d(TAG, "On Create portion of activity lifecycle");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "On Pause portion of activity lifecycle");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "On Destroy portion of activity lifecycle");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "On Resume portion of activity lifecycle");
    }
}