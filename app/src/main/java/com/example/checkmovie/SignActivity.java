package com.example.checkmovie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignActivity extends AppCompatActivity {
    private Button btn_Sign_sign;
    private EditText edt_Sign_email, edt_Sign_password, edt_Sign_passwordAgain, edt_Sign_userName;
    private FirebaseUser user;

    private FirebaseAuth auth;
    DatabaseReference mDatabaseRef;
    String email, password, passwordAgain, userName;

    private Task<AuthResult> mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        findView();

        btn_Sign_sign.setOnClickListener(btnlistener);
        auth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("User");

    }

    private void findView() {
        btn_Sign_sign = (Button) findViewById(R.id.signUp_button_signUp);
        edt_Sign_email = (EditText) findViewById(R.id.signUp_editView_email);
        edt_Sign_password = (EditText) findViewById(R.id.signUp_editView_password);
        edt_Sign_passwordAgain = (EditText) findViewById(R.id.signUp_editView_passwordAgain);
        edt_Sign_userName = (EditText) findViewById(R.id.signUp_editView_userName);

    }

    private Button.OnClickListener btnlistener = new Button.OnClickListener() {
        public void onClick(View v) {
            if (checkEdt()) {
                createUser();
            }


        }
    };


    private void clearText() {
        edt_Sign_passwordAgain.setText("");
        edt_Sign_password.setText("");
    }

    private void createUser() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        mUploadTask = auth.createUserWithEmailAndPassword(email, password);
        mUploadTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 500);

                new AlertDialog.Builder(SignActivity.this)
                        .setMessage("註冊成功")
                        .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    public void onSuccess(AuthResult authResult) {
                                         user = FirebaseAuth.getInstance().getCurrentUser();
                                        Users users = new Users(userName,
                                                email, password);
                                        String uploadId= authResult.getUser().getUid();
                                        mDatabaseRef.child(uploadId).setValue(users);
                                        closeKeyboard();
                                        if (user != null) {
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(userName)
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("Sign", "更新成功"+user.getDisplayName());
                                                                Toast.makeText(getApplicationContext(),"註冊完成",Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(SignActivity.this,MainActivity.class));

                                                            }
                                                        }
                                                    });
                                        } else {
                                           Toast.makeText(getApplicationContext(),"更新用戶失敗,請重試",Toast.LENGTH_SHORT).show();
                                           Log.d("Sign","更新用戶失敗");
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"更新用戶失敗 : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                        Log.d("Sign",e.getMessage());

                                    }
                                });

                            }
                        })
                        .show();



            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                new AlertDialog.Builder(SignActivity.this)
                        .setTitle("註冊失敗")
                        .setMessage("檢查格式是否錯誤")
                        .setPositiveButton("OK", null)
                        .show();
                clearText();
            }
        });
    }

    private boolean checkEdt() {
        userName = edt_Sign_userName.getText().toString();
        email = edt_Sign_email.getText().toString();
        password = edt_Sign_password.getText().toString();
        passwordAgain = edt_Sign_passwordAgain.getText().toString();
        if (email != null && !email.equals("") && password != null && !password.equals("") && !passwordAgain.equals("")) {

            if (password.equals(passwordAgain)) {
                return true;


            } else
                Toast.makeText(getApplicationContext(), "密碼和確認密碼不一致", Toast.LENGTH_SHORT).show();
            return false;

        } else Toast.makeText(getApplicationContext(), "不能為空", Toast.LENGTH_SHORT).show();
        return false;

    }
    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
