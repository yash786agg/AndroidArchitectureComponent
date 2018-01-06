package app.com.roomlivedata;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import app.com.Extras.RecyclerView_ItemClickListener;
import app.com.View.DeliveryDetails;
import app.com.View.DeliveryList;
import app.com.model.DeliveryDataModel;

public class MainActivity extends AppCompatActivity implements RecyclerView_ItemClickListener
{
    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

   /* private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<DeliveryDataModel> deliveryArrayList;*/
    private static final String TAG = "MainActivity";

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

        /*RecyclerView deliveryRclv = findViewById(R.id.deliveryRclv);

        deliveryArrayList = new ArrayList<>();

        recyclerViewAdapter = new RecyclerViewAdapter(this,deliveryArrayList);
        deliveryRclv.setLayoutManager(new LinearLayoutManager(this));

        deliveryRclv.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListener(this);

        Button landscape = findViewById(R.id.landscape);
        landscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: landscape");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        Button potrait = findViewById(R.id.potrait);
        potrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: potrait");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });*/
    }

   /* @Override
    protected void onResume()
    {
        super.onResume();

        Log.i(TAG, "onResume: ");

        DeliveryDataViewModel viewModel = ViewModelProviders.of(this).get(DeliveryDataViewModel.class);

        viewModel.getDeliveryList(this).observe(MainActivity.this, new Observer<List<DeliveryDataModel>>()
        {
            @Override
            public void onChanged(@Nullable List<DeliveryDataModel> deliveryData)
            {
                Log.i(TAG, "onChanged: "+deliveryData);

                deliveryArrayList = (ArrayList<DeliveryDataModel>) deliveryData;

                recyclerViewAdapter.addItems(deliveryArrayList);
            }
        });
    }*/


    @Override
    public void onItemClick(View view, int position,ArrayList<DeliveryDataModel> deliveryArrayList)
    {
        DeliveryDetails deliveryDetailsFragment = (DeliveryDetails) getSupportFragmentManager()
                .findFragmentById(R.id.deliveryDetailsFragment);

        Log.i(TAG, "onItemClick: ");

        if (deliveryDetailsFragment != null)
        {
            Log.i(TAG, "onItemClick if: "+deliveryArrayList.size());

            // If description is available, we are in two pane layout
            // so we call the method in DescriptionFragment to update its content
            deliveryDetailsFragment.setDescription(position,deliveryArrayList);
        }
        else
        {
            Log.i(TAG, "onItemClick else: "+deliveryArrayList.size());

            DeliveryDetails fragment = new DeliveryDetails();
            Bundle args = new Bundle();

            args.putParcelableArrayList("deliveryArrayList",deliveryArrayList);
            args.putInt("selectedPosition",position);
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

        Log.i(TAG, "onBackPressed: "+count);

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
