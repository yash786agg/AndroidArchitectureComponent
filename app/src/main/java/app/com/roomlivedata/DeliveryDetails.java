package app.com.roomlivedata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import app.com.model.DeliveryDataModel;

/*
 * Created by Yash on 6/1/18.
 */

public class DeliveryDetails extends AppCompatActivity implements OnMapReadyCallback
{
    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private ArrayList<DeliveryDataModel> deliveryArrayList;
    int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_layout);

        deliveryArrayList = getIntent().getParcelableArrayListExtra("deliveryArrayList");

        selectedPosition = getIntent().getIntExtra("selectedPosition", 0);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapLayout);

        if(deliveryArrayList.get(selectedPosition).getLat() != 0.0 && deliveryArrayList.get(selectedPosition).getLng() != 0.0)
        {
            mapFragment.getMapAsync(this);
        }

        ImageView ivBackDeliver = findViewById(R.id.ivBackDeliver);
        ivBackDeliver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moveToPreviousScreen();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        LatLng latlng_object = new LatLng(deliveryArrayList.get(selectedPosition).getLat(),
                deliveryArrayList.get(selectedPosition).getLng());

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng_object, 10));

        map.addMarker(new MarkerOptions()
                .title(deliveryArrayList.get(selectedPosition).getDescription())
                .snippet(deliveryArrayList.get(selectedPosition).getAddress())
                .position(latlng_object));

        map.moveCamera(CameraUpdateFactory.newLatLng(latlng_object));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onBackPressed()
    {
       moveToPreviousScreen();
    }

    private void moveToPreviousScreen()
    {
        finish();
    }
}
