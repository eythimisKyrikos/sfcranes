package com.safeline.safelinecranes.ui.products;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.github.chrisbanes.photoview.PhotoView;
import com.safeline.safelinecranes.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CertificateActivity extends AppCompatActivity {

    Context mContext;
    Intent mIintent;
    Button cameraBtn;
    Button returnBtn;
    ImageView certificateView;
    String mRopeSerial;
    Bitmap bitmap;
    Boolean editMode;
    byte[] byteArray;
    private File output=null;

    private ActivityResultLauncher<Intent> cameraActivityLanuncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        cameraActivityResults(data);
                    } else {
                        Toast.makeText(mContext, "Canceled", Toast.LENGTH_LONG);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);
        mContext = this;
        setTitle("Certificate View");
        mIintent = getIntent();
        mRopeSerial = mIintent.getStringExtra("serialNumber");
        certificateView = findViewById(R.id.captureImage);

        String path;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +
                    File.separator + "SAFELINE_CRANES" + File.separator + "rope_certificates" + File.separator;
        } else {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +
                    File.separator + "SAFELINE_CRANES" + File.separator + "rope_certificates" + File.separator;
        }
        File directory = new File(path);
        File[] jpgfiles = directory.listFiles();
        if(jpgfiles != null) {
            for (File file : jpgfiles) {
                if (file.getName().startsWith("serial_" + mRopeSerial)) {
                    Bitmap bMap = BitmapFactory.decodeFile(file.getPath());
                    certificateView.setImageBitmap(bMap);
                    certificateView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(CertificateActivity.this);
                            View mView = getLayoutInflater().inflate(R.layout.picture_zoom_dialog, null);
                            PhotoView photoView = mView.findViewById(R.id.photoView_zoom);
                            photoView.setImageBitmap(bMap);
                            mBuilder.setView(mView);
                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();
                        }
                    });
                }
            }
        }
        cameraBtn = findViewById(R.id.btnCamera);
        returnBtn = findViewById(R.id.btnReturn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +
                            File.separator + "SAFELINE_CRANES" + File.separator + "rope_certificates" + File.separator;
                    File dir = new File(directory);
                    if(!dir.exists()) {
                        dir.mkdirs();
                    }
                    String timestamp = new SimpleDateFormat("dd_MM_yyyy_HHmmss").format(new Date());
                    output = new File(dir, "serial_" + mRopeSerial + "_" + timestamp +  ".jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                } else {
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +
                            File.separator + "SAFELINE_CRANES" + File.separator + "rope_certificates" + File.separator;
                    File dir = new File(directory);
                    if(!dir.exists()) {
                        dir.mkdirs();
                    }
                    String timestamp = new SimpleDateFormat("dd_MM_yyyy_HHmmss").format(new Date());
                    output = new File(dir, "serial_" + mRopeSerial + "_" + timestamp +  ".jpg");
                    File file = new File(Uri.fromFile(output).getPath());
                    Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraActivityLanuncher.launch(intent);
            }
        });
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(returnBtn.getText().equals("Save")) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("certificate", byteArray);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
                else {
                    finish();
                }
            }
        });
    }

    private void cameraActivityResults(Intent data) {
        String path = getImagePath();
        File imgFile = new File(path);
        if(imgFile.exists()) {
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            certificateView.setImageBitmap(bitmap);
        }
        certificateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CertificateActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.picture_zoom_dialog, null);
                PhotoView photoView = mView.findViewById(R.id.photoView_zoom);
                photoView.setImageBitmap(bitmap);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
        });
    }
    public void zoomImageFromThumb(final View thumbView, int imageResId) {
        ImageView image = (ImageView) thumbView;
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CertificateActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.picture_zoom_dialog, null);
                PhotoView photoView = mView.findViewById(R.id.photoView_zoom);
                photoView.setImageResource(imageResId);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
        });
    }
    private void saveImageToGallery(Bitmap finalBitmap) {
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +
                File.separator + "SAFELINE_CRANES" + File.separator + "rope_certificates" + File.separator;
        File folder = new File(directory);
        if(!folder.exists()) {
            folder.mkdirs();
        }
        String timestamp = new SimpleDateFormat("dd_MM_yyyy_HHmmss").format(new Date());
        File file = new File(directory, "serial_" + mRopeSerial + "_" + timestamp +  ".jpg");
        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                Toast.makeText(this, "Picture saved.", Toast.LENGTH_SHORT).show();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String getImagePath() {
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +
                File.separator + "SAFELINE_CRANES" + File.separator + "rope_certificates" + File.separator;
        File folder = new File(directory);
        File[] files = folder.listFiles();
        for(File file : files) {
            if(file.getName().startsWith("serial_" + mRopeSerial)){
                return file.getAbsolutePath();
            }
        }
        return null;
    }
}