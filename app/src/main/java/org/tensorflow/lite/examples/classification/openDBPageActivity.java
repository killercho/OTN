package org.tensorflow.lite.examples.classification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.BooleanNode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.List;

public class openDBPageActivity extends AppCompatActivity {


    String butterfly = "https://firebasestorage.googleapis.com/v0/b/otn-tf-boilerplate.appspot.com/o/pictures%2FbutterflyGrey2.jpg?alt=media&token=4e9060a2-9844-4396-92cc-717aa20dea52",
            cat = "https://firebasestorage.googleapis.com/v0/b/otn-tf-boilerplate.appspot.com/o/pictures%2FcatGrey.jpg?alt=media&token=cabf1b3e-f461-4de1-af87-a3ea447b4353",
            dog = "https://firebasestorage.googleapis.com/v0/b/otn-tf-boilerplate.appspot.com/o/pictures%2FdogGrey.jpg?alt=media&token=d84ed2e3-fe24-4d96-910f-e3321e380242",
            squirrel = "https://firebasestorage.googleapis.com/v0/b/otn-tf-boilerplate.appspot.com/o/pictures%2FsquirrelGrey.jpg?alt=media&token=0a504023-76ac-428c-b39f-ad0ad82b6184",
            spider = "https://firebasestorage.googleapis.com/v0/b/otn-tf-boilerplate.appspot.com/o/pictures%2FspiderGrey.jpg?alt=media&token=ff391b39-0b53-4697-8407-c1f0df0fe45b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_dbpage);

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        Spinner s = findViewById(R.id.menuSpinner);

        TextView squirrelText = findViewById(R.id.squirrelText);
        TextView spiderText = findViewById(R.id.spiderText);
        TextView catText = findViewById(R.id.catText);
        TextView dogText = findViewById(R.id.dogText);
        TextView butterflyText = findViewById(R.id.butterflyText);

        squirrelText.setMovementMethod(new ScrollingMovementMethod());
        spiderText.setMovementMethod(new ScrollingMovementMethod());
        catText.setMovementMethod(new ScrollingMovementMethod());
        dogText.setMovementMethod(new ScrollingMovementMethod());
        butterflyText.setMovementMethod(new ScrollingMovementMethod());

//        if((String.valueOf(s.getSelectedItem()).equals("Plants") && s.performClick()) || (String.valueOf(s.getSelectedItem()).equals("Mushrooms")) && s.performClick()){
//            openPlantsDB();
//        }
//
//        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if(String.valueOf(s.getSelectedItem()).equals("Plants") || String.valueOf(s.getSelectedItem()).equals("Mushrooms")){
//                    openPlantsDB();
//                }
//
//                if (String.valueOf(s.getSelectedItem()).equals("Animals"))
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });



        ImageView squirrelImage = findViewById(R.id.squirrelImg);

        ImageView spiderImage = findViewById(R.id.spiderImg);

        ImageView dogImage = findViewById(R.id.dogImg);

        ImageView catImage = findViewById(R.id.catImg);

        ImageView butterflyImage = findViewById(R.id.butterflyImg);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("foundAnimals");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String arr[] = {"cat", "butterfly", "dog", "squirrel", "spider"};

        if(user != null){
            String uid = user.getUid();
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (String animal : arr) {
                            boolean val = Boolean.parseBoolean(dataSnapshot.child(uid).child(animal).getValue().toString());
                            if (val == true) {
                                Log.d("blah", "sq sha vidim");
                                switch (animal) {
                                    case "butterfly":
                                        butterfly = "https://firebasestorage.googleapis.com/v0/b/otn-tf-boilerplate.appspot.com/o/pictures%2Fbutterfly2.jpg?alt=media&token=ba138613-918f-498b-8770-95feca557789";
                                        Glide.with(openDBPageActivity.this).load(butterfly).into(butterflyImage);
                                        break;
                                    case "cat":
                                        cat = "https://firebasestorage.googleapis.com/v0/b/otn-tf-boilerplate.appspot.com/o/pictures%2Fcat.png?alt=media&token=59203226-bc9e-46eb-b2a9-880136cbd786";
                                        Glide.with(openDBPageActivity.this).load(cat).into(catImage);
                                        break;
                                    case "dog":
                                        dog = "https://firebasestorage.googleapis.com/v0/b/otn-tf-boilerplate.appspot.com/o/pictures%2Fdog.jpg?alt=media&token=c3573d9f-d531-4b27-9558-14c462ee4ab2";
                                        Glide.with(openDBPageActivity.this).load(dog).into(dogImage);
                                        break;
                                    case "spider":
                                        spider = "https://firebasestorage.googleapis.com/v0/b/otn-tf-boilerplate.appspot.com/o/pictures%2Fspider.jpeg?alt=media&token=14972775-9a0c-4e4b-851f-afc045233b6d";
                                        Glide.with(openDBPageActivity.this).load(spider).into(spiderImage);
                                        break;
                                    case "squirrel":
                                        squirrel = "https://firebasestorage.googleapis.com/v0/b/otn-tf-boilerplate.appspot.com/o/pictures%2Fsqurrel.jpeg?alt=media&token=1a978c92-7ac8-451e-b5bf-230167ef578d";
                                        Glide.with(openDBPageActivity.this).load(squirrel).into(squirrelImage);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("blah", "loadPost:onCancelled", databaseError.toException());
                    }


                };
                myRef.addValueEventListener(postListener);

        }

        Glide.with(this).load(squirrel).into(squirrelImage);

        Glide.with(this).load(spider).into(spiderImage);

        Glide.with(this).load(dog).into(dogImage);

        Glide.with(this).load(cat).into(catImage);

        Glide.with(this).load(butterfly).into(butterflyImage);


    }

    private void setSupportActionBar(Toolbar myToolbar) {
    }

    public void returnHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openPlantsDB(View v) {
        Intent intent = new Intent(this, PlantsActivity.class);
        startActivity(intent);
    }

}