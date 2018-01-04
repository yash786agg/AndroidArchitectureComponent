package app.com.roomlivedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import java.util.List;
import app.com.Extras.ApiRequestSingleton;
import app.com.RoomDAO.DeliveryDataBase;
import app.com.ViewModel.DeliveryDataViewModel;
import app.com.model.DeliveryDataModel;

public class MainActivity extends AppCompatActivity
{
    private DeliveryDataViewModel viewModel;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(DeliveryDataViewModel.class);

        viewModel.getDeliveryList().observe(MainActivity.this, new Observer<List<DeliveryDataModel>>()
        {
            @Override
            public void onChanged(@Nullable List<DeliveryDataModel> deliveryData)
            {
                Log.i(TAG, "onChanged: "+deliveryData);
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
}
