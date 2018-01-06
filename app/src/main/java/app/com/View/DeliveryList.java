package app.com.View;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import app.com.Adapter.RecyclerViewAdapter;
import app.com.ViewModel.DeliveryDataViewModel;
import app.com.model.DeliveryDataModel;
import app.com.roomlivedata.MainActivity;
import app.com.roomlivedata.R;

/*
 * Created by Yash on 7/1/18.
 */

public class DeliveryList extends Fragment
{
    Activity mactivity;
    MainActivity main_activity;

    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<DeliveryDataModel> deliveryArrayList;
    private static final String TAG = "MainActivity";

    public DeliveryList()
    {
        // Mandatory empty constructor for the fragment manager to instantiate the fragment
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mactivity = getActivity();

        main_activity = (MainActivity) mactivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.delivery_list_layout, container, false);

        RecyclerView deliveryRclv = v.findViewById(R.id.deliveryRclv);

        deliveryArrayList = new ArrayList<>();

        recyclerViewAdapter = new RecyclerViewAdapter(main_activity,deliveryArrayList);
        deliveryRclv.setLayoutManager(new LinearLayoutManager(main_activity));

        deliveryRclv.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListener(main_activity);

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

        return v;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        Log.i(TAG, "onResume: ");

        DeliveryDataViewModel viewModel = ViewModelProviders.of(this).get(DeliveryDataViewModel.class);

        viewModel.getDeliveryList(main_activity).observe(main_activity, new Observer<List<DeliveryDataModel>>()
        {
            @Override
            public void onChanged(@Nullable List<DeliveryDataModel> deliveryData)
            {
                Log.i(TAG, "onChanged: "+deliveryData);

                deliveryArrayList = (ArrayList<DeliveryDataModel>) deliveryData;

                recyclerViewAdapter.addItems(deliveryArrayList);
            }
        });
    }
}
