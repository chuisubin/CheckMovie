package com.example.checkmovie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Image_Info extends AppCompatActivity {
    private ImageView selectImage;
    private TextView title, userName, date, info;
    private String settitle, setImageUrl, setdate, setinfo, setuserName, itemKey;
    private ListView info_ListView;
    private DatabaseReference mDatabaseRef;
    private String replyDate;
    ValueEventListener mDbListener;

    private Button button_reply;
    private EditText edit_reply;
    private SimpleDateFormat simpleDateFormat;

    ReplyAdapter mAdapter;
    private List<ReplyMessage> mReply;

    boolean islogin = false;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    private String replyUser;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_image);
        init();

        updateReply();
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(
                    @NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    replyUser = user.getDisplayName();
                    Log.d("HIHI", "登入2:" +
                            replyUser);
                    islogin = true;
                } else {
                    Log.d("HIHI", "已登出");
                }
            }
        };


    }

    private void updateReply() {
        mReply = new ArrayList<>();
        mAdapter = new ReplyAdapter(Image_Info.this, mReply);
        info_ListView.setAdapter(mAdapter);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads/" + itemKey + "/reply");
        Log.d("HIHI", "setAdapter 1");
        Log.d("HIHI", itemKey);
        mDbListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mReply.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ReplyMessage replyMessage = postSnapshot.getValue(ReplyMessage.class);
                    if (replyMessage != null) {
                        replyMessage.setReplyKey(postSnapshot.getKey());
                    }
                    mReply.add(replyMessage);
                    Log.d("HIHI", "setAdapter 2");
                    Log.d("HIHI", mReply.toString());
                }

                Log.d("HIHI", mReply.size() + " ");
                mAdapter.notifyDataSetChanged();
                if (mAdapter != null) {
                    int totalHeight = 0;
                    for (int i = 0; i < mAdapter.getCount(); i++) {
                        View listItem = mAdapter.getView(i, null, info_ListView);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();
                    }
                    ViewGroup.LayoutParams params = info_ListView.getLayoutParams();
                    params.height = totalHeight + (info_ListView.getDividerHeight() * (mAdapter.getCount() - 1));
                    info_ListView.setLayoutParams(params);
                }

            }


            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Image_Info.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void init() {
        title = (TextView) findViewById(R.id.imageInfo_textView_title);
        userName = (TextView) findViewById(R.id.imageInfo_textView_UserName);
        date = (TextView) findViewById(R.id.imageInfo_textView_uplaodDate);
        selectImage = (ImageView) findViewById(R.id.imageInfo_imageView_image);
        info_ListView = (ListView) findViewById(R.id.imageInfo_listView);
        button_reply = (Button) findViewById(R.id.imageInfo_Button_reply);
        edit_reply = (EditText) findViewById(R.id.imageInfo_edit_reply);
        info=(TextView)findViewById(R.id.imageInfo_textView_imageInfo);
        button_reply.setOnClickListener(replylistener);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            setImageUrl = bundle.getString("imageUrl");
            setdate = bundle.getString("date");
            settitle = bundle.getString("title");
            setuserName = bundle.getString("userName");
            itemKey = bundle.getString("itemKey");
            setinfo=bundle.getString("info");
            title.setText(settitle);
            date.setText(setdate);
            userName.setText(setuserName);
            userName.setText(setuserName);
            info.setText(setinfo);

            Picasso.with(Image_Info.this)
                    .load(setImageUrl)
                    .fit()
                    .placeholder(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(selectImage);


        }

    }

    private Button.OnClickListener replylistener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (islogin) {
                if (edit_reply != null) {
                    Log.d("HIIHI", "upload前");
                    uploadReply();
                    closeKeyboard();
                    editClear();
                } else {
                    Toast.makeText(getApplicationContext(), "請先登入", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    @SuppressLint("SimpleDateFormat")
    private void uploadReply() {

        simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        Log.d("HIHI", "DATE: " + date);
        replyDate = simpleDateFormat.format(date);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads/" + itemKey + "/reply/");


        final ReplyMessage replyMessage = new ReplyMessage(replyUser, replyDate, edit_reply.getText().toString());
        String uploadId = mDatabaseRef.push().getKey();
        if (uploadId != null) {
            mDatabaseRef.child(uploadId).setValue(replyMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("HIHI", "成功" + replyMessage);
                }
            });

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);

    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void editClear() {
        edit_reply.setText("");
    }


}
