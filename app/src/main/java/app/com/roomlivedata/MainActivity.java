package app.com.roomlivedata;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import app.com.Extras.RecyclerView_ItemClickListener;
import app.com.View.DeliveryDetails;
import app.com.View.DeliveryList;
import app.com.model.DeliveryDataModel;

public class MainActivity extends AppCompatActivity implements RecyclerView_ItemClickListener
{
    //Application supports the latest Android Architecture Component ie:
    //Code Structure: MVVM (Model View View Controller) with Live Data and View Model are Used.
    //For offline storage : Android Latest "Room" database is used.
    //Application support both Potrait and Landsacpe mode in Mobile and Tablet Devices.

    // On MainActivity both the Fragment ie DeliveryDetails and DeliveryList are attached and visible according to device.
    // In Mobile mode 1 fragment will be visible at one time but in case of Tablet mode both fragments will be visible.
    // Application support "Support multi pane layout".

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check whether the Activity is using the layout verison with the fragment_container
        // FrameLayout and if so we must add the first fragment
        if (findViewById(R.id.fragment_container) != null)
        {
            // However if we are being restored from a previous state, then we don't
            // need to do anything and should return or we could end up with overlapping Fragments
            if (savedInstanceState != null)
            {
                return;
            }

            // Create an Instance of Fragment

            Fragment fragment = new DeliveryList();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override //onItemClick is an Override method from RecyclerViewAdapter to provide the item click listener on recyclerview
    public void onItemClick(View view, int position,ArrayList<DeliveryDataModel> deliveryArrayList)
    {
        DeliveryDetails deliveryDetailsFragment = (DeliveryDetails) getSupportFragmentManager()
                .findFragmentById(R.id.deliveryDetailsFragment);

        if (deliveryDetailsFragment != null)
        {
            // If description is available, we are in two pane layout
            // so we call the method in DeliveryDetails Fragment to update its content
            deliveryDetailsFragment.setDescription(position,deliveryArrayList);
        }
        else
        {
            DeliveryDetails fragment = new DeliveryDetails();
            Bundle args = new Bundle();

            args.putParcelableArrayList(getResources().getString(R.string.deliveryArrayList),deliveryArrayList);
            args.putInt(getResources().getString(R.string.selectedPosition),position);
            fragment.setArguments(args);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the backStack so the User can navigate back
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed()
    {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        // On click of onBackPressed the "Count" is playing a vital role if both the fragment is visible like in Tablet Mode
        //then count value is 2 otherwise count value will be 1.

        if (count == 1)
        {
            super.onBackPressed();
            finish();
        }
        else
        {
           getSupportFragmentManager().popBackStack();
        }
    }
}
