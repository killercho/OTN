package com.example.otnv_10;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import org.tensorflow.lite.examples.classification.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
