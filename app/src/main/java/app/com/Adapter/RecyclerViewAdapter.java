package app.com.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import app.com.Extras.RecyclerView_ItemClickListener;
import app.com.model.DeliveryDataModel;
import app.com.roomlivedata.R;

/*
 * Created by Yash on 5/1/18.
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{
    private ArrayList<DeliveryDataModel> deliverArrayList;
    private LayoutInflater layoutInflater;
    private Context context;
    private RecyclerView_ItemClickListener clickListener;

    public RecyclerViewAdapter(Context context, ArrayList<DeliveryDataModel> deliverArrayList)
    {
        /*
         * RecyclerViewAdapter Constructor to Initialize Data which we get from MainActivity
         **/

        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.deliverArrayList = deliverArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        /*
         * LayoutInflater is used to Inflate the view
         * from fragment_listview_adapter
         * for showing data in RecyclerView
         **/

        View view = layoutInflater.inflate(R.layout.adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.MyViewHolder holder, final int position)
    {
        /*
         * onBindViewHolder is used to Set all the respective data
         * to Textview or Imagview form worldpopulation_pojoArrayList
         * ArrayList Object.
         **/

        if (!TextUtils.isEmpty(deliverArrayList.get(position).getDescription()) && !TextUtils.isEmpty(deliverArrayList.get(position).getAddress()))
        {
            String deliveryAddress = deliverArrayList.get(position).getDescription()+" "+context.getResources().getString(R.string.at)+" "+deliverArrayList.get(position).getAddress();

            holder.deliveryDescrp.setText(deliveryAddress);
        }
        else if(!TextUtils.isEmpty(deliverArrayList.get(position).getDescription()))
        {
            String deliveryAddress = deliverArrayList.get(position).getDescription();

            holder.deliveryDescrp.setText(deliveryAddress);
        }
        else
        {
            holder.deliveryDescrp.setText(context.getResources().getString(R.string.addressNotAvailable));
        }

        if (!TextUtils.isEmpty(deliverArrayList.get(position).getImageUrl()))
        {
            Picasso.with(context)
                    .load("https://asia-public.foodpanda.com/dynamic/production/in/images/vendors/s5gg_sqp.jpg?v=20170908125114")
                    .into(holder.deliveryImg);
        }
    }

    @Override
    public int getItemCount()
    {
        /*
         * getItemCount is used to get the size of respective worldpopulation_pojoArrayList ArrayList
         **/

        return deliverArrayList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        /*
         * Return the view type of the item at position for the purposes of view recycling.
         **/

        return position;
    }

    public void addItems(ArrayList<DeliveryDataModel> deliverArrayList)
    {
        this.deliverArrayList = deliverArrayList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(final RecyclerView_ItemClickListener mItemClickListener)
    {
        this.clickListener = mItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView deliveryDescrp;
        ImageView deliveryImg;

        /**
         * MyViewHolder is used to Initializing the view.
         **/

        MyViewHolder(View itemView)
        {
            super(itemView);

            deliveryDescrp = itemView.findViewById(R.id.deliveryDescrp);

            deliveryImg = itemView.findViewById(R.id.deliveryImg);

            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view)
        {
            if (clickListener != null)
            {
                clickListener.onItemClick(view,getAdapterPosition());
            }
        }
    }
}