package app.com.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
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
    private ArrayList<DeliveryDataModel> deliveryArrayList;
    private LayoutInflater layoutInflater;
    private Context context;
    private RecyclerView_ItemClickListener clickListener;

    public RecyclerViewAdapter(Context context, ArrayList<DeliveryDataModel> deliveryArrayList)
    {
        /*
         * RecyclerViewAdapter Constructor to Initialize Data which we get from DeliveryList Fragment
         */

        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.deliveryArrayList = deliveryArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        /*
         * LayoutInflater is used to Inflate the view
         * from adapter_layout
         * for showing data in RecyclerView
         */

        View view = layoutInflater.inflate(R.layout.adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.MyViewHolder holder, final int position)
    {
        /*
         * onBindViewHolder is used to Set all the respective data
         * to Textview or Imagview form deliveryArrayList
         * ArrayList Object.
         */

        if (!TextUtils.isEmpty(deliveryArrayList.get(position).getDescription()) && !TextUtils.isEmpty(deliveryArrayList.get(position).getAddress()))
        {
            String deliveryAddress = deliveryArrayList.get(position).getDescription()+" "+context.getResources().getString(R.string.at)+" "+deliveryArrayList.get(position).getAddress();

            holder.deliveryDescrp.setText(deliveryAddress);
        }
        else if(!TextUtils.isEmpty(deliveryArrayList.get(position).getDescription()))
        {
            String deliveryAddress = deliveryArrayList.get(position).getDescription();

            holder.deliveryDescrp.setText(deliveryAddress);
        }
        else
        {
            holder.deliveryDescrp.setText(context.getResources().getString(R.string.addressNotAvailable));
        }

        if (!TextUtils.isEmpty(deliveryArrayList.get(position).getImageUrl()))
        {
            Picasso.with(context)
                    .load(deliveryArrayList.get(position).getImageUrl())
                    .into(holder.deliveryImg, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                            {
                                holder.deliveryImg.setBackground(null);
                            }
                            else
                            {
                                holder.deliveryImg.setBackgroundDrawable(null);
                            }
                        }

                        @Override
                        public void onError()
                        {}
                    });
        }
    }

    @Override
    public int getItemCount()
    {
        /*
         * getItemCount is used to get the size of respective deliveryArrayList
         */

        return deliveryArrayList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        /*
         * Return the view type of the item at position for the purposes of view recycling.
         */

        return position;
    }

    public void addItems(ArrayList<DeliveryDataModel> deliverArrayList)
    {
        /*
         * addItems method is to add items in the deliverArrayList and notifiy the adapter for the data change.
         */

        this.deliveryArrayList = deliverArrayList;
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

        /*
         * MyViewHolder is used to Initializing the view.
         */

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
                clickListener.onItemClick(view,getAdapterPosition(),deliveryArrayList);
            }
        }
    }
}