package app.com.roomlivedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import app.com.Adapter.RecyclerViewAdapter;
import app.com.Extras.ApiRequestSingleton;
import app.com.Extras.RecyclerView_ItemClickListener;
import app.com.RoomDAO.DeliveryDataBase;
import app.com.ViewModel.DeliveryDataViewModel;
import app.com.model.DeliveryDataModel;

public class MainActivity extends AppCompatActivity implements RecyclerView_ItemClickListener
{
    private DeliveryDataViewModel viewModel;
    private RecyclerView deliveryRclv;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<DeliveryDataModel> deliveryArrayList;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deliveryRclv = findViewById(R.id.deliveryRclv);

        deliveryArrayList = new ArrayList<>();

        recyclerViewAdapter = new RecyclerViewAdapter(this,deliveryArrayList);
        deliveryRclv.setLayoutManager(new LinearLayoutManager(this));

        deliveryRclv.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListener(this);

        viewModel = ViewModelProviders.of(this).get(DeliveryDataViewModel.class);

        viewModel.getDeliveryList().observe(MainActivity.this, new Observer<List<DeliveryDataModel>>()
        {
            @Override
            public void onChanged(@Nullable List<DeliveryDataModel> deliveryData)
            {
                Log.i(TAG, "onChanged: "+deliveryData);

                deliveryArrayList = (ArrayList<DeliveryDataModel>) deliveryData;

                recyclerViewAdapter.addItems(deliveryArrayList);
            }
        });

        /*LiveData<List<DeliveryDataModel>> deliveryLiveData = DeliveryDataBase.getDatabaseInstance(this).deliveryDataDaoAccess().fetchAllData();
        deliveryLiveData.observe(this, new Observer<List<DeliveryDataModel>>()
        {
            @Override
            public void onChanged(@Nullable List<DeliveryDataModel> universities)
            {
                Log.i(TAG, "onChanged: "+universities);
            }
        });*/
    }

    /*private void networkCheck()
    {
        if(ConnectivityReceiver.isNetworkAvailable(this))
        {
            ConnectivityReceiver.speedTest(new ConnectivityReceiver.AsyncResponse()
            {
                @Override
                public void processFinish(boolean output)
                {
                    if (output)
                    {

                        *//* * If The output Value --->"True" Then Only login_request will Execute
                         * NOTE: Here "True" Value Means Intenet is Working*//*

                        getAppVersion();

                        getBaneerImage();
                    }
                    else
                    {
                        Utility.SnackbarPopup(this, MainActivity.this.getResources().getString(R.string.Please_Check_Your_Internet_Connection),2,splash_coordinatorlayout);
                    }
                }
            });
        }
        else
        {
            Utility.SnackbarPopup(this, MainActivity.this.getResources().getString(R.string.Please_Connect_to_your_Wifi_or_Data_Connection),2,splash_coordinatorlayout);
        }
    }*/



    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        DeliveryDataBase.destroyDatabaseInstance();
    }

    @Override
    public void onItemClick(View view, int position)
    {
        Intent deliveryintent = new Intent(MainActivity.this,DeliveryDetails.class);

        if (deliveryArrayList.size() >=1)
        {
            /*
            * Passing of ParcelableArrayList it will help as to take data to next screen
            * It also reduce time and effect to call the Api Request to server for each product.
            * */

            deliveryintent.putParcelableArrayListExtra("deliveryArrayList",deliveryArrayList);
            deliveryintent.putExtra("selectedPosition",position);
        }
        startActivity(deliveryintent);
    }
}