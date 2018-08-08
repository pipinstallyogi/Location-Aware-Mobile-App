package de.darmstadt.tu.informatik.tk.iptk.views.PlaceReviewViews;

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
import de.darmstadt.tu.informatik.tk.iptk.entities.Reviews;

/**
 * The type Place review view adapter.
 */
public class PlaceReviewViewAdapter extends RecyclerView.Adapter<PlaceReviewViewAdapter.PlaceReviewViewHolder>{

    private ArrayList<Reviews> mReviewsList;

    private OnItemClickListener listener;

    private Context mContext;


    /**
     * Instantiates a new Place review view adapter.
     *
     * @param mReviewsList the m reviews list
     * @param mContext     the m context
     * @param listener     the listener
     */
    public PlaceReviewViewAdapter(ArrayList<Reviews> mReviewsList,Context mContext, OnItemClickListener listener) {
        this.mReviewsList = mReviewsList;
        this.listener = listener;
        this.mContext = mContext;
    }

    @Override
    public PlaceReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reviews,parent,false);
        return new PlaceReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceReviewViewHolder holder, int position) {
        holder.mReviewText.setText(mReviewsList.get(position).getReview());
        holder.mReviewTime.setText(mReviewsList.get(position).getLatestReviewDate());
        holder.mReviewName.setText(mReviewsList.get(position).getUserName());
        holder.mReviewEmail.setText(mReviewsList.get(position).getEmail());
        Picasso.with(mContext).load(mReviewsList.get(position).getUserPicture()).into(holder.mReviewPicture);
    }

    @Override
    public int getItemCount() {
        return mReviewsList.size();
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
     * The type Place review view holder.
     */
    public class PlaceReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView mReviewName,mReviewEmail,mReviewTime,mReviewText;
        private ImageView mReviewPicture;

        /**
         * Instantiates a new Place review view holder.
         *
         * @param itemView the item view
         */
        public PlaceReviewViewHolder(final View itemView) {
            super(itemView);

            mReviewName = (TextView)itemView.findViewById(R.id.list_review_UserName);
            mReviewEmail = (TextView)itemView.findViewById(R.id.list_review_UserEmail);
            mReviewText = (TextView)itemView.findViewById(R.id.list_review_reviewText);
            mReviewTime = (TextView)itemView.findViewById(R.id.list_review_time);
            mReviewPicture = (ImageView)itemView.findViewById(R.id.list_review_UserPicture);

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

