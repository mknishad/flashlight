package com.ideafactorybd.flashlight;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

  private CameraManager cameraManager;
  private String cameraId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // remove title
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_main);

    boolean isFlashAvailable = getApplicationContext().getPackageManager()
        .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

    if (!isFlashAvailable) {
      showNoFlashError();
    }

    cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
    try {
      cameraId = cameraManager.getCameraIdList()[0];
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }

    SwitchCompat flashSwitch = findViewById(R.id.flashSwitch);
    flashSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> switchFlashLight(isChecked));
  }

  public void showNoFlashError() {
    AlertDialog alert = new AlertDialog.Builder(this)
        .create();
    alert.setTitle("Oops!");
    alert.setMessage("Flash not available in this device...");
    alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        finish();
      }
    });
    alert.show();
  }

  public void switchFlashLight(boolean status) {
    try {
      cameraManager.setTorchMode(cameraId, status);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }
}
