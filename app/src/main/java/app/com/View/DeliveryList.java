package app.com.View;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
    // DeliveryList Fragment will show the list of deliveries.

    Activity mactivity;
    MainActivity main_activity;

    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<DeliveryDataModel> deliveryArrayList;
    private ProgressBar progressBar;

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

        /*
         *  Intialization of ProgressBar
         **/

        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        /*
         *  Intialization of RecyclerView
         **/

        RecyclerView deliveryRclv = v.findViewById(R.id.deliveryRclv);

         /*
         *  Intialization of ArrayList
         **/

        deliveryArrayList = new ArrayList<>();

        recyclerViewAdapter = new RecyclerViewAdapter(main_activity,deliveryArrayList);
        deliveryRclv.setLayoutManager(new LinearLayoutManager(main_activity));

        /*
         *  Above we used LinearLayoutManager to show Data in Listview Form
         *  Similarly GridLayoutManager --> Is used to Display Data in Gridview with same Size of Columns.
         *  Similarly StaggeredGridLayoutManager --> Is used to Display Data in Gridview with Different Size of Columns
         *  according to size of image or data.
         */

        deliveryRclv.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListener(main_activity);

        return v;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        // ViewModel observer method is used to connect the View Model ie: DeliveryDataViewModel class to View class ie Activity
        // or Fragment in our case we are using Fragment ie : DeliveryList to get new or updated data from server or Api request
        // and passing the data in form of Arraylist to RecyclerViewAdapter class to set the data in a cell.

        // Basic Functionality of ViewModel is to observer the data change and lifecycle in conscious way and survive configuration
        // changes on screen rotations.

        DeliveryDataViewModel viewModel = ViewModelProviders.of(this).get(DeliveryDataViewModel.class);

        viewModel.getDeliveryList().observe(main_activity, new Observer<List<DeliveryDataModel>>()
        {
            @Override
            public void onChanged(@Nullable List<DeliveryDataModel> deliveryData)
            {
                if (deliveryData != null)
                {
                    // For the first time when data is not available then progressBar will visible to user.But on second time
                    // when user visit our application then at that time there is no need to wait for the user. Exiting data is
                    // always visible for the user and new data will get synchronized to the existing one.

                    if(deliveryData.size() == 0)
                    {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        if(progressBar != null)
                        {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }

                // Update the UI.

                deliveryArrayList = (ArrayList<DeliveryDataModel>) deliveryData;

                recyclerViewAdapter.addItems(deliveryArrayList);
            }
        });
    }
}
