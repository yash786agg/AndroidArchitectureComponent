package app.com.View;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import app.com.roomlivedata.MainActivity;
import app.com.roomlivedata.R;

/*
 * Created by Yash on 6/1/18.
 */

public class DeliveryDetails extends Fragment implements OnMapReadyCallback
{
    private ArrayList<DeliveryDataModel> deliveryArrayList;
    int selectedPosition;

    Activity mactivity;
    MainActivity main_activity;

    private MapFragment mapFragment;
    private TextView deliveryDescrp;
    private ImageView deliveryImg, ivBackDeliver;
    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mactivity = getActivity();

        main_activity = (MainActivity) mactivity;

        if (getArguments() != null)
        {
            Log.i(TAG, "onCreate DeliveryDetails if");
            deliveryArrayList = getArguments().getParcelableArrayList("deliveryArrayList");
            selectedPosition = getArguments().getInt("selectedPosition", 0);
        }
        else
        {
            Log.i(TAG, "onCreate DeliveryDetails else");
            deliveryArrayList = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.delivery_layout, container, false);

        mapFragment = (MapFragment) main_activity.getFragmentManager().findFragmentById(R.id.mapLayout);

        Button landscape = v.findViewById(R.id.landscape);
        landscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: landscape");
                main_activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        Button potrait = v.findViewById(R.id.potrait);
        potrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: potrait");
                main_activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        ivBackDeliver = v.findViewById(R.id.ivBackDeliver);
        ivBackDeliver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moveToPreviousScreen();
            }
        });

        deliveryDescrp = v.findViewById(R.id.deliveryDescrp);

        deliveryImg = v.findViewById(R.id.deliveryImg);

        if(deliveryArrayList.size() >= 1)
        {
            setData();
        }

        return v;
    }

    private void setData()
    {
        if(deliveryArrayList.get(selectedPosition).getLat() != 0.0 && deliveryArrayList.get(selectedPosition).getLng() != 0.0)
        {
            mapFragment.getMapAsync(this);
        }

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
            Picasso.with(main_activity)
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

    public void setDescription(int Index,ArrayList<DeliveryDataModel> arrayList)
    {
        deliveryArrayList = arrayList;
        selectedPosition = Index;

        ivBackDeliver.setVisibility(View.GONE);

        setData();
    }

    private void moveToPreviousScreen()
    {
        main_activity.getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Log.i(TAG, "onDestroy: ");

        main_activity.getFragmentManager().beginTransaction().remove(mapFragment).commit();
    }

}
