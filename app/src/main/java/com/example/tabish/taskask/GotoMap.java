package com.example.tabish.taskask;

import android.content.Intent;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

public class GotoMap extends AppCompatActivity implements OnMapReadyCallback {

    boolean mapReady=false;
    GoogleMap m_map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goto_map);

        Button btnNormal = (Button) findViewById(R.id.btnNormal);
        Button btnSatellite = (Button) findViewById(R.id.btnSatellite);
        Button btnHybrid = (Button) findViewById(R.id.btnHybrid);

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
        LatLng habib = new LatLng(24.905294,67.138026);
        CameraPosition target = CameraPosition.builder().target(habib).zoom(17).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        final Marker mark = m_map.addMarker(new MarkerOptions()
                .position(habib)
                .draggable(true));
        mark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.markerx));


        m_map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Double lat = marker.getPosition().latitude;
                Double lon = marker.getPosition().longitude;
                Log.e("Latitude ",Double.toString(lat));
                Log.e("Longitude ",Double.toString(lon));

            }
        });


        Button btnSelect = (Button) findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng position = mark.getPosition();
                Double latX = position.latitude;
                Double lonX = position.longitude;
                Intent output = new Intent();
                output.putExtra("Latitude", latX);
                output.putExtra("Longitude", lonX);
                setResult(RESULT_OK, output);
                finish();
            }
        });
    }
}
