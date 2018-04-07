package com.example.tabish.taskask;

import android.content.Intent;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

//import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

public class VicinityMap extends AppCompatActivity implements OnMapReadyCallback {

    boolean mapReady=false;
    GoogleMap m_map;
    int radiusInM;
    private Circle mapCircle;
    SeekBar mapVicinityBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vicinity_map);

        Button btnNormal = (Button) findViewById(R.id.btnNormal);
        Button btnSatellite = (Button) findViewById(R.id.btnSatellite);
        Button btnHybrid = (Button) findViewById(R.id.btnHybrid);
        mapVicinityBar = (SeekBar) findViewById(R.id.mapVicinityBar);
        radiusInM = getIntent().getIntExtra("radiusInMeters",0);
        mapVicinityBar.setProgress(radiusInM);
        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mapReady){
                    Log.e("Yes","coming");
                    m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });

        btnHybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady)
                    m_map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        btnSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady)
                    m_map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        mapReady = true;
        m_map= map;

        final LatLng habib = new LatLng(24.905294,67.138026);
        CameraPosition target = CameraPosition.builder().target(habib).zoom(17).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));

        final LatLng middle = m_map.getCameraPosition().target;

        final Marker mark = m_map.addMarker(new MarkerOptions()
                .position(middle)
                .draggable(false));
        mark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.markerx));

        CircleOptions circle = new CircleOptions()
                .center(mark.getPosition())
                .radius(radiusInM)
                .strokeWidth(0f)
                .fillColor(0x550000FF);
        mapCircle = m_map.addCircle(circle);

        final ImageView fakeMarker = (ImageView) findViewById(R.id.fakeMarker);
        fakeMarker.setVisibility(View.INVISIBLE);
        m_map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {

                mark.setVisible(false);
                fakeMarker.setVisibility(View.VISIBLE);
                mapCircle.setVisible(false);
            }
        });

        m_map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng centerNow = m_map.getCameraPosition().target;
                mark.setPosition(centerNow);
                mark.setVisible(true);
                mapCircle.setVisible(true);
                mapCircle.setCenter(centerNow);
                fakeMarker.setVisibility(View.INVISIBLE);
            }
        });







        mapVicinityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusInM = progress;
                mapCircle.setCenter(mark.getPosition());
                mapCircle.setRadius(progress);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button btnSelect = (Button) findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent output = new Intent();
                output.putExtra("Radius", radiusInM);
                output.putExtra("startingLatitude",mark.getPosition().latitude);
                output.putExtra("startingLongitude",mark.getPosition().longitude);
                setResult(RESULT_OK, output);
                finish();
            }
        });
    }
}
