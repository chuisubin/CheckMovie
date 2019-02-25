package com.example.checkmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;

public class FragA extends Fragment implements ImageAdapter.onItemClickListener {
    private static FragA instance;

    public FragA() {

    }

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDbListener;
    private List<Upload> mUploads;
    private ProgressBar progressBarCircle;
    private TextView txt_nothinView;
    private FirebaseStorage mStorage;

    private View view;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_frag_a, container, false);
        initRecyclerView();
        return view;


    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragA_recycleView);
        progressBarCircle = (ProgressBar) view.findViewById(R.id.fragA_progressbarCircle);
        txt_nothinView = (TextView) view.findViewById(R.id.fragA_textview_nothingView);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layoutManager.setReverseLayout(true);//列表翻转
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);


        mUploads = new ArrayList<>();
        mAdapter = new ImageAdapter(getContext(), mUploads);
        mRecyclerView.setAdapter(mAdapter);

        Log.d("HIHI", "setU");
        Log.d("HIHI", mUploads.toString());

        mAdapter.setOnItemClickListener(FragA.this);
        if (mAdapter.getItemCount() == 0) {
            txt_nothinView.setText("沒有項目");
        }
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDbListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                    Log.d("HIHI", "setU");
                    Log.d("HIHI", mUploads.toString());
                }
                mAdapter.notifyDataSetChanged();

                progressBarCircle.setVisibility(View.INVISIBLE);
                txt_nothinView.setVisibility(View.INVISIBLE);
            }


            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                progressBarCircle.setVisibility(INVISIBLE);


            }
        });


    }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity(), "Normal click position : " + position, Toast.LENGTH_LONG).show();
        Upload selectItem = mUploads.get(position);

        final String imageUrl=selectItem.getImageUrl();
        final String userName=selectItem.getUserName();
        final String title=selectItem.getName();
        final String date=selectItem.getDate();
        final String itemKey = selectItem.getKey();
        final String info=selectItem.getInfo();
        Intent intent=new Intent(getActivity(), Image_Info.class);
        Bundle bundle=new Bundle();
        bundle.putString("imageUrl",imageUrl);
        bundle.putString("userName",userName);
        bundle.putString("title",title);
        bundle.putString("date",date);
        bundle.putString("itemKey",itemKey);
        bundle.putString("info",info);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onWhateverClick(int position) {
        Toast.makeText(getActivity(), " click position onWhatever : " + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(getActivity(), " click position Delete : " + position, Toast.LENGTH_LONG).show();
        Upload selectItem = mUploads.get(position);
        final String itemKey = selectItem.getKey();

        StorageReference reference = mStorage.getReferenceFromUrl(selectItem.getImageUrl());
        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(itemKey).removeValue();
                Toast.makeText(getContext(), "Delete", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDbListener);
    }
}






