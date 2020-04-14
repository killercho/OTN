package org.tensorflow.lite.examples.classification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.os.Bundle;

import org.tensorflow.lite.examples.classification.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openSettingsActivity(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openCameraActivity(View v) {
        Intent intent = new Intent(this, ClassifierActivity.class);
        startActivity(intent);
    }

    public void openDBActivity(View v) {
        Intent intent = new Intent(this, openDBPageActivity.class);
        startActivity(intent);
    }


    public void SignIn(View v) {
        Intent intent = new Intent(this, GoogleSignInActivity.class);
        startActivity(intent);
    }
}