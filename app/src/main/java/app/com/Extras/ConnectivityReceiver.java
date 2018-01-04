package app.com.Extras;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Created by Yash on 4/1/18.
 */

public class ConnectivityReceiver
{
    private static ConnectivityReceiver.AsyncResponse asyncResponse;

    /***************** Method for checking internet connection  *******/
    public static boolean isNetworkAvailable(Context context)
    {

        ConnectivityManager connectivity = null;
        boolean isNetworkAvail = false;
        try
        {
            connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null)
            {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                {
                    for(int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        {
                            return true;
                        }
                }
            }
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connectivity != null)
            {
                connectivity = null;
            }
        }
        return isNetworkAvail;
    }

    private static class ConnectionCheck extends AsyncTask<String,Void,Boolean>
    {

        @Override
        protected Boolean doInBackground(String... params)
        {
            HttpURLConnection urlConnection = null;
            try
            {
                if(params[0].endsWith("") || params[0] == null)
                {
                    params[0] = "http://www.google.com/";
                }

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                if (!url.getHost().equals(urlConnection.getURL().getHost()))
                {
                    // we were redirected! Kick the user out to the browser to sign on?

                    return true;
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();

                return false;
            }
            finally
            {
                urlConnection.disconnect();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);
            asyncResponse.processFinish(aBoolean);

        }
    }


    public interface AsyncResponse
    {
        void processFinish(boolean output);
    }
    public static void speedTest(ConnectivityReceiver.AsyncResponse asyncResponse)
    {
        ConnectivityReceiver.asyncResponse = asyncResponse;
        String url = "http://www.google.com/";
        new ConnectivityReceiver.ConnectionCheck().execute(url);
    }
}