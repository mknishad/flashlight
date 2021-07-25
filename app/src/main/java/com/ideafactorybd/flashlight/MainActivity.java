package com.ideafactorybd.flashlight;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

  private ImageButton imageButton;
  private Camera camera;
  private Camera.Parameters parameters;
  private boolean isFlash = false;
  private boolean isOn = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // remove title
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_main);

    imageButton = (ImageButton) findViewById(R.id.imageButton);

    if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
      camera = Camera.open();
      parameters = camera.getParameters();
      isFlash = true;
    }

    imageButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          if (isFlash) {
            if (!isOn) {
              imageButton.setImageResource(R.drawable.on);
              parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
              camera.setParameters(parameters);
              camera.startPreview();
              isOn = true;
            } else {
              imageButton.setImageResource(R.drawable.off);
              parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
              camera.setParameters(parameters);
              camera.stopPreview();
              isOn = false;
            }
          } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Error...");
            builder.setMessage("Flashlight is not available on this device...");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
              }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
          }
        } catch (Exception e) {
        }
      }
    });
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (camera != null) {
      camera.release();
      camera = null;
    }
  }
}
