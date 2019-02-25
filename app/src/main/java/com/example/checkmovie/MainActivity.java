package com.example.checkmovie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    public MainActivity(){

    }

    //==========Fragment==========//
    private FragA fragA;
    private FragB fragB;
    private FragC fragC;
    private ViewPager viewPager;
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentPagerAdapter fragmentPageAdapter;
    public final int requestCode = 1;
    DatabaseReference mDatabaseRef;

    private DrawerLayout drawerLayout;


    //==========================下選單===================//
    private RadioButton rbtn1, rbtn2, rbtn3;
    private RadioGroup rbtnGroup;
    private String userName;


    private Button nav_button_login;
    private Button nav_button_info;

    //=============================監聽登入情況=============//
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    private boolean islogin;


    //=======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//===========FindView=============//
        initFragment();

        init();

        nav_button_login.setOnClickListener(nav_listener);
        nav_button_info.setOnClickListener(nav_listener);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    islogin = true;
                    Log.d("HIHI", "登入: " + user.getEmail());
                    userName = user.getDisplayName();
                    Log.d("HIHI", "登入: " + user.getDisplayName());
                } else {
                    userName = "未登入";
                    islogin = false;
                }
                nav_button_login.setText(userName);
            }
        };



        //Toolbar聲明,,取代原本的 actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.appMain_toolbar_view);
        setSupportActionBar(toolbar);


        //左滑選單

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }




    public void init() {
        nav_button_login = (Button) findViewById(R.id.nav_button_Login);
        nav_button_info = (Button) findViewById(R.id.nav_button_info);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    }



    private void initFragment() {
        viewPager = (ViewPager) findViewById(R.id.appMain_viewPager_view);
        fragA = new FragA();
        fragB = new FragB();
        fragC = new FragC();
        rbtnGroup = (RadioGroup) findViewById(R.id.appMain_radioButtonGroup_group);
        rbtn1 = (RadioButton) findViewById(R.id.appMain_radoButton_fragA);
        rbtn2 = (RadioButton) findViewById(R.id.rappMain_radoButton_fragB);
        rbtn3 = (RadioButton) findViewById(R.id.appMain_radoButton_fragC);
        mFragmentList.add(fragA);
        mFragmentList.add(fragB);
        mFragmentList.add(fragC);

        fragmentPageAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            public Fragment getItem(int i) {
                return mFragmentList != null ? mFragmentList.get(i) : null;
            }

            public int getCount() {
                return mFragmentList != null ? mFragmentList.size() : 0;

            }
        };
        viewPager.setAdapter(fragmentPageAdapter);
        viewPager.setCurrentItem(0);
       // viewPager.setOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);
        rbtnGroup.setOnCheckedChangeListener(this);
        rbtn1.setChecked(true);  //首頁

    }


    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.appMain_radoButton_fragA:
                viewPager.setCurrentItem(0);
                break;

            case R.id.rappMain_radoButton_fragB:
                viewPager.setCurrentItem(1);
                break;

            case R.id.appMain_radoButton_fragC:
                viewPager.setCurrentItem(2);
                break;

        }

    }


    public void onPageScrolled(int i, float v, int i1) {

    }

    public void onPageSelected(int i) {

    }

    public void onPageScrollStateChanged(int state) { //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (viewPager.getCurrentItem()) {
                case 0:
                    rbtn1.setChecked(true);
                    break;
                case 1:
                    rbtn2.setChecked(true);
                    break;
                case 2:
                    rbtn3.setChecked(true);
                    break;

            }
        }
    }


    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private Button.OnClickListener nav_listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nav_button_Login: {

                    if (!islogin) {
                        closeDrawer();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("你現已登入,是否需要登出")
                               .setPositiveButton("不用", null)
                                .setNegativeButton("是的,我要登出", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                        auth.signOut();
                                        auth.addAuthStateListener(authStateListener);
                                    }
                               }).show();
                    }
                }
                break;
                case R.id.nav_button_info:{
                    startActivity(new Intent(MainActivity.this,MapInfo.class));

                }break;
            }
        }

    };

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


    protected void onDestroy() {
      //  auth.signOut();
        super.onDestroy();
    }

    public void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
    }


}
