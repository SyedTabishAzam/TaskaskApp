package com.example.tabish.taskask;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewCheckMap extends Activity implements OnMapReadyCallback {

    boolean mapReady=false;
    GoogleMap m_map;
    double latD = 0;
    double lonD = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_check_map);

        String lat = getIntent().getStringExtra("Latitude");
        String lon = getIntent().getStringExtra("Longitude");
        latD = Double.parseDouble(lat);
        lonD = Double.parseDouble(lon);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.checkMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        mapReady = true;
        m_map= map;
        LatLng location = new LatLng(latD,lonD);
        CameraPosition target = CameraPosition.builder().target(location).zoom(17).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        Marker mark = m_map.addMarker(new MarkerOptions()
                .position(location)
                .draggable(false));
        mark.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.markerx));

    }
}
