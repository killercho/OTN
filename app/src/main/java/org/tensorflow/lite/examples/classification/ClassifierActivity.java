/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.classification;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.tensorflow.lite.examples.classification.env.BorderedText;
import org.tensorflow.lite.examples.classification.env.Logger;
import org.tensorflow.lite.examples.classification.tflite.Classifier;
import org.tensorflow.lite.examples.classification.tflite.Classifier.Device;
import org.tensorflow.lite.examples.classification.tflite.Classifier.Model;

public class ClassifierActivity extends CameraActivity implements OnImageAvailableListener {
  private static final Logger LOGGER = new Logger();
  private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
  private static final float TEXT_SIZE_DIP = 10;
  private Bitmap rgbFrameBitmap = null;
  private long lastProcessingTimeMs;
  private Integer sensorOrientation;
  private Classifier classifier;
  private BorderedText borderedText;
  int tmr = 0;
  String first;
  String title = "Animal recognised!";
  String dialogMsg = "This is probably a ";
  boolean isTheDialogActive = false;
  /** Input image size of the model along x axis. */
  private int imageSizeX;
  /** Input image size of the model along y axis. */
  private int imageSizeY;




 // FirebaseStorage storage = FirebaseStorage.getInstance();
//  StorageReference storageRef = storage.getReference();
//
//
//  String currentPhotoPath;
//  String imageFileName;
//
//  private File createImageFile() throws IOException {
//    // Create an image file name
//    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//    String imageFileName = "JPEG_" + timeStamp + "_";
//    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//    File image = File.createTempFile(
//            imageFileName,  /* prefix */
//            ".jpg",         /* suffix */
//            storageDir      /* directory */
//    );
//
//    // Save a file: path for use with ACTION_VIEW intents
//    currentPhotoPath = image.getAbsolutePath();
//    return image;
//  }
//
//
//  static final int REQUEST_TAKE_PHOTO = 1;
//
//  private void dispatchTakePictureIntent() {
//    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//    // Ensure that there's a camera activity to handle the intent
//    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//      // Create the File where the photo should go
//      File photoFile = null;
//      try {
//        photoFile = createImageFile();
//      } catch (IOException ex) {
//        // Error occurred while creating the File
//      }
//      // Continue only if the File was successfully created
//      if (photoFile != null) {
//        Uri photoURI = FileProvider.getUriForFile(this,
//                "com.example.android.fileprovider",
//                photoFile);
//        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//      }
//    }
//  }




  @Override
  protected int getLayoutId() {
    return R.layout.tfe_ic_camera_connection_fragment;
  }

  @Override
  protected Size getDesiredPreviewFrameSize() {
    return DESIRED_PREVIEW_SIZE;
  }

  @Override
  public void onPreviewSizeChosen(final Size size, final int rotation) {
    final float textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
    borderedText = new BorderedText(textSizePx);
    borderedText.setTypeface(Typeface.MONOSPACE);

    recreateClassifier(getModel(), getDevice(), getNumThreads());
    if (classifier == null) {
      LOGGER.e("No classifier on preview!");
      return;
    }

    previewWidth = size.getWidth();
    previewHeight = size.getHeight();

    sensorOrientation = rotation - getScreenOrientation();
    LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

    LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
    rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
  }

    @Override
  protected void processImage() {
    rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);
    final int cropSize = Math.min(previewWidth, previewHeight);


    runInBackground(
        new Runnable() {
          @Override
          public void run() {
            if (classifier != null) {

              final long startTime = SystemClock.uptimeMillis();
              final List<Classifier.Recognition> results =
                  classifier.recognizeImage(rgbFrameBitmap, sensorOrientation);

              if (first != results.get(0).getTitle()) {
                tmr = 0;
              }

              first = results.get(0).getTitle();

              if (tmr == 5 && !isTheDialogActive) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ClassifierActivity.this, R.style.MyDialog);
                alertDialogBuilder.setTitle(title);
                alertDialogBuilder.setMessage(dialogMsg + first);
                alertDialogBuilder.setPositiveButton("Learn more", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(ClassifierActivity.this, openDBPageActivity.class);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("foundAnimals");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user != null){
                      String uid = user.getUid();
                      myRef.child(uid).child(first.toLowerCase()).setValue(true);
                    }

                    startActivity(intent);
                    isTheDialogActive = false;
                    tmr = 0;
                  }
                });

                alertDialogBuilder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                    isTheDialogActive = false;
                    tmr = 0;
                  }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                if(!alertDialog.isShowing()){

//                  dispatchTakePictureIntent();

                  alertDialog.show();
                  isTheDialogActive = true;
                }else {
                  isTheDialogActive = false;
                  tmr = 0;
                }
              }else {
                tmr++;
              }



              lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
              LOGGER.v("Detect: %s", results);

              runOnUiThread(
                  new Runnable() {
                    @Override
                    public void run() {
                      showResultsInBottomSheet(results);
                      showFrameInfo(previewWidth + "x" + previewHeight);
                      showCropInfo(imageSizeX + "x" + imageSizeY);
                      showCameraResolution(cropSize + "x" + cropSize);
                      showRotationInfo(String.valueOf(sensorOrientation));
                      showInference(lastProcessingTimeMs + "ms");
                    }
                  });
            }
            readyForNextImage();
          }
        });
  }

  @Override
  protected void onInferenceConfigurationChanged() {
    if (rgbFrameBitmap == null) {
      // Defer creation until we're getting camera frames.
      return;
    }
    final Device device = getDevice();
    final Model model = getModel();
    final int numThreads = getNumThreads();
    runInBackground(() -> recreateClassifier(model, device, numThreads));
  }

  private void recreateClassifier(Model model, Device device, int numThreads) {
    if (classifier != null) {
      LOGGER.d("Closing classifier.");
      classifier.close();
      classifier = null;
    }
    if (device == Device.GPU && model == Model.QUANTIZED) {
      LOGGER.d("Not creating classifier: GPU doesn't support quantized models.");
      runOnUiThread(
          () -> {
            Toast.makeText(this, "GPU does not yet supported quantized models.", Toast.LENGTH_LONG)
                .show();
          });
      return;
    }
    try {
      LOGGER.d(
          "Creating classifier (model=%s, device=%s, numThreads=%d)", model, device, numThreads);
      classifier = Classifier.create(this, model, device, numThreads);
    } catch (IOException e) {
      LOGGER.e(e, "Failed to create classifier.");
    }

    // Updates the input image size.
    imageSizeX = classifier.getImageSizeX();
    imageSizeY = classifier.getImageSizeY();
  }



}
