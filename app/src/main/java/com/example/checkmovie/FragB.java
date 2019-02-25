package com.example.checkmovie;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;


public class FragB extends Fragment {
    private static final int PICK_IMAGES_REQUEST = 10;
    private Button btn_choose_Image, btn_firebase_upload;
    private EditText edt_Image_filename, edt_info;
    private ImageView img_firebase_upload;


    private Uri mImageUri;

    private StorageTask<UploadTask.TaskSnapshot> mUploadTask;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private String userName;
    private String date,info;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_b, container, false);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final Context context = getActivity();
        user = auth.getCurrentUser();
        if (user != null) {
            userName = user.getDisplayName();
        }
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads/");

        init(view);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void init(View view) {
        btn_choose_Image = (Button) view.findViewById(R.id.firebaseUpload_button_ChooseImages);
        btn_firebase_upload = (Button) view.findViewById(R.id.firebaseUpload_button_upload);
        edt_Image_filename = (EditText) view.findViewById(R.id.firebaseUpload_editView_filename);
        img_firebase_upload = (ImageView) view.findViewById(R.id.firebaseUpload_imageView_image);
        edt_info = (EditText) view.findViewById(R.id.fragB_edt_info);
        edt_info.setHorizontallyScrolling(false);


        btn_firebase_upload.setOnClickListener(listener);
        btn_choose_Image.setOnClickListener(listener);


        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                //未取得權限
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.CAMERA, Manifest.permission.READ_CALENDAR}, PICK_IMAGES_REQUEST);
            } else {

            }

        }

    }

    private Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.firebaseUpload_button_ChooseImages: {

                    openFileChoose();
                }
                break;
                case R.id.firebaseUpload_button_upload:
                    if (mUploadTask != null && mUploadTask.isInProgress()) {

                    } else
                        uploadFile();

            }
        }
    };

    private void cleanEdit() {
        edt_Image_filename.setText("");
        edt_info.setText("");
    }


    private void openFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGES_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(getContext()).load(mImageUri).into(img_firebase_upload); //超好用
        }

    }

    // private String getFileExtension(Uri uri) {
    //ContentResolver cr = getContentResolver;
    // MimeTypeMap mime = MimeTypeMap.getSingleton();
    //   return mime.getExtensionFromMimeType(cr.getType(uri));
    //    return
    //}

    private void uploadFile() {
        closeKeyboard();
        if (mImageUri != null) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            date = (year + "年" + month + "月" + day + "日" + hour + "小時");
            info=edt_info.getText().toString();


            final StorageReference spacRef = mStorageRef.child("images/" + System.currentTimeMillis() + ".jpg"); //img url地址


            mUploadTask = spacRef.putFile(mImageUri);
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            mUploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 500);


                    Toast.makeText(getContext(), "上傳成功", Toast.LENGTH_SHORT).show();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                    progressDialog.setMessage("Uploaded" + (int) progress + "%");


                }
            });


            mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return spacRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Upload upload = null;
                        if (downloadUri != null) {
                            upload = new Upload(edt_Image_filename.getText().toString().trim(),
                                    downloadUri.toString(), userName, date,info);
                        }
                        String uploadId = mDatabaseRef.push().getKey();
                        Log.d("HIHI", "  DATE: " + date + " USERNAME : " + userName);

                        mDatabaseRef.child(uploadId).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("HIHI","數據上傳完成");
                                cleanEdit();
                            }
                        });


                    } else {
                        Toast.makeText(getContext(), "已有權限", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        } else Toast.makeText(getContext(), "no Image", Toast.LENGTH_SHORT).show();

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_IMAGES_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    new AlertDialog.Builder(getContext()).setMessage("必須取得權限才能獲取相片")
                            .setPositiveButton("OK", null)
                            .show();

                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void closeKeyboard() {


        InputMethodManager inputMethodManager = (InputMethodManager) getView().getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }

    }


}


