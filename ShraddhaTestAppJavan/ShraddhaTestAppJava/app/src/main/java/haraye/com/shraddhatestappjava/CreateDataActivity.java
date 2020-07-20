package haraye.com.shraddhatestappjava;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import haraye.com.shraddhatestappjava.model.AttachmentModel;
import haraye.com.shraddhatestappjava.model.DataImage;
import haraye.com.shraddhatestappjava.model.DataModel;
import haraye.com.shraddhatestappjava.repository.DataRepository;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


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
    private  ImageView mAudioRecorder;
    private GridView imageGridView;

    boolean isPressed=false;

    Uri image_uri;
    DataModel dataModel;
    AttachmentModel attachmentModel;
    ImageAdapter imageAdapter = null;

    private OPERATION curerntOperation = OPERATION.CREATE;

    private DataViewModel viewModel;

    Button mButtonStart, mButtonStop, mButtonPlayLastRecordAudio, mButtonStopPlayingRecording ;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_data);

        viewModel = new ViewModelProvider(this).get(DataViewModel.class);

        mEditTitle = findViewById(R.id.edtx_create_title);
        mEditDescription = findViewById(R.id.edtx_create_description);
        mImageCaptured = findViewById(R.id.img_create_data);

        AppCompatButton mButtonSave = findViewById(R.id.btn_create_save);

        mButtonStart = (Button) findViewById(R.id.btn_start_record_audio);
        mButtonStop = (Button) findViewById(R.id.btn_stop_record_audio);
        mButtonPlayLastRecordAudio = (Button) findViewById(R.id.btn_start_play_audio);
        mButtonStopPlayingRecording = (Button)findViewById(R.id.btn_stop_play_audio);
        imageGridView = (GridView)findViewById(R.id.grid_create_data);

        mButtonStart.setEnabled(false);
        mButtonStop.setEnabled(false);
        mButtonPlayLastRecordAudio.setEnabled(false);
        mButtonStopPlayingRecording.setEnabled(false);

        random = new Random();
        mAudioRecorder = findViewById(R.id.img_create_audio);

        imageAdapter = new ImageAdapter(this);
        imageGridView.setAdapter(imageAdapter);

        mAudioRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPressed){
                    mAudioRecorder.setImageResource(R.drawable.ic_baseline_mic_24);
                    mButtonStart.setEnabled(true);
                    isPressed = true;
                }else{
                    mAudioRecorder.setImageResource(R.drawable.ic_baseline_mic_off_24);
                    mButtonStart.setEnabled(false);
                    isPressed = false;
                }
            }
        });

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";
                    Log.e("Audio_Save_Path:::", ""+AudioSavePathInDevice);

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    mButtonStart.setEnabled(false);
                    mAudioRecorder.setImageResource(R.drawable.ic_baseline_mic_24);
                    mButtonStop.setEnabled(true);

                    Toast.makeText(CreateDataActivity.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }

            }
        });

        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                mButtonStop.setEnabled(false);
                mButtonPlayLastRecordAudio.setEnabled(true);
                mButtonStart.setEnabled(true);
                mAudioRecorder.setImageResource(R.drawable.ic_baseline_mic_off_24);
                mButtonStopPlayingRecording.setEnabled(false);

                Toast.makeText(CreateDataActivity.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        mButtonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                mButtonStop.setEnabled(false);
                mButtonStart.setEnabled(false);
                mAudioRecorder.setImageResource(R.drawable.ic_baseline_mic_off_24);
                mButtonStopPlayingRecording.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(CreateDataActivity.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        mButtonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonStop.setEnabled(false);
                mButtonStart.setEnabled(true);
                mAudioRecorder.setImageResource(R.drawable.ic_baseline_mic_off_24);
                mButtonStopPlayingRecording.setEnabled(false);
                mButtonPlayLastRecordAudio.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });



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
                            List<DataImage> tempImages = new ArrayList<>();

                            for(String imagePath : data.get(0).image){

                                File imgFile = new File(imagePath);

                                if (imgFile.exists()) {
                                    Uri imageUri = Uri.fromFile(imgFile);
                                    tempImages.add(new DataImage("",imageUri, true));
                                }
                            }

                            imageAdapter.addAllImages(tempImages);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            File audioFile = new File(data.get(0).audio.get(0));

                            if (audioFile.exists()) {
                                Log.e("Set_Audio_File:", ""+audioFile);

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
            //Log.e("Image Path:", "" + image_uri);
            if (!title.isEmpty()) {
                if (curerntOperation == OPERATION.CREATE) {
                    if (imageAdapter.getAllImages() != null && AudioSavePathInDevice != null) {

                        String audioPath = AudioSavePathInDevice;
                        DataModel dataModel = new DataModel();
                        dataModel.title = title;
                        dataModel.description = mEditDescription.getText().toString();


                            viewModel.insert(dataModel, new DataRepository.InsertListener() {
                                @Override
                                public void onSuccess(long id) {

                                    for(DataImage dataImage: imageAdapter.getAllImages()) {
                                        if(!dataImage.isSaved()){
                                        String filePath = saveImage(uriToBitmap(dataImage.getImageUri()));
                                        Log.e("Image File Path:", "" + filePath);

                                        AttachmentModel attachmentModel = new AttachmentModel();
                                        attachmentModel.source = filePath;
                                        attachmentModel.type = "image";
                                        attachmentModel.data_model_id = id;
                                        viewModel.insert(attachmentModel);
                                        }
                                    }
                                    
                                    AttachmentModel attachmentModel1 = new AttachmentModel();
                                    attachmentModel1.source = audioPath;
                                    attachmentModel1.type = "audio";
                                    attachmentModel1.data_model_id = id;
                                    viewModel.insert(attachmentModel1);

                                }
                            });


                        finish();
                    } else {
                        Snackbar.make(v, "Image Required!!!", Snackbar.LENGTH_LONG).show();
                    }

                }

                if (curerntOperation == OPERATION.UPDATE && dataModel != null) {

                    dataModel.title = title;
                    dataModel.description = mEditDescription.getText().toString();

                    for(DataImage dataImage: imageAdapter.getAllImages()) {
                        if(!dataImage.isSaved()){
                            String filePath = saveImage(uriToBitmap(dataImage.getImageUri()));
                            Log.e("Image File Path:", "" + filePath);

                            AttachmentModel attachmentModel = new AttachmentModel();
                            attachmentModel.source = filePath;
                            attachmentModel.type = "image";
                            attachmentModel.data_model_id = dataModel.dataid;
                            viewModel.insert(attachmentModel);
                        }
                    }

                    /*AttachmentModel attachmentModel1 = new AttachmentModel();
                    attachmentModel1.source = audioPath;
                    attachmentModel1.type = "audio";
                    attachmentModel1.data_model_id = id;
                    viewModel.insert(attachmentModel1);*/

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
        Uri image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Log.e("Image Path:", "" + image_uri);
        this.image_uri = image_uri;

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
            DataImage dataImage = new DataImage("", image_uri, false);
            imageAdapter.addData(dataImage);
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

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(CreateDataActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }


}
