package de.darmstadt.tu.informatik.tk.iptk.views.PhotosViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.entities.Photos;

/**
 * Created by aditya on 8/28/17.
 */
public class PhotosViewsAdapter extends RecyclerView.Adapter<PhotosViewsAdapter.PhotosViewsHolder>{

    private ArrayList<Photos> mPhotosList;

    private OnItemClickListener listener;

    private Context mContext;

    /**
     * Instantiates a new Photos views adapter.
     *
     * @param mPhotosList the m photos list
     * @param mContext    the m context
     * @param listener    the listener
     */
    public PhotosViewsAdapter(ArrayList<Photos> mPhotosList, Context mContext, OnItemClickListener listener) {
        this.mPhotosList = mPhotosList;
        this.listener = listener;
        this.mContext = mContext;
    }


    @Override
    public PhotosViewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_photos,parent,false);
        return new PhotosViewsHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotosViewsHolder holder, int position) {
        Picasso.with(mContext).load(mPhotosList.get(position).getPhotoUrl()).into(holder.mPlacePicture);
        Log.d("url",mPhotosList.get(position).getPhotoUrl());
    }


    @Override
    public int getItemCount() {
        return mPhotosList.size();
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
     * The type Photos views holder.
     */
    public class PhotosViewsHolder extends RecyclerView.ViewHolder{

        private ImageView mPlacePicture;

        /**
         * Instantiates a new Photos views holder.
         *
         * @param itemView the item view
         */
        public PhotosViewsHolder(final View itemView) {
            super(itemView);

            mPlacePicture = (ImageView)itemView.findViewById(R.id.list_photos_ImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.onItemClick(itemView,position);
                }
            });

        }
    }

}
