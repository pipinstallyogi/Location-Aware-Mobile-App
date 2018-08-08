package de.darmstadt.tu.informatik.tk.iptk.views.FindFriendsViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.R2;
import de.darmstadt.tu.informatik.tk.iptk.entities.User;
import de.darmstadt.tu.informatik.tk.iptk.utilities.Constants;

/**
 * The type Find friends view holder.
 */
public class FindFriendsViewHolder extends RecyclerView.ViewHolder {

    /**
     * The M user picture.
     */
    @BindView(R2.id.list_user_userPicture)
    ImageView mUserPicture;

    /**
     * The M add friend.
     */
    @BindView(R2.id.list_user_addFriend)
    public ImageView mAddFriend;

    /**
     * The M user name.
     */
    @BindView(R2.id.list_user_userName)
    TextView mUserName;

    /**
     * The M user status.
     */
    @BindView(R2.id.list_user_userStatus)
    TextView mUserStatus;


    /**
     * Instantiates a new Find friends view holder.
     *
     * @param itemView the item view
     */
    public FindFriendsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    /**
     * Populate.
     *
     * @param context                  the context
     * @param user                     the user
     * @param friendRequestSentMap     the friend request sent map
     * @param friendRequestRecievedMap the friend request recieved map
     * @param currentUsersFriendMap    the current users friend map
     */
    public void populate(Context context, User user,
                         HashMap<String,User> friendRequestSentMap,
                         HashMap<String,User> friendRequestRecievedMap,
                         HashMap<String,User> currentUsersFriendMap){
        itemView.setTag(user);
        mUserName.setText(user.getUserName());

        Picasso.with(context)
                .load(user.getUserPicture())
                .into(mUserPicture);

        if (Constants.isIncludedInMap(friendRequestSentMap,user)){
            mUserStatus.setVisibility(View.VISIBLE);
            mUserStatus.setText("Friend Request Sent");
            mAddFriend.setImageResource(R.drawable.ic_cancel_request);
            mAddFriend.setVisibility(View.VISIBLE);
        } else if (Constants.isIncludedInMap(friendRequestRecievedMap,user)){
            mAddFriend.setVisibility(View.GONE);
            mUserStatus.setVisibility(View.VISIBLE);
            mUserStatus.setText("This User Has Requested You");
        } else if(Constants.isIncludedInMap(currentUsersFriendMap,user)){
            mUserStatus.setVisibility(View.VISIBLE);
            mUserStatus.setText("User Added!");
            mAddFriend.setVisibility(View.GONE);
        } else{
            mAddFriend.setVisibility(View.VISIBLE);
            mUserStatus.setVisibility(View.GONE);
            mAddFriend.setImageResource(R.drawable.ic_add);
        }
    }
}
