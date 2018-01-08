package app.com.RoomDAO;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import app.com.model.DeliveryDataModel;
import app.com.roomlivedata.R;

/*
 * Created by Yash on 4/1/18.
 */

/**
 * The Room database.
 */

@Database(entities = {DeliveryDataModel.class}, version = 1)
public abstract class DeliveryDataBase extends RoomDatabase
{
    /**
     * @return The DAO for the Delivery_Database table.
     */
    public abstract DeliveryDataDaoAccess deliveryDataDaoAccess();

    /** The DeliveryDataBase instance */
    private static DeliveryDataBase mInstance;

    /**
     * Gets the singleton instance of DeliveryDataBase.
     *
     * @param context The context.
     * @return The singleton instance of DeliveryDataBase.
     */
    public static DeliveryDataBase getDatabaseInstance(Context context)
    {
        if (mInstance == null)
        {                                                                                                                       /* The name of the Database table. */
            mInstance = Room.databaseBuilder(context.getApplicationContext(), DeliveryDataBase.class, context.getResources().getString(R.string.databaseTableName)).build();
        }

        return mInstance;
    }
}
