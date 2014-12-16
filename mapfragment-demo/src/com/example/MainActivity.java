package com.example;

import android.app.Activity;
import android.os.Bundle;
//import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import android.widget.EditText;
import android.app.FragmentManager;
import android.graphics.Color;
import android.view.Menu;
import com.google.android.gms.maps.model.LatLng;
import java.util.Locale;

public class MainActivity extends Activity {

    private MapFragment mapFragment;
    private GoogleMap map;
    //private LocationClient mLocationClient;
    private EditText editText;
    private MyView myView = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        FragmentManager f = getFragmentManager();
        mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        map = mapFragment.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.setBuildingsEnabled(true);
        if (myView == null) {
            myView = new MyView(this);
            myView.map = map;
        }
        utility.map = map;

        editText = (EditText) findViewById(R.id.edit_message);
        editText.setTextColor(Color.RED);
        editText.setText("flot");
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                //this should instead be set by an event handler for editText
                MyView.linetype = editText.getText().toString().toUpperCase(Locale.US);
                myView.onTouchEvent(point);
                return;
            }
        });
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            private float currentZoom = -1;

            @Override
            public void onCameraChange(CameraPosition pos) {
                if (pos.zoom != currentZoom) {
                    currentZoom = pos.zoom;
                }
                myView.setExtents();
                myView.DrawFromZoom(null);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        map.clear();
        MyView._points.clear();
        MyView._pointsGeo.clear();
        return false;
    }
}
