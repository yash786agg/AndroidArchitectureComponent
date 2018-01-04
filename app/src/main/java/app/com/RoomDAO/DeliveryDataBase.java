package app.com.RoomDAO;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import app.com.model.DeliveryDataModel;

/*
 * Created by Yash on 4/1/18.
 */

@Database(entities = {DeliveryDataModel.class}, version = 1)
public abstract class DeliveryDataBase extends RoomDatabase
{
    public abstract DeliveryDataDaoAccess deliveryDataDaoAccess();

    private static DeliveryDataBase mInstance;

    public static DeliveryDataBase getDatabaseInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = Room.databaseBuilder(context.getApplicationContext(), DeliveryDataBase.class, "Delivery_Database").build();
        }
        return mInstance;
    }

    public static void destroyDatabaseInstance()
    {
        mInstance.close();
        mInstance = null;
    }
}
