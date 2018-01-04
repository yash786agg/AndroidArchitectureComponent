package app.com.Extras;

import android.annotation.SuppressLint;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/*
 * Created by Yash on 4/1/18.
 */

public class ApiRequestSingleton
{
    @SuppressLint("StaticFieldLeak")
    private static ApiRequestSingleton mInstance = null;
    private RequestQueue mRequestQueue;

    @SuppressLint("StaticFieldLeak")
    private static Context mCtc;

    private ApiRequestSingleton(Context context)
    {
        mCtc = context;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized ApiRequestSingleton getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new ApiRequestSingleton(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(mCtc.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
