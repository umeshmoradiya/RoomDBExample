package haraye.com.shraddhatestappjava;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProvider;
import haraye.com.shraddhatestappjava.model.DataModel;

public class CreateDataActivity extends AppCompatActivity {

    private enum OPERATION {
        CREATE,
        UPDATE
    }

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    private AppCompatEditText mEditTitle;
    private AppCompatEditText mEditDescription;
    private ImageView mImageCaptured;

    Uri image_uri;
    DataModel dataModel;

    private OPERATION curerntOperation = OPERATION.CREATE;

    private DataViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_data);

        viewModel = new ViewModelProvider(this).get(DataViewModel.class);

        mEditTitle = findViewById(R.id.edtx_create_title);
        mEditDescription = findViewById(R.id.edtx_create_description);
        mImageCaptured = findViewById(R.id.img_create_data);
        AppCompatButton mButtonSave = findViewById(R.id.btn_create_save);

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("DATA_ID")) {

            curerntOperation = OPERATION.UPDATE;

            int dataModelId = getIntent().getIntExtra("DATA_ID", 1);

            AsyncTask.execute(() -> {

                List<DataModel> data = viewModel.getDataById(dataModelId);
                runOnUiThread(() -> {
                    if (data != null && data.size() > 0) {
                        dataModel = data.get(0);
                        mEditTitle.setText(data.get(0).title);
                        mEditDescription.setText(data.get(0).description);
                        try {
                            File imgFile = new File(data.get(0).image);

                            if (imgFile.exists()) {

                                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                mImageCaptured.setImageBitmap(bitmap);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            });

        }

        mImageCaptured.setOnClickListener(v -> {
            //if system os is >= marshmallow, request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                PackageManager.PERMISSION_DENIED) {
                    //permission not enabled, request it
                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    //show popup to request permissions
                    requestPermissions(permission, PERMISSION_CODE);
                } else {
                    //permission already granted
                    openCamera();
                }
            } else {
                //system os < marshmallow
                openCamera();
            }
        });

        mButtonSave.setOnClickListener(v -> {
            String title = mEditTitle.getText().toString();
            Log.e("Title:", "" + mEditTitle.getText().toString());
            Log.e("Description:", "" + mEditDescription.getText().toString());
            Log.e("Image Path:", "" + image_uri);
            if (!title.isEmpty()) {
                if (curerntOperation == OPERATION.CREATE) {
                    if (image_uri != null) {

                        String filePath = saveImage(uriToBitmap(image_uri));
                        Log.e("Image File Path:", "" + filePath);
                        DataModel dataModel = new DataModel();
                        dataModel.title = title;
                        dataModel.description = mEditDescription.getText().toString();
                        dataModel.image = filePath;
                        viewModel.insert(dataModel);
                        finish();
                    } else {
                        Snackbar.make(v, "Image Required!!!", Snackbar.LENGTH_LONG).show();
                    }
                }

                if (curerntOperation == OPERATION.UPDATE && dataModel != null) {

                    dataModel.title = title;
                    dataModel.description = mEditDescription.getText().toString();

                    if (image_uri != null) {
                        String filePath = saveImage(uriToBitmap(image_uri));
                        Log.e("Image File Path:", "" + filePath);
                        dataModel.image = filePath;
                    }

                    viewModel.update(dataModel);
                    finish();
                }
            } else {
                mEditTitle.setError("Required!");
            }

        });

    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Log.e("Image Path:", "" + image_uri);

        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        Log.e("cameraIntent:::", "" + cameraIntent);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted
                    openCamera();
                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mImageCaptured.setImageURI(image_uri);
        }

    }

    private Bitmap uriToBitmap(Uri uri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
            } else {
                return MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String saveImage(Bitmap finalBitmap) {
        try {
            String root = getExternalFilesDir(null).getAbsolutePath();
            File myDir = new File(root + "/saved_images");
            myDir.mkdirs();
            String fileName = String.format("%d.jpg", System.currentTimeMillis());
            File file = new File(myDir, fileName);
            if (file.exists()) file.delete();

            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
