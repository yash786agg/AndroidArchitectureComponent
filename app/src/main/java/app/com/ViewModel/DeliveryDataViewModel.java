package app.com.ViewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;
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
import app.com.RoomDAO.DeliveryDataBase;
import app.com.model.DeliveryDataModel;

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

        databaseInstance = DeliveryDataBase.getDatabaseInstance(this.getApplication());

        deliveryList = databaseInstance.deliveryDataDaoAccess().fetchAllData();
    }

    public LiveData<List<DeliveryDataModel>> getDeliveryList()
    {
        Log.i("Main", "getDeliveryList: ");
        if (deliveryList == null)
        {
            Log.i("Main", "getDeliveryList if: ");
            deliveryList = new MutableLiveData<>();
        }

        getCropData();

        return deliveryList;
    }

    @SuppressLint("StaticFieldLeak")
    private class DeliveryDataAsync extends AsyncTask<JSONArray, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(JSONArray ... jsonArray)
        {
            int itemCount = 0;
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

                        Log.i("Main", "doInBackground: "+i);

                        itemCount = databaseInstance.deliveryDataDaoAccess().getDeliveryById(i+1).getCount();

                        if(itemCount == 0)
                        {
                            Log.i("Main", "doInBackground if");
                            databaseInstance.deliveryDataDaoAccess().insertDeliveryList(deliveryDataModel);
                        }
                        else
                        {
                            Log.i("Main", "doInBackground else");
                            databaseInstance.deliveryDataDaoAccess().updateDeliveryList(deliveryDataModel);
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

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            //To after addition operation here.
        }
    }

    private void getCropData()
    {
        JsonArrayRequest mJsonObjectRequest = new JsonArrayRequest(Request.Method.GET, ApiURL.deliveriesGetURL, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                Log.i("Main", "onResponse: "+response);
                setAllData(response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("Main", "onResponse error: "+error.getMessage()
                );
            }
        });
        mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ApiRequestSingleton.getInstance(this.getApplication()).addToRequestQueue(mJsonObjectRequest);
    }

    private void setAllData(JSONArray response)
    {
        new DeliveryDataAsync().execute(response);
    }

}
