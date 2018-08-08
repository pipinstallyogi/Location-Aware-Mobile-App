package de.darmstadt.tu.informatik.tk.iptk.views.PlaceCheckinViews;

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
import de.darmstadt.tu.informatik.tk.iptk.entities.Checkin;

/**
 * Created by aditya on 8/28/17.
 */
public class CheckinsViewAdapter extends RecyclerView.Adapter<CheckinsViewAdapter.CheckinsViewHolder> {

    private ArrayList<Checkin> mCheckinList;

    private OnItemClickListener listener;

    private Context mContext;

    /**
     * Instantiates a new Checkins view adapter.
     *
     * @param mCheckinList the m checkin list
     * @param mContext     the m context
     * @param listener     the listener
     */
    public CheckinsViewAdapter(ArrayList<Checkin> mCheckinList, Context mContext, OnItemClickListener listener) {
        this.mCheckinList = mCheckinList;
        this.listener = listener;
        this.mContext = mContext;
    }


    @Override
    public CheckinsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_checkins,parent,false);
        return new CheckinsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CheckinsViewHolder holder, int position) {
        holder.mCheckinName.setText(mCheckinList.get(position).getUserName());
        holder.mCheckinTime.setText(mCheckinList.get(position).getLatestCheckin());
        holder.mCheckinEmail.setText(mCheckinList.get(position).getEmail());
        Picasso.with(mContext).load(mCheckinList.get(position).getUserPicture()).into(holder.mCheckinPicture);

    }


    @Override
    public int getItemCount() {
        return mCheckinList.size();
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
     * The type Checkins view holder.
     */
    public class CheckinsViewHolder extends RecyclerView.ViewHolder{

        private TextView mCheckinName,mCheckinEmail,mCheckinTime;
        private ImageView mCheckinPicture;

        /**
         * Instantiates a new Checkins view holder.
         *
         * @param itemView the item view
         */
        public CheckinsViewHolder(final View itemView) {
            super(itemView);

            mCheckinName = (TextView)itemView.findViewById(R.id.list_checkin_UserName);
            mCheckinEmail = (TextView)itemView.findViewById(R.id.list_checkin_UserEmail);
            mCheckinTime = (TextView)itemView.findViewById(R.id.list_checkin_time);
            mCheckinPicture = (ImageView)itemView.findViewById(R.id.list_checkin_UserPicture);

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
