package de.darmstadt.tu.informatik.tk.iptk.views.UserFriendViews;

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
 * The type User friend view holder.
 */
public class UserFriendViewHolder extends RecyclerView.ViewHolder {

    /**
     * The M user picture.
     */
    @BindView(R2.id.list_users_friends_friendImageView)
    ImageView mUserPicture;

    /**
     * The M user name.
     */
    @BindView(R2.id.list_users_friends__userName)
    TextView mUserName;

    /**
     * The M start chat.
     */
    @BindView(R2.id.list_users_friends_startChat)
    ImageView mStartChat;

    /**
     * Instantiates a new User friend view holder.
     *
     * @param itemView the item view
     */
    public UserFriendViewHolder(View itemView) {
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
        Picasso.with(context)
                .load(user.getUserPicture())
                .into(mUserPicture);

        mUserName.setText(user.getUserName());
    }
}
