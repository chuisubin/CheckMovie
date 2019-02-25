package com.example.checkmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Button btn_Login_login, btn_Login_sign;
    private EditText edt_Login_Email, edt_Login_Password;
    String email;
    String password;

    private CheckBox ckBox_Login;
     private Boolean hasSave;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        btn_Login_login = (Button) findViewById(R.id.login_button_login);
        edt_Login_Email = (EditText) findViewById(R.id.login_editView_email);
        edt_Login_Password = (EditText) findViewById(R.id.login_edit_Password);
        btn_Login_sign = (Button) findViewById(R.id.login_button_signUp);
        ckBox_Login=(CheckBox)findViewById(R.id.login_checkBox);
        ckBox_Login.setOnCheckedChangeListener(ckBoxlistener);

        btn_Login_sign.setOnClickListener(btnlistener);
        btn_Login_login.setOnClickListener(btnlistener);



       SharedPreferences preferences =getSharedPreferences("Login",MODE_PRIVATE);
        String userEMAIL =preferences.getString("EMAIL","");
        hasSave=preferences.getBoolean("SAVE",false);
        String userPW=preferences.getString("PASSWORD","");
        edt_Login_Email.setText(userEMAIL);
        edt_Login_Password.setText(userPW);
        ckBox_Login.isChecked();

        authStateListener = new FirebaseAuth.AuthStateListener() {   //監測登入狀況
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("onAuthStateChanged", "登入: " + user.getUid());

                } else Log.d("onAuthStateChanged", "未登入");

            }
        };

    }


    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

    private Button.OnClickListener btnlistener = new Button.OnClickListener() {
        public void onClick(View v) {

            email = edt_Login_Email.getText().toString();
            password = edt_Login_Password.getText().toString();
            switch (v.getId()) {
                case R.id.login_button_login: {
                    if (email != null && !email.equals("") && password != null && !password.equals("")) {
                        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            public void onSuccess(AuthResult authResult) {
                                Log.d("onComplete", "登入成功");
                                closeKeyboard();
                                Toast.makeText(getApplicationContext(), "登入成功 : "+auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                                if(hasSave) {
                                    SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
                                    preferences.edit()
                                            .putBoolean("SAVE",true)
                                    .putString("EMAIL", email)
                                    .putString("PASSWORD", password).commit();
                                }else {
                                    SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
                                    preferences.edit()
                                            .clear().commit();
                                }

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();

                            }
                        });


                    } else {
                        Toast.makeText(getApplicationContext(), "不能為空值", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
                case R.id.login_button_signUp: {
                    Intent intent = new Intent(LoginActivity.this, SignActivity.class);
                    startActivity(intent);
                    break;
                }
                //case R.id.login_textView_forgetPW:{


            }
        }

    };
    private void closeKeyboard(){
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private CheckBox.OnCheckedChangeListener ckBoxlistener=new CheckBox.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(ckBox_Login.isChecked()){
                hasSave=true;
            }else {
                hasSave=false;
            }
        }
    };


}
