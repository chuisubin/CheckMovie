package com.example.checkmovie;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapInfo extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView txt_broadwayTinShuiWai, txt_hylandTuenMun, txt_emperorTuenMun, txt_chinemaYuenLong, txt_broadwayTsuenWan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_info);



        init();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng hongkong = new LatLng(22.3738974, 114.0755071);

        LatLng broadwayTinShuiWai = new LatLng(22.4570683, 114.0032537);
        LatLng hylandTuenMun = new LatLng(22.398782, 113.9754652);
        LatLng emperorTuenMun = new LatLng(22.3907678, 113.9782311);
        LatLng chinemaYuenLong = new LatLng(22.4456765, 114.0290748);
        LatLng broadwayTsuenWan = new LatLng(22.371052, 114.1110593);

        mMap.addMarker(new MarkerOptions()
                .position(broadwayTinShuiWai)
                .title("Cinemas")
                .snippet("天水圍銀座百老匯"));
        mMap.addMarker(new MarkerOptions()
                .position(hylandTuenMun)
                .title("Cinemas")
                .snippet("屯門凱都戲院"));
        mMap.addMarker(new MarkerOptions()
                .position(emperorTuenMun)
                .title("Cinemas")
                .snippet("屯門英皇戲院"));
        mMap.addMarker(new MarkerOptions()
                .position(chinemaYuenLong)
                .title("Cinemas")
                .snippet("元朗戲院"));
        mMap.addMarker(new MarkerOptions()
                .position(broadwayTsuenWan)
                .title("Comemas")
                .snippet("荃灣百老匯戲院"));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hongkong, 11));
    }

    private void init() {
        txt_broadwayTinShuiWai = (TextView) findViewById(R.id.mapInfo_text_broadwayTinShuiWai);
        txt_hylandTuenMun = (TextView) findViewById(R.id.mapInfo_text_hylandTuenMun);
        txt_emperorTuenMun = (TextView) findViewById(R.id.mapInfo_text_emperorTuenMun);
        txt_chinemaYuenLong = (TextView) findViewById(R.id.mapInfo_text_chinemaYuenLong);
        txt_broadwayTsuenWan = (TextView) findViewById(R.id.mapInfo_text_broadwayTsuenWan);

        txt_broadwayTinShuiWai.setOnClickListener(listener);
        txt_hylandTuenMun.setOnClickListener(listener);
        txt_emperorTuenMun.setOnClickListener(listener);
        txt_chinemaYuenLong.setOnClickListener(listener);
        txt_broadwayTsuenWan.setOnClickListener(listener);
    }

    private TextView.OnClickListener listener=new TextView.OnClickListener(){
        @Override
        public void onClick(View v) {

         switch (v.getId()){
             case R.id.mapInfo_text_broadwayTinShuiWai:{
                 startActivity(new Intent(MapInfo.this,Cinemas.class));
             }
         }
        }
    };
}
