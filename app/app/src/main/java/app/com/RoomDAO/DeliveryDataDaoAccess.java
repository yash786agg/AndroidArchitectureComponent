package app.com.RoomDAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;
import app.com.model.DeliveryDataModel;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/*
 * Created by Yash on 4/1/18.
 */

@Dao//Data Access Object ,it is a main component of Room and are responsible for defining the methods that access the database.
public interface DeliveryDataDaoAccess
{
   /**
    * Fetch all delivery item from the database.
    */

   @Query("SELECT * FROM DeliveryDataModel")
   LiveData<List<DeliveryDataModel>> fetchAllData();

   /**
    * Inserts new delivery item into the database.
    */

   @Insert(onConflict = REPLACE)
   void insertDeliveryList(DeliveryDataModel deliveryData);

   /**
    * Update the item. The item is identified by the slNo .
    */

   @Update(onConflict = REPLACE)
   void updateDeliveryList(DeliveryDataModel deliveryData);

   /**
    * Counts the number of items in the table.
    *
    * @return The number of delivery list.
    */

   @Query("SELECT * FROM DeliveryDataModel where slNo = :slNo")
   Cursor getDeliveryById(int slNo);
}
