package de.darmstadt.tu.informatik.tk.iptk.views.SearchResultsViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.entities.Results;

/**
 * Created by aditya on 8/21/17.
 */
public class PlaceResultsViewAdapter extends RecyclerView.Adapter<PlaceResultsViewAdapter.PlaceResultsViewHolder> {

    private ArrayList<Results> mResultsList;

    private OnItemClickListener listener;

    private Context mContext;


    /**
     * Instantiates a new Place results view adapter.
     *
     * @param mResultsList the m results list
     * @param mContext     the m context
     * @param listener     the listener
     */
    public PlaceResultsViewAdapter(ArrayList<Results> mResultsList, Context mContext, OnItemClickListener listener) {
        this.mResultsList = mResultsList;
        this.mContext = mContext;
        this.listener = listener;
    }


    @Override
    public PlaceResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_results_recycler_view, parent, false);
        return new PlaceResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceResultsViewHolder holder, int position) {

        holder.mPlaceName.setText(mResultsList.get(position).getName());
        holder.mPlaceId.setText(mResultsList.get(position).getPlaceId());
        holder.mPlaceAddress.setText(mResultsList.get(position).getFormatted_address());
        Picasso.with(mContext).load(mResultsList.get(position).getIcon()).into(holder.mPlaceIcon);

    }

    @Override
    public int getItemCount() {
        return mResultsList.size();
    }


    /**
     * The interface On item click listener.
     */
    public interface OnItemClickListener {
        /**
         * On item click.
         *
         * @param itemView the item view
         * @param position the position
         */
        void onItemClick(View itemView, int position);
    }


    /**
     * The type Place results view holder.
     */
    public class PlaceResultsViewHolder extends RecyclerView.ViewHolder  {

        private TextView mPlaceName, mPlaceId, mPlaceAddress;
        private ImageView mPlaceIcon;

        /**
         * Instantiates a new Place results view holder.
         *
         * @param view the view
         */
        private PlaceResultsViewHolder(final View view) {
            super(view);

            mPlaceName = (TextView) view.findViewById(R.id.places_results_recyclerview_NameTextView);
            mPlaceId = (TextView) view.findViewById(R.id.places_results_recyclerview_IdTextView);
            mPlaceAddress = (TextView) view.findViewById(R.id.places_results_recyclerview_AddressTextView);
            mPlaceIcon = (ImageView) view.findViewById(R.id.places_results_recyclerview_IconImageView);

            view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        listener.onItemClick(view,position );
                    }
                }
            });

        }
    }
}
