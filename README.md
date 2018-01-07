# AndroidArchitectureComponent
=============================================

This sample showcases the following Architecture Components:

* [Room](https://developer.android.com/topic/libraries/architecture/room.html)
* [ViewModels](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html)
* [LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData.html)

Introduction
-------------
### Features

This sample contains two screens: a list of delivery and a delivery detail view.

#### Presentation layer

The presentation layer consists of the following components:
* A main activity that handles 2 Fragments.
* A fragment to display the list of delivery.
* A fragment to display a delivery detail review with google map.

The app uses a Model-View-ViewModel (MVVM) architecture for the presentation layer. Each of the fragments corresponds to a MVVM View. The View and ViewModel communicate  using LiveData and the following design principles:

* ViewModel objects don't have references to activities, fragments, or Android views. That would cause leaks on configuration changes, such as a screen rotation, because the system retains a ViewModel across the entire lifecycle of the corresponding view.
* ViewModel objects expose data using `LiveData` objects. `LiveData` allows you to observe changes to data across multiple components of your app without creating explicit and rigid dependency paths between them.
* Views, including the fragments used in this sample, subscribe to corresponding `LiveData` objects. Because `LiveData` is lifecycle-aware, it doesn’t push changes to the underlying data if the observer is not in an active state, and this helps to avoid many common bugs. This is an example of a subscription:

```java
viewModel.getDeliveryList().observe(this, new Observer<List<DeliveryDataModel>>()
        {
            @Override
            public void onChanged(@Nullable List<DeliveryDataModel> deliveryData)
            {
                // Update the UI.

                deliveryArrayList = (ArrayList<DeliveryDataModel>) deliveryData;

                recyclerViewAdapter.addItems(deliveryArrayList);
            }
        });
  ```      
 #### Data layer
 Room populates the database asynchronously when it's created, via the `RoomDatabase#Callback`. To simulate low-performance, an artificial delay is added. To let 
 other components know when the data has finished populating, the `AppDatabase` exposes a 
 `LiveData` object..

To access the data and execute queries, you use a [Data Access Object](https://developer.android.com/topic/libraries/architecture/room.html#daos) (DAO). For example, a product is loaded with the following query:

```java
   /**
    * Fetch all delivery item from the database.
    */

   @Query("SELECT * FROM DeliveryDataModel")
   LiveData<List<DeliveryDataModel>> fetchAllData();
   
    /**
    * Counts the number of items in the table.
    *
    * @return The number of delivery list.
    */

   @Query("SELECT * FROM DeliveryDataModel where slNo = :slNo")
   Cursor getDeliveryById(int slNo);
   
```

Queries that return a `LiveData` object can be observed, so when  a change in one of the affected tables is detected, `LiveData` delivers a notification of that change to the registered observers.

