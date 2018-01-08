package app.com.Extras;

import android.view.View;
import java.util.ArrayList;
import app.com.model.DeliveryDataModel;

/*
 * Created by Yash on 5/1/18.
 */

public interface RecyclerView_ItemClickListener
{
    void onItemClick(View view, int position,ArrayList<DeliveryDataModel> deliveryArrayList);
}
