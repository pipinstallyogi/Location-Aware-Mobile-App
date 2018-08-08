package de.darmstadt.tu.informatik.tk.iptk.views.FriendRequestViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.darmstadt.tu.informatik.tk.iptk.R2;
import de.darmstadt.tu.informatik.tk.iptk.entities.User;

/**
 * The type Friend requests view holder.
 */
public class FriendRequestsViewHolder extends RecyclerView.ViewHolder {

    /**
     * The User picture.
     */
    @BindView(R2.id.list_friend_request_userPicture)
    ImageView userPicture;

    /**
     * The User name.
     */
    @BindView(R2.id.list_friend_request_userName)
    TextView userName;

    /**
     * The Approve image view.
     */
    @BindView(R2.id.list_friend_request_acceptRequest)
    ImageView approveImageView;

    /**
     * The Reject image view.
     */
    @BindView(R2.id.list_friend_request_rejectRequest)
    ImageView rejectImageView;

    /**
     * Instantiates a new Friend requests view holder.
     *
     * @param itemView the item view
     */
    public FriendRequestsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    /**
     * Populate.
     *
     * @param context the context
     * @param user    the user
     */
    public void populate(Context context, User user){
        itemView.setTag(user);

        userName.setText(user.getUserName());

        Picasso.with(context)
                .load(user.getUserPicture())
                .into(userPicture);
    }
}
