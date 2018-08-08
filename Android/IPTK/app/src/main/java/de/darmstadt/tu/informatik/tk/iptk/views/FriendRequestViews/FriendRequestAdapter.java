package de.darmstadt.tu.informatik.tk.iptk.views.FriendRequestViews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.darmstadt.tu.informatik.tk.iptk.R;
import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;
import de.darmstadt.tu.informatik.tk.iptk.entities.User;

/**
 * The type Friend request adapter.
 */
public class FriendRequestAdapter extends RecyclerView.Adapter{

    private BaseFragmentActivity mActivity;
    private LayoutInflater mInflater;
    private List<User> mUsers;
    private OnOptionListener mListener;

    /**
     * Instantiates a new Friend request adapter.
     *
     * @param mActivity the m activity
     * @param mListener the m listener
     */
    public FriendRequestAdapter(BaseFragmentActivity mActivity, OnOptionListener mListener) {
        this.mActivity = mActivity;
        this.mListener = mListener;
        mInflater = mActivity.getLayoutInflater();
        mUsers = new ArrayList<>();
    }


    /**
     * Sets users.
     *
     * @param users the users
     */
    public void setmUsers(List<User> users) {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_friend_requests,parent,false);
        final FriendRequestsViewHolder friendRequestsViewHolder = new FriendRequestsViewHolder(view);

        friendRequestsViewHolder.approveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = (User) friendRequestsViewHolder.itemView.getTag();
                mListener.OnOptionClicked(user,"0");
            }
        });

        friendRequestsViewHolder.rejectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = (User) friendRequestsViewHolder.itemView.getTag();
                mListener.OnOptionClicked(user,"1");
            }
        });

        return friendRequestsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FriendRequestsViewHolder) holder).populate(mActivity,mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    /**
     * The interface On option listener.
     */
    public interface OnOptionListener{
        /**
         * On option clicked.
         *
         * @param user   the user
         * @param result the result
         */
        void OnOptionClicked(User user, String result);
    }
}
