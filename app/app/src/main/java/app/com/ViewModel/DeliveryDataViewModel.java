package app.com.ViewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import app.com.Extras.ApiRequestSingleton;
import app.com.Extras.ApiURL;
import app.com.Extras.ConnectivityReceiver;
import app.com.RoomDAO.DeliveryDataBase;
import app.com.model.DeliveryDataModel;
import app.com.roomlivedata.R;

/*
 * Created by Yash on 4/1/18.
 */

public class DeliveryDataViewModel extends AndroidViewModel
{
    private DeliveryDataBase databaseInstance;
    private LiveData<List<DeliveryDataModel>> deliveryList;

    public DeliveryDataViewModel(Application application)
    {
        super(application);

        /*
         *  Intialization of Database Instance.
         **/

        databaseInstance = DeliveryDataBase.getDatabaseInstance(this.getApplication());

        /*
         *  Fetching all existing data from Room database for the user if the data is already available to the user.
         **/

        deliveryList = databaseInstance.deliveryDataDaoAccess().fetchAllData();
    }

    public LiveData<List<DeliveryDataModel>> getDeliveryList()
    {
        if (deliveryList == null)
        {
            /*
             *  Intialization of MutableLiveData.
             */
            deliveryList = new MutableLiveData<>();
        }

        /*
         *  Network connectivity check to check whether Internet is connected or not.
         */

        networkCheck();

        return deliveryList;
    }

    private void networkCheck()
    {
        if(ConnectivityReceiver.isNetworkAvailable(this.getApplication()))
        {
            /*
            * isNetworkAvailable will check whether device is connected to wifi or data connection
            * If yes then will move to next step otherwise display a message "Please Check Your Internet Connection"*/

            getData();
        }
        else
        {
            Toast.makeText(this.getApplication(),R.string.ConnectWifiDataConn,Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DeliveryDataAsync extends AsyncTask<JSONArray, Void, Void>
    {
        // DeliveryDataAsync is used to perform database operations like insert, update from the table on background thread.

        // Note: /** DO NOT PERFORM OPERATION ON MAIN THREAD App will crash. */

        // Do implement in the following way.

        private DeliveryDataBase db;

        DeliveryDataAsync(DeliveryDataBase appDatabase)
        {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(JSONArray ... jsonArray)
        {
            int itemCount;
            JSONArray response = jsonArray[0];

            try
            {
                if(response.length() >= 1)
                {
                    for(int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObjectDelivery = response.getJSONObject(i);

                        DeliveryDataModel deliveryDataModel = new DeliveryDataModel();

                        if(jsonObjectDelivery.has("description"))
                        {
                            if (!(jsonObjectDelivery.isNull("description")))
                            {
                                deliveryDataModel.setDescription(jsonObjectDelivery.getString("description"));
                            }
                        }

                        if(jsonObjectDelivery.has("imageUrl"))
                        {
                            if (!(jsonObjectDelivery.isNull("imageUrl")))
                            {
                                deliveryDataModel.setImageUrl(jsonObjectDelivery.getString("imageUrl"));
                            }
                        }

                        if(jsonObjectDelivery.has("location"))
                        {
                            if (!(jsonObjectDelivery.isNull("location")))
                            {
                                JSONObject jsonObjectLocation = jsonObjectDelivery.getJSONObject("location");

                                if(jsonObjectLocation.has("lat"))
                                {
                                    if (!(jsonObjectLocation.isNull("lat")))
                                    {
                                        deliveryDataModel.setLat(jsonObjectLocation.getDouble("lat"));
                                    }
                                }

                                if(jsonObjectLocation.has("lng"))
                                {
                                    if (!(jsonObjectLocation.isNull("lng")))
                                    {
                                        deliveryDataModel.setLng(jsonObjectLocation.getDouble("lng"));
                                    }
                                }

                                if(jsonObjectLocation.has("address"))
                                {
                                    if (!(jsonObjectLocation.isNull("address")))
                                    {
                                        deliveryDataModel.setAddress(jsonObjectLocation.getString("address"));
                                    }
                                }
                            }
                        }

                        // itemCount will give you the count of existing item based on Primary key is. slNo in data.
                        // slNo is getting compared with arraylist item position ie: i+1.
                        // As there is no unique or primary key is provided by api request so to avoid adding of duplicate
                        // data i came up with this solution as there is no pagination is happening and fixed data is coming.

                        itemCount = db.deliveryDataDaoAccess().getDeliveryById(i+1).getCount();

                        //Now access all the methods defined in DaoAccess with DeliveryDataBase object

                        if(itemCount == 0)
                        {
                            // insertDeliveryList to insert new data in Database.
                            db.deliveryDataDaoAccess().insertDeliveryList(deliveryDataModel);
                        }
                        else
                        {
                            // updateDeliveryList to update existing data in Database.
                            db.deliveryDataDaoAccess().updateDeliveryList(deliveryDataModel);
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void getData()
    {
        // Do an asyncronous operation to fetch data.

        JsonArrayRequest mJsonObjectRequest = new JsonArrayRequest(Request.Method.GET, ApiURL.deliveriesGetURL, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                setAllData(response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {}
        });
        mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ApiRequestSingleton.getInstance(this.getApplication()).addToRequestQueue(mJsonObjectRequest);
    }

    private void setAllData(JSONArray response)
    {
        // Passing data to DeliveryDataAsync for crud operation on database.
        new DeliveryDataAsync(databaseInstance).execute(response);
    }
}
