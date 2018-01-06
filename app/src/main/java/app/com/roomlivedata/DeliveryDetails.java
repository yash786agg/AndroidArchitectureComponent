package app.com.roomlivedata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

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

        TextView deliveryDescrp = findViewById(R.id.deliveryDescrp);

        ImageView deliveryImg = findViewById(R.id.deliveryImg);

        if (!TextUtils.isEmpty(deliveryArrayList.get(selectedPosition).getDescription()) && !TextUtils.isEmpty(deliveryArrayList.get(selectedPosition).getAddress()))
        {
            String deliveryAddress = deliveryArrayList.get(selectedPosition).getDescription()+" "+this.getResources().getString(R.string.at)+" "+deliveryArrayList.get(selectedPosition).getAddress();

            deliveryDescrp.setText(deliveryAddress);
        }
        else if(!TextUtils.isEmpty(deliveryArrayList.get(selectedPosition).getDescription()))
        {
            String deliveryAddress = deliveryArrayList.get(selectedPosition).getDescription();

            deliveryDescrp.setText(deliveryAddress);
        }
        else
        {
            deliveryDescrp.setText(this.getResources().getString(R.string.addressNotAvailable));
        }

        if (!TextUtils.isEmpty(deliveryArrayList.get(selectedPosition).getImageUrl()))
        {
            Picasso.with(this)
                    .load("https://asia-public.foodpanda.com/dynamic/production/in/images/vendors/s5gg_sqp.jpg?v=20170908125114")
                    .into(deliveryImg);
        }
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
