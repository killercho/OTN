package org.tensorflow.lite.examples.classification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    Switch switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (InitApplication.getInstance().isNightModeEnabled()) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }
        setContentView(R.layout.activity_settings);
        Switch switchCompat = findViewById(R.id.colorSwitch);

//        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
//            switchCompat.setChecked(true);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // InitApplication.getInstance().setIsNightModeEnabled(true);
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
                else{
                    // InitApplication.getInstance().setIsNightModeEnabled(false);
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }
}
