package app.com.ViewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
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

        Log.i("Main", "DeliveryDataViewModel instance: ");

        databaseInstance = DeliveryDataBase.getDatabaseInstance(this.getApplication());

        deliveryList = databaseInstance.deliveryDataDaoAccess().fetchAllData();
    }

    public LiveData<List<DeliveryDataModel>> getDeliveryList(Context context)
    {
        Log.i("Main", "getDeliveryList: ");
        if (deliveryList == null)
        {
            Log.i("Main", "getDeliveryList if: ");
            deliveryList = new MutableLiveData<>();
        }

        getData();

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

        private DeliveryDataBase db;

        DeliveryDataAsync(DeliveryDataBase appDatabase)
        {
            db = appDatabase;
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

                        itemCount = db.deliveryDataDaoAccess().getDeliveryById(i+1).getCount();

                        if(itemCount == 0)
                        {
                            Log.i("Main", "doInBackground if ");
                            db.deliveryDataDaoAccess().insertDeliveryList(deliveryDataModel);
                        }
                        else
                        {
                            Log.i("Main", "doInBackground else ");
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

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            //To after addition operation here.
        }
    }

    private void getData()
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
        new DeliveryDataAsync(databaseInstance).execute(response);
    }

}
